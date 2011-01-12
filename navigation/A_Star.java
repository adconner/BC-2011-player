package hex.navigation;

import java.util.ArrayList;
import hex.data.*;
import battlecode.common.*;

public class A_Star extends Navigator {
	Path setPath;
	
	public A_Star(MapLocation tar, RobotControls cont) {
		super(tar, cont);
	}

	@Override
	public void move() {
		super.move(); //Until function is finished
		/*try {			
			calculate(myRC.getLocation(), target);
			System.out.println(Extra.canMove(motor, myRC.getLocation(), setPath.firstNode().nextTile()));
			if (Extra.canMove(motor, myRC.getLocation(), setPath.firstNode().nextTile())) {
				motor.setDirection(Extra.dirTo(myRC.getLocation(), setPath.firstNode().nextTile()));
				while (motor.isActive()) 
					myRC.yield();
				motor.moveForward();
			}
		} catch (GameActionException e) {
			System.out.println("Error with A_Star move: ");
			e.printStackTrace();
		}*/
	}

	@SuppressWarnings("unchecked")
	private void calculate(MapLocation cur, MapLocation tar) {
		try {
			SortedMap<MapLocation, Double> open = new SortedMap<MapLocation, Double>();
			ArrayList<Path> closed = new ArrayList<Path>();
			open.add(cur, scoreTile(cur, tar));
			//
				calculate(cur, tar, open, closed);
			//
			//Figure out a way to record path and score of path
			/*while (!open.isEmpty()) {
				MapLocation current = open.firstKey();
				if (current.equals(tar)) {
					setPath = Path.addToALOfPaths(current, closed);
					break;
				}
				closed.add(current);
				open.removeFirst();
				ArrayList<MapLocation> adj = Extra.locsNextTo(current);
				for (MapLocation loc: adj) {
					//Not sure how to tell if a terrain is traversable or not yet. Will fix later. 
					//Until then, will assume all is traversable
					if (!open.contains(loc) && !closed.contains(loc)) {
						if (control.hasSensor() && control.sensor.canSenseSquare(loc) && control.sensor.senseObjectAtLocation(loc, RobotLevel.ON_GROUND) == null)
							open.addPair(loc, scoreTile(loc, tar));
						else
							open.addPair(loc, scoreTile(loc, tar));
					}
				}
			}*/
		} catch (GameActionException e) {
			System.out.println("Problem with A_Star calculate: ");
			e.printStackTrace();
		}
	}
	
	/*************************************************
	 * instead of keeping track of MapLocations, just keep track of Direction
	 * will save space and might be easier to code
	 * will mean overhaul of current implementation though
	 * even though this way isn't working anyway
	 */
	
	//Supposed to be a recursive method is called by former calculate
	@SuppressWarnings("unchecked")
	private void calculate(MapLocation prev, MapLocation tar, SortedMap<MapLocation, Double> open, ArrayList<Path> closed) throws GameActionException {
		if (!open.isEmpty()) {
			System.out.println(closed);
			MapLocation current = open.firstKey();
			if (current.equals(tar)) {
				setPath = Path.addToALOfPaths(prev, current, closed);
				open = new SortedMap<MapLocation, Double>(); //to break out of recursion
			}
			else {
				Path.addToALOfPaths(prev, current, closed);
				open.removeFirst();
				ArrayList<MapLocation> adj = Extra.locsNextTo(current);
				for (MapLocation loc: adj) {
					//Not sure how to tell if a terrain is traversable or not yet. Will fix later. 
					//Until then, will assume all is traversable
					if (!open.contains(loc) && !Path.containsInALOfPaths(loc, closed)) {
						if (control.hasSensor() && control.sensor.canSenseSquare(loc) && control.sensor.senseObjectAtLocation(loc, RobotLevel.ON_GROUND) == null)
							open.addPair(loc, scoreTile(loc, tar));
						else if (!control.hasSensor() || !control.sensor.canSenseSquare(loc))
							open.addPair(loc, scoreTile(loc, tar));
					}
				}
				calculate(current, tar, open, closed);	
			}
		}
	}
	
	//Calculates score based on distance to target
	//Uses Manhattan distance = (goal.x - s.x) + (goal.y - s.y)
	//May want to use Math.sqrt(s.distanceSquaredTo(goal)) at some point, that's why return type is double
	public double scoreTile(MapLocation cur, MapLocation tar) {
		return Math.abs(tar.x-cur.x) + Math.abs(tar.y-cur.y);
	}
}
