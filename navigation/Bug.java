package hex.navigation;

import hex.data.Map;
import hex.data.RobotControls;

import java.util.ArrayList;
import battlecode.common.*;

/**Bug movement, dead reckoning to target, and tracing wall if needed */
public class Bug extends Navigator {
	private boolean tracing;
	private Map<MapLocation, ArrayList<Direction>> previousBumps;
	
	
	public Bug(MapLocation tar, RobotControls cont) {
		super(tar, cont);
		tracing = false;
		previousBumps = new Map<MapLocation, ArrayList<Direction>>();
	}
	
	@Override
	public void move() {
		try {
			//System.out.println("Cur: " + myRC.getLocation() + "Dir: " + myRC.getDirection() + "Tar: " + target);
			if (!myRC.getLocation().equals(target))
				if (tracing) {
					if (canDeadReckon()) {
						tracing = false; //Stop tracing
						motor.setDirection(deadReckon());
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();
					}
					else {
						//motor.setDirection(dirToTrace()); //Pick a direction to trace the object
						dirToTrace();
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();
					}
				}
				else {
					if (canDeadReckon()) {
						motor.setDirection(deadReckon());
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();		
					}
					else {
						tracing = true;
						//motor.setDirection(dirToTrace()); //Pick a direction to trace the object
						dirToTrace();
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();
					}
				}
		}
		catch (GameActionException e) {
			System.out.println("caught exception (with Bug):");
			e.printStackTrace();
		}		
	}
	
	private boolean canDeadReckon() {
		return motor.canMove(deadReckon());
	}
	
	private Direction deadReckon() {
		return myRC.getLocation().directionTo(target);
	}
	
	//Picks a direction that will allow it to trace along the object that is blocking the path
	//Right now the code just picks a random direction, which is not the same as tracing.
	//************FIX**************
	private Direction dirToTrace() {
		ArrayList<Direction> triedDirs = new ArrayList<Direction>(); //Directions that have already been tried
		if (previousBumps.contains(myRC.getLocation())) {
			triedDirs = previousBumps.valueOfKey(myRC.getLocation());
		}
		try {
			do {
				Direction dir = myRC.getDirection();					
				if (motor.canMove(dir) && !triedDirs.contains(dir)) {
					triedDirs.add(dir);	
					if (!previousBumps.contains(myRC.getLocation()))
						previousBumps.add(myRC.getLocation(), triedDirs);
					return dir;
				}
				triedDirs.add(dir);	
				while (motor.isActive()) {
                    myRC.yield();
                }
				motor.setDirection(dir.rotateRight());
			} while (true);
		}
		catch (GameActionException e) {
			System.out.println("caught exception (with Bug dirToTrace):");
			e.printStackTrace();
		}
		return Direction.NORTH;
	}
}