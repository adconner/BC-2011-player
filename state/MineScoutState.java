package team146.state;

import team146.data.*;
import team146.navigation.*;

import java.util.ArrayList;
import java.util.Random;

import battlecode.common.*;

public class MineScoutState extends AbstractState {

	private Navigator navi;
	private ArrayList<MapLocation> availableMines = new ArrayList<MapLocation>();
	private RobotSchematic schematic;
	
	public MineScoutState(RobotController RC, RobotControls comp, Navigator n) {
		super(RC, comp);
		navi = n;
		schematic = Schematics.RECYCLER.s;
	}
	public MineScoutState(RobotController RC, RobotControls comp, Navigator n, RobotSchematic s) {
		super(RC, comp);
		navi = n;
		schematic = s;
	}

	@Override
	public void run() {
    	MovementController motor = robotComps.mover;
    	while (true) {
            try {
            	myRC.setIndicatorString(0, "Me " + myRC.getLocation() + " Tar " + navi.getTarget().toString());
                while (motor.isActive()) {
                    myRC.yield();
                }
                MapLocation mine = null;
                while (mine == null) {
                	mine = searchForMine();
                	if (mine == null) navi.explore();
                }
                
                Direction d = myRC.getLocation().directionTo(mine);
                if (d != Direction.OMNI && d != Direction.NONE)
                	motor.setDirection(d);
                moveToBuild(mine);
                
                navi = new A_Star(myRC.getLocation().add(Direction.NORTH, 20), robotComps);
                
//                navi.move();
            } catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
    	}
    }
	
	private void moveToBuild(MapLocation mine) throws GameActionException {
		MovementController motor = robotComps.mover;
    	BuilderController builder = robotComps.builder;
    	boolean mineTargetSet = false; //Tells if the bot's target has been set to a mine
		while (mine != null) {
        	MapLocation myLoc = myRC.getLocation();
        	myRC.setIndicatorString(0, "Me: " + myLoc.toString() + " Mine: " + mine.toString());
        	if (!mineTargetSet){ //Set target as mine location and head there
				System.out.println('3');
				//navi.setTarget(mine);
				navi = new A_Star(mine, robotComps);
//				navi = new Bug(mine, robotComps);
				mineTargetSet = true;
				while (!builder.withinRange(mine)) {
					while (motor.isActive())
                        myRC.yield();
					navi.move();
				}
				myLoc = myRC.getLocation();
			}
        	if (!mine.equals(myLoc)) {
        		if (builder.withinRange(mine)) {
        			Direction dir = Extra.dirTo(myLoc, mine);
        			if (dir==Direction.OMNI || dir==Direction.NONE){
            			System.out.println('4');
//            			navi = new Bug(emptylocNextTo(mine), robotComps);
            			navi = new A_Star(emptylocNextTo(mine), robotComps);
            			navi.move();
            			while (motor.isActive()) {
                            myRC.yield();
                        }
            			myLoc = myRC.getLocation();
            			motor.setDirection(Extra.dirTo(myLoc, mine));
            		}
            			
        			if(Extra.senseIfClear(robotComps, mine) && myRC.getTeamResources()>=schematic.baseCost()) {//If the mine is empty build a recycler
						System.out.println('5');
						BuildHelper.buildInLocation(builder, schematic, mine);
        				/*builder.build(Chassis.BUILDING, mine);
        				while (robotComps.builder.isActive())
        					myRC.yield();
        				builder.build(ComponentType.RECYCLER, mine, RobotLevel.ON_GROUND);*/
						mineTargetSet = false;
						availableMines.remove(mine);
						mine = null;
//						navi = new A_Star(myLoc.add(Direction.NORTH, 100), robotComps);
						navi = new Bug(myRC.getLocation().add(0, 100), robotComps);
						break;
//						navi = new Navigator(myLoc.add(Direction.NORTH, 100), robotComps);
					}
        		}
    		}
    		else {
    			MapLocation mine2 = anotherMineNextDoor();
    			if (mine2 == null) {
    				while (motor.isActive())
                        myRC.yield();
    				motor.moveBackward();
    			}
    			else
    				mine = mine2;
    		}
    		while (motor.isActive()) {
                myRC.yield(); 
            }
    		/*searchForMine();
    		if (!availableMines.contains(mine)) {
    			mine = null;
//				navi = new A_Star(myLoc.add(Direction.NORTH, 100), robotComps);
				navi = new Bug(myRC.getLocation().add(0, 100), robotComps);
				break;
    		}*/
		}
	}

	private MapLocation anotherMineNextDoor() {
		for (MapLocation loc: Extra.locsNextTo(myRC.getLocation()))
			if (availableMines.contains(loc))
				return loc;
		return null;
	}
	@Override
	public AbstractState getNextState() {
//		return new MineScoutState(myRC, robotComps, navi);
		return this;
	}
	
	/**
	 * @return returns closest empty mine location
	 * @throws GameActionException 
	 */
	private MapLocation searchForMine() throws GameActionException {
		//****************
		//Find a better way to implement this part
		//This is here to make sure a scout doesn't stay frozen at a point if a mine is built upon
		/*for (MapLocation l: availableMines)
			if (robotComps.sensor.withinRange(l))
				if (robotComps.sensor.senseObjectAtLocation(l, RobotLevel.ON_GROUND) != null)
					availableMines.remove(l);*/
		if (availableMines.size()>0) 
    		return availableMines.get(0);
		//***********************
		
		
		//rotates to search all around it
		for (int x = 0; x < 4; x++) {
			for (Mine min : robotComps.sensor.senseNearbyGameObjects(Mine.class)) {
				MapLocation l = min.getLocation();
	    		if (!availableMines.contains(l) && robotComps.sensor.senseObjectAtLocation(l, RobotLevel.ON_GROUND) == null)
	    			availableMines.add(l);
			}
			while (robotComps.mover.isActive()) myRC.yield();
			robotComps.mover.setDirection(Extra.rotate(myRC.getDirection(), 90));
		}
    	if (availableMines.size()>0) 
    		return availableMines.get(0);
    	return null;
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