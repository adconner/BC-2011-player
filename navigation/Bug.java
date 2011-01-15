package hex.navigation;

import hex.data.Extra;
import hex.data.Map;
import hex.data.RobotControls;

import java.util.ArrayList;
import battlecode.common.*;

/**Bug movement, dead reckoning to target, and tracing wall if needed */
public class Bug extends Navigator {
	private boolean tracing;
	private char sideOfWall; //is the side of the unit that the wall is on. 'L' for left, 'R' for right, or 'C' for clear (no wall). sideOfWall will be used when tracing
	private Map<MapLocation, ArrayList<Direction>> previousBumps;
	
	
	public Bug(MapLocation tar, RobotControls cont) {
		super(tar, cont);
		tracing = false;
		sideOfWall = 'C';
		previousBumps = new Map<MapLocation, ArrayList<Direction>>();
	}
	
	@Override
	public void move() {
		try {
			curLoc = myRC.getLocation();
			curDir = myRC.getDirection();
			if (!curLoc.equals(target)) {
				if (tracing) {
					if (canDeadReckon()) {
						tracing = false; //Stop tracing
						motor.setDirection(deadReckon());
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();
					}
					else if (!motor.canMove(curDir)) { //It hit another wall -> it's in a corner
						hitWall();
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
						motor.setDirection(dirToTrace()); //Pick a direction to trace the object
//						dirToTrace();
						while (motor.isActive()) {
		                    myRC.yield();
		                }
						motor.moveForward();
					}
				}
			}
		}
		catch (GameActionException e) {
			System.out.println("caught exception (with Bug):");
			e.printStackTrace();
		}		
	}
	
	//Only used if tracing
	private void hitWall() throws GameActionException {
		if (sideOfWall == 'L') {
			motor.setDirection(Extra.findClearDir(curDir, motor, -1));
			return;
		}
		if (sideOfWall == 'R') {
			motor.setDirection(Extra.findClearDir(curDir, motor, 1));
		}
	}

	//Need to check if we're at the very end of the wall
	private boolean canDeadReckon() throws GameActionException {
		if (control.hasSensor() && sideOfWall != 'C') {
			if (sideOfWall=='L') 
				return control.sensor.senseObjectAtLocation(curLoc.add(Extra.rotate(curDir, 90)), RobotLevel.ON_GROUND) == null;
			if (sideOfWall=='R') 
				return control.sensor.senseObjectAtLocation(curLoc.add(Extra.rotate(curDir, -90)), RobotLevel.ON_GROUND) == null;
		}
		Direction d = deadReckon();
		return motor.canMove(d) && d != curDir.opposite();
	}
	
	private Direction deadReckon() {
		return curLoc.directionTo(target);
	}
	
	//Picks a direction that will allow it to trace along the object that is blocking the path
	//Right now the code just picks a random direction, which is not the same as tracing.
	//************FIX**************
	private Direction dirToTrace() {
		ArrayList<Direction> triedDirs; //Directions that have already been tried
		if (previousBumps.contains(curLoc))
			triedDirs = previousBumps.valueOfKey(curLoc);
		else
			triedDirs = new ArrayList<Direction>();
		
		Direction dir = curDir.rotateRight();
		int rotation = -1; //rotating right
		boolean tried = false; //Keeps track of whether we've turned and hit that direction already
		while (true) {
			dir = Extra.findClearDir(curDir, motor, rotation);
			if (!triedDirs.contains(dir)) {	
				triedDirs.add(dir);
				if (!previousBumps.contains(curLoc))
					previousBumps.add(curLoc, triedDirs);
				else 
					previousBumps.changeValue(curLoc, triedDirs);
				int degRot = Extra.degreesBetween(curDir, dir);
				if (degRot < 180) sideOfWall = 'R';
				else if (degRot > 180) sideOfWall = 'L';
				System.out.print(sideOfWall);
				return dir;
			}
			else if (!tried) {
				rotation *=-1;
				tried = true;
			}
			triedDirs.add(dir);
			dir = Extra.rotate(dir, rotation);
		} 
	}
}