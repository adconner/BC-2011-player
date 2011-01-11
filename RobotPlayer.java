package hex;


import java.util.ArrayList;
import java.util.Random;

import battlecode.common.*;

public class RobotPlayer implements Runnable {

	private final RobotController myRC;
	private RobotControls robotComps = new RobotControls();
	private Bug navi; 
	
	public RobotPlayer(RobotController rc) {
        myRC = rc;
    }
	
	@Override
	public void run() {
		checkForNewComponents();
		
		if(myRC.getChassis()==Chassis.BUILDING) {
			runBuilder();
		}
		else {
			navi = new Bug(new MapLocation(0,0).add(Direction.NORTH, 20), robotComps); //for testing
			runMotor();
		}
	}
	
	private void checkForNewComponents() {
		ComponentController [] components = myRC.newComponents();
		
		for (ComponentController c: components) {
			switch (c.componentClass()) {
				case ARMOR:
				case MISC:
					robotComps.extra.add(c);
					break;
				case BUILDER:
					robotComps.builder = (BuilderController)c;
					break;
				case COMM:
					robotComps.comUnit = (BroadcastController)c;
				case MOTOR:
					robotComps.mover = (MovementController)c;
					break;
				case SENSOR:
					robotComps.sensor = (SensorController)c;
					break;
				case WEAPON:
					robotComps.weapons.add((WeaponController)c);
					break;
			}
		}
		
	}

	public void runBuilder() {	
		MovementController motor = robotComps.mover;
		BuilderController builder = robotComps.builder;
		while (true) {
            try {
				/*myRC.yield();
				
				Mine[] availableMines = robotComps.sensor.senseNearbyGameObjects(Mine.class);
				for (Mine m: availableMines) {
					motor.setDirection(Extra.dirTo(myRC.getLocation(), m.getLocation()));
					if(!motor.canMove(myRC.getDirection()) && myRC.getTeamResources()>=Chassis.BUILDING.cost+ComponentType.RECYCLER.cost) {//If the mine is empty build a recycler
						builder.build(Chassis.BUILDING, m.getLocation());
						builder.build(ComponentType.RECYCLER, m.getLocation(), RobotLevel.MINE);
					}
				}
				/*
				if(!motor.canMove(myRC.getDirection())) //Determines if the tile in front is empty, otherwise, rotates
					motor.setDirection(myRC.getDirection().rotateRight());
				else if(myRC.getTeamResources()>=2*Chassis.LIGHT.cost) {
					builder.build(Chassis.LIGHT,myRC.getLocation().add(myRC.getDirection()));
					builder.build(ComponentType.HAMMER, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
					builder.build(ComponentType.ANTENNA, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
				}*/
            } 
            catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
        }
	}
	
	public MapLocation emptylocNextTo(MapLocation loc) throws GameActionException {
		ArrayList<MapLocation> locs = Extra.locsNextTo(loc);
		for (int i = 0; i < locs.size(); i++) {
			MapLocation l = locs.get(i);
			while (robotComps.mover.isActive()) {
                myRC.yield();
            }
			robotComps.mover.setDirection(Extra.dirTo(myRC.getLocation(), l));
			while (robotComps.mover.isActive()) {
                myRC.yield();
            }
			GameObject ob = robotComps.sensor.senseObjectAtLocation(l, RobotLevel.ON_GROUND);
			if (ob != null)
				locs.remove(i--);
		}
		if (locs.isEmpty())
			return loc;
		Random r = new Random();
		return locs.get(r.nextInt(locs.size()));
	}

    public void runMotor() {
    	MovementController motor = robotComps.mover;
    	boolean mineTargetSet = false; //Tells if the bot's target has been set to a mine
    	while (true) {
            try {
                /*** beginning of main loop ***/
            	checkForNewComponents();
                while (motor.isActive()) {
                    myRC.yield();
                }
                /* Has a CONSTRUCTOR*/
                if (robotComps.builder != null && robotComps.builder.type()==ComponentType.CONSTRUCTOR && robotComps.sensor != null) {
                	Mine[] availableMines = robotComps.sensor.senseNearbyGameObjects(Mine.class);
                	if (availableMines.length>0) {
                		Mine m = availableMines[0];
    					while (motor.isActive()) {
                            myRC.yield();
                        }
                		
                		System.out.println(robotComps.mover.withinRange(m.getLocation()));
                		if (robotComps.mover.withinRange(m.getLocation())) {
                			Direction dir = Extra.dirTo(myRC.getLocation(), m.getLocation());
                			if (dir==Direction.OMNI || dir==Direction.NONE){
                    			System.out.println('4');
                    			navi = new Bug(emptylocNextTo(m.getLocation()), robotComps);
                    			navi.bug();
                    			while (motor.isActive()) {
                                    myRC.yield();
                                }
                    			motor.setDirection(Extra.dirTo(myRC.getLocation(), m.getLocation()));
                    		}
                    			//navi.setTarget(emptylocNextTo(m.getLocation()));}
                    			/*motor.setDirection(myRC.getLocation().directionTo(emptylocNextTo(m.getLocation())));
                    			while (motor.isActive()) {
                                    myRC.yield();
                                }
                    			motor.moveForward();}*/
                			if(myRC.getTeamResources()>=Chassis.BUILDING.cost+ComponentType.RECYCLER.cost) {//If the mine is empty build a recycler
	    						System.out.println('5');
                				robotComps.builder.build(Chassis.BUILDING, m.getLocation());
	    						mineTargetSet = false;
	    					}
                		}
                		else if (myRC.getLocation().equals(m.getLocation())) {
                			System.out.println('2');
                			motor.setDirection(myRC.getLocation().directionTo(emptylocNextTo(m.getLocation())));
                			while (motor.isActive()) {
                                myRC.yield();
                            }
                			//motor.moveForward();
                		}
	    				else if (!mineTargetSet){
	    					System.out.println('3');
	    					//navi.setTarget(m.getLocation());
	    					navi = new Bug(m.getLocation(), robotComps);
	    					mineTargetSet = true;
	    					while (!myRC.getLocation().equals(m.getLocation())) {
	    						while (motor.isActive()) {
	                                myRC.yield();
	                            }
	    						navi.bug();
	    					}
	    				}
                		while (motor.isActive()) {
                            myRC.yield(); 
                        }
    				}
                }
                
                
                navi.bug();
                while (motor.isActive()) {
                    myRC.yield();
                }
                /*if (motor.canMove(myRC.getDirection())) {
                    //System.out.println("about to move");
                    navi.bug();
                }
                else {
                	ComponentController [] comps = myRC.newComponents();
                	if (comps.length>0) {
                		robotComps.weapons.add((WeaponController)comps[0]);
                		if (comps.length>1)
                			robotComps.sensors.add((SensorController)comps[1]);
                	}
                	if (robotComps.sensors.size()>0) {
                		GameObject[] obj = robotComps.sensors.get(0).senseNearbyGameObjects(Robot.class);
                		for (GameObject ob : obj) {
                			myRC.yield();
	                		if (ob instanceof Robot && !ob.getTeam().equals(myRC.getTeam()))
	                			robotComps.weapons.get(0).attackSquare(myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
	                		}
                	}
                	robotComps.mover.setDirection(myRC.getDirection().rotateRight());
                }*/

                /*** end of main loop ***/
            } catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
        }
    }    
}