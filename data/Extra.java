package hex.data;

import java.util.ArrayList;
import battlecode.common.*;

public class Extra {
	
	/** Return the direction from loc to tar */
	public static Direction dirTo(MapLocation loc, MapLocation tar) {
		return loc.directionTo(tar);
	}

	public static ArrayList<MapLocation> locsNextTo(MapLocation loc) {
		ArrayList<MapLocation> locs = new ArrayList<MapLocation>();
		/*for (int x = -1; x < 2; x++) {
			for (int y = -1; y<2; y++) {
				if ((x==0 && y!=0) || (x!=0 && y==0)) //forgotten how to do this properly
					locs.add(loc.add(x,y));
				else
					locs.add(loc.add(x,y));
			}
		}*/
		Direction d = Direction.NORTH;
		for (int a = 0; a < 8; a++) {
			locs.add(loc.add(d));
			d = d.rotateLeft();
		}
		return locs;
	}

	public static boolean canMove(MovementController motor, MapLocation loc, MapLocation tar) {
		return motor.canMove(dirTo(loc, tar));
	}

	//Returns a copy of an ArrayList
	//at least for integers. not sure how to do it for generic object
	public static ArrayList<Integer> copyIntAL(ArrayList<Integer> orig) {
		ArrayList<Integer> clone = new ArrayList<Integer>();
		for (int or : orig) {
			clone.add(or);
		}
		return clone;
	}
	
	/**Returns a copy of an ArrayList of Directions. Not sure how to do it for generic object
	 * @param orig -- the AL to be copied
	 * @return
	 */
	public static ArrayList<Direction> copyDirAL(ArrayList<Direction> orig) {
		ArrayList<Direction> clone = new ArrayList<Direction>();
		for (Direction or : orig) {
			clone.add(or);
		}
		return clone;
	}
	
	public static boolean senseIfClear(RobotControls control, MapLocation loc) throws GameActionException {
		boolean a= control.hasSensor() && control.sensor.canSenseSquare(loc) && control.sensor.senseObjectAtLocation(loc, RobotLevel.ON_GROUND) == null;
		boolean b = !control.hasSensor() || !control.sensor.canSenseSquare(loc);
		return a || b;
	}

	/**
	 * rotates direction degrees number of degrees, in direction of unit circle (counter-clockwise)
	 * best to use only if degrees > 45
	 * @param from
	 * @param degrees
	 * @return
	 */
	public static Direction rotate(Direction from, int degrees) {
		degrees%=360; degrees/=45; //Figures out how many times to turn
		if (degrees > 0) {
			while (degrees != 0) {
				from = from.rotateLeft();
				degrees--;
			}				
		}
		else {
			while (degrees != 0) {
				from = from.rotateRight();
				degrees++;
			}
		}
		return from;
	}

	/**
	 * returns positive degree value between first and second
	 * @param first
	 * @param second
	 * @return
	 */
	public static int degreesBetween(Direction first, Direction second) {
		int turns = 0;
		while (first != second) {
			first = first.rotateLeft();
			turns++;
		}
		return turns*45;
	}
	
	/** Consumes a max of 240 bytecodes
	 * @param current
	 * @param mot
	 * @return
	 */
	public static Direction findClearDir(Direction current, MovementController mot) {
		while (!mot.canMove(current))
			current = current.rotateLeft();
		return current;
	}
	
	/** Consumes a max of 240 bytecodes
	 * @param current
	 * @param mot
	 * @param direct --either 1 for rotateLeft() or -1 for rotateRight()
	 * @return
	 */
	public static Direction findClearDir(Direction current, MovementController mot, int direct) {
		while (!mot.canMove(current))
			if (direct > 0)
				current = rotate(current, 45);
			else if (direct < 0)
				current = rotate(current, -45);
			else break; 
		return findClearDir(current, mot);
	}
	
	public static boolean chassisFull(Chassis chas, ComponentController[] comps) {
		int weight = 0;
		for (ComponentController c: comps) 
			weight += c.type().weight;
		return weight==chas.weight;
	}
}