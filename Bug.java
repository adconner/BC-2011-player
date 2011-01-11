package hex;

import java.util.ArrayList;
import battlecode.common.*;

/**Bug movement, dead reckoning to target, and tracing wall if needed */
public class Bug {
	private boolean tracing = false;
	private MapLocation target;
	private RobotControls control;
	private Map<MapLocation, ArrayList<Direction>> previousBumps = new Map<MapLocation, ArrayList<Direction>>();
	private MovementController motor;
	private RobotController myRC;
	
	public Bug(MapLocation tar, RobotControls cont) {
		target = tar;
		control = cont;
		motor = control.mover;
		myRC = control.mover.getRC();
	}
	
	public void bug() {
		try {
			System.out.println("Cur: " + myRC.getLocation() + "Dir: " + myRC.getDirection() + "Tar: " + target);
			if (!myRC.getLocation().equals(target))
				if (tracing) {
					if (canDeadReckon()) {
						tracing = false; //Stop tracing
						control.mover.setDirection(deadReckon());
						while (control.mover.isActive()) {
		                    control.mover.getRC().yield();
		                }
						control.mover.moveForward();
					}
					else {
						//control.mover.setDirection(dirToTrace()); //Pick a direction to trace the object
						dirToTrace();
						while (control.mover.isActive()) {
		                    control.mover.getRC().yield();
		                }
						control.mover.moveForward();
					}
				}
				else {
					if (canDeadReckon()) {
						control.mover.setDirection(deadReckon());
						while (control.mover.isActive()) {
		                    control.mover.getRC().yield();
		                }
						control.mover.moveForward();		
					}
					else {
						tracing = true;
						//control.mover.setDirection(dirToTrace()); //Pick a direction to trace the object
						dirToTrace();
						while (control.mover.isActive()) {
		                    control.mover.getRC().yield();
		                }
						control.mover.moveForward();
					}
				}
		}
		catch (GameActionException e) {
			System.out.println("caught exception (with Bug):");
			e.printStackTrace();
		}		
	}
	
	private boolean canDeadReckon() {
		return control.mover.canMove(deadReckon());
	}
	
	private Direction deadReckon() {
		return control.mover.getRC().getLocation().directionTo(target);
	}
	
	//Picks a direction that will allow it to trace along the object that is blocking the path
	//Right now the code just picks a random direction, which is not the same as tracing.
	//************FIX**************
	private Direction dirToTrace() {
		ArrayList<Direction> triedDirs = new ArrayList<Direction>(); //Directions that have already been tried
		if (previousBumps.contains(control.mover.getRC().getLocation())) {
			triedDirs = previousBumps.valueOfKey(control.mover.getRC().getLocation());
		}
		try {
			do {
				Direction dir = control.mover.getRC().getDirection();
				triedDirs.add(dir);			
				if (control.mover.canMove(dir)) {
					return dir;
				}
				while (motor.isActive()) {
                    myRC.yield();
                }
				control.mover.setDirection(dir.rotateRight());
			} while (true);
		}
		catch (GameActionException e) {
			System.out.println("caught exception (with Bug dirToTrace):");
			e.printStackTrace();
		}
		return Direction.NORTH;
	}

	public void setTarget(MapLocation tar) {
		target = tar;
	}
}