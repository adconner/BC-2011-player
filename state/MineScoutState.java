package hex.state;

import hex.data.Extra;
import hex.data.RobotControls;
import hex.navigation.*;

import java.util.ArrayList;
import java.util.Random;

import battlecode.common.*;

public class MineScoutState extends AbstractState {

	private Navigator navi;
	
	public MineScoutState(RobotController RC, RobotControls comp, Navigator n) {
		super(RC, comp);
		navi = n;
	}

	@Override
	public void run() {
    	MovementController motor = robotComps.mover;
    	boolean mineTargetSet = false; //Tells if the bot's target has been set to a mine
    	while (true) {
            try {
                while (motor.isActive()) {
                    myRC.yield();
                }
                MapLocation mine = searchForMine();
                MapLocation myLoc = myRC.getLocation();
                while (motor.isActive())
                    myRC.yield();
        		if (!mine.equals(myRC.getLocation())) {
            		if (motor.withinRange(mine)) {
            			Direction dir = Extra.dirTo(myLoc, mine);
            			if (dir==Direction.OMNI || dir==Direction.NONE){
                			System.out.println('4');
                			navi = new Bug(emptylocNextTo(mine), robotComps);
                			navi.move();
                			while (motor.isActive()) {
                                myRC.yield();
                            }
                			myLoc = myRC.getLocation();
                			motor.setDirection(Extra.dirTo(myLoc, mine));
                		}
                			
            			if(Extra.senseIfClear(robotComps, mine) && myRC.getTeamResources()>=Chassis.BUILDING.cost+ComponentType.RECYCLER.cost) {//If the mine is empty build a recycler
    						System.out.println('5');
            				robotComps.builder.build(Chassis.BUILDING, mine);
            				while (robotComps.builder.isActive())
            					myRC.yield();
            				robotComps.builder.build(ComponentType.RECYCLER, mine, RobotLevel.ON_GROUND);
                			motor.setDirection(Extra.dirTo(myRC.getLocation(), mine));
                		}
                			//navi.setTarget(emptylocNextTo(mine));}
                			/*motor.setDirection(myRC.getLocation().directionTo(emptylocNextTo(mine)));
                			while (motor.isActive()) {
                                myRC.yield();
                            }
                			motor.moveForward();}*/
            			if(Extra.senseIfClear(robotComps, mine) && myRC.getTeamResources()>=Chassis.BUILDING.cost+ComponentType.RECYCLER.cost) {//If the mine is empty build a recycler
    						System.out.println('5');
            				robotComps.builder.build(Chassis.BUILDING, mine);
            				while (robotComps.builder.isActive())
            					myRC.yield();
            				robotComps.builder.build(ComponentType.RECYCLER, mine, RobotLevel.ON_GROUND);
    						mineTargetSet = false;
    						navi = new Bug(new MapLocation(0,0).add(Direction.NORTH, 100), robotComps);
    					}
            		}
            		else if (myLoc.equals(mine)) {
            			System.out.println('2');
            			motor.setDirection(myLoc.directionTo(emptylocNextTo(mine)));
            			while (motor.isActive())
                            myRC.yield();
            		}
    				else if (!mineTargetSet){ //Set target as mine location and head there
    					System.out.println('3');
    					//navi.setTarget(mine);
//    					navi = new A_Star(mine, robotComps);
    					navi = new Bug(mine, robotComps);
    					mineTargetSet = true;
    					while (!myRC.getLocation().isAdjacentTo(mine)) {
    						while (motor.isActive())
                                myRC.yield();
    						navi.move();
    					}
    					myLoc = myRC.getLocation();
    				}
            		else if (myRC.getLocation().equals(mine)) {
            			System.out.println('2');
            			motor.setDirection(myRC.getLocation().directionTo(emptylocNextTo(mine)));
            			while (motor.isActive()) {
                            myRC.yield();
                        }
            			//motor.moveForward();
            		}
    				else if (!mineTargetSet){
    					System.out.println('3');
    					//navi.setTarget(mine);
//    					navi = new A_Star(mine, robotComps);
    					navi = new Bug(mine, robotComps);
    					mineTargetSet = true;
    					while (!myRC.getLocation().isAdjacentTo(mine)) {
    						while (motor.isActive()) {
                                myRC.yield();
                            }
    						navi.move();
    					}
    				}
            		while (motor.isActive()) {
                        myRC.yield(); 
                    }
				}               
                
                navi.move();
                while (motor.isActive()) {
                    myRC.yield();
                }
            } catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
    	}
    }
	@Override
	public AbstractState getNextState() {
		return new MineScoutState(myRC, robotComps, navi);
	}
	
	/**
	 * @return returns closest empty mine location
	 * @throws GameActionException 
	 */
	private MapLocation searchForMine() throws GameActionException {
		ArrayList<MapLocation> availableMines = new ArrayList<MapLocation>();
    	
		//rotates to search all around it
		for (int x = 0; x < 3; x++) {
			for (Mine min : robotComps.sensor.senseNearbyGameObjects(Mine.class)) {
				MapLocation l = min.getLocation();
	    		if (!availableMines.contains(l))
	    			availableMines.add(l);
			}
			while (robotComps.mover.isActive())
                myRC.yield();
			robotComps.mover.setDirection(Extra.rotate(myRC.getDirection(), 90));
		}
    	System.out.println(availableMines);
    	if (availableMines.size()>0) 
    		return availableMines.get(0);
    	return myRC.getLocation();
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
}