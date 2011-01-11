package hex.navigation;

import java.util.ArrayList;
import hex.data.*;
import battlecode.common.*;

public class A_Star extends Navigator {

	public A_Star(MapLocation tar, RobotControls cont) {
		super(tar, cont);
	}

	@Override
	public void move() {
		calculate(myRC.getLocation(), target);
	}

	@SuppressWarnings("unchecked")
	private void calculate(MapLocation cur, MapLocation tar) {
		try {
			SortedMap<MapLocation, Double> open = new SortedMap<MapLocation, Double>();
			ArrayList<Path> closed = new ArrayList<Path>();
			open.add(cur, scoreTile(cur, tar));
			//Figure out a way to record path and score of path
			while (!open.isEmpty()) {
				MapLocation current = open.firstKey();
				if (current.equals(tar))
					break;
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
			}
		} catch (GameActionException e) {
			System.out.println("Problem with A_Star calculate: ");
			e.printStackTrace();
		}
	}
	
	//Calculates score based on distance to target
	//Uses Manhattan distance = (goal.x - s.x) + (goal.y - s.y)
	//May want to use Math.sqrt(s.distanceSquaredTo(goal)) at some point, that's why return type is double
	public double scoreTile(MapLocation cur, MapLocation tar) {
		return Math.abs(tar.x-cur.x) + Math.abs(tar.y-cur.y);
	}
}
