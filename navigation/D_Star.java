package team146.navigation;

import java.util.ArrayList;

import team146.data.Extra;
import team146.data.Map;
import team146.data.RobotControls;
import team146.data.SortedMap;
import battlecode.common.*;

/** for D_Star, the current location is taken to be the target, starting from the actual target */
public class D_Star extends Navigator {
	
	/**D_Star DOES NOT work
	 * problem with the way calculate works*/
	private Map<MapLocation, Double> scores; //Contains scores of all squares that have been calculated
	private Map<MapLocation, Direction> path; //Contains order of MapLocations to be traversed and the Direction the robot should face AFTER getting into the tile
	private double additional; //To help when recalculating
	
	public D_Star(MapLocation tar, RobotControls cont) {
		super(tar, cont);
		additional = 1.0;
		scores = new Map<MapLocation, Double>();
		path = createPath(myRC.getLocation(), calculate(myRC.getLocation(), target));
	}
	
	@Override
	public void move() {
		Direction turn;
		try {			
			if (path.contains(myRC.getLocation())) 
				trimPath(myRC.getLocation());
			else 
				path = createPath(myRC.getLocation(), calculate(myRC.getLocation(), target));
			turn = path.getValue(0);
			
			
			if (turn == Direction.OMNI || turn == Direction.NONE)
				super.move();
			
			if (!motor.canMove(turn)) 
				path = createPath(myRC.getLocation(), reCalculate(myRC.getLocation(), target));
			turn = path.getValue(0);
			//else if (motor.canMove(turn)) {
				motor.setDirection(turn);
				while (motor.isActive()) 
					myRC.yield();
				motor.moveForward();
		//	}
		} catch (GameActionException e) {
			System.out.println("Error with A_Star move: ");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Direction> reCalculate(MapLocation loc, MapLocation tar) {
		scores.changeValue(path.getKey(1), 1000. + additional);
		
		SortedMap<MapLocation, Double> open = new SortedMap<MapLocation, Double>();
		//Keeps track of directions to get to each location
		Map<MapLocation, ArrayList<Direction>> closed = new Map<MapLocation, ArrayList<Direction>>();
		open.add(tar, 0.0);
		try {
			ArrayList<Direction> newDirs = reCalculate(tar, loc, open, closed);
			additional++;
			return redoPathDir(loc, newDirs);
		} catch (GameActionException e) {
			System.out.println("Problem with A_Star calculate: ");
			e.printStackTrace();
		}
		additional++;
		return closed.getValue(0);
	}
	
	private ArrayList<Direction> redoPathDir(MapLocation loc, ArrayList<Direction> newDirs) {
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		for (Direction d: newDirs) {
			dirs.add(d);
			loc = loc.add(d);
		}
		
		if (path.contains(loc)) {
			trimPath(loc); //path is going to be changed anyway, so no need to preserve old version
			for (int x = 0; x < path.size(); x++) {
				dirs.add(path.getValue(x));
			}
		}
		
		return dirs;
	}
	

	@SuppressWarnings("unchecked")
	private ArrayList<Direction> reCalculate(MapLocation prev, MapLocation tar, SortedMap<MapLocation, Double> open, Map<MapLocation, ArrayList<Direction>> closed) throws GameActionException {
		if (!open.isEmpty()) {
			MapLocation current = open.firstKey();
			
			if (!open.valueOfKey(current).equals(1000 + additional)) {
				if (current.equals(tar)) {
					ArrayList<Direction> d = closed.valueOfKey(prev);
					d.add(Extra.dirTo(tar, current)); //dirTo is backwards because the calculated path is going backwards
					return d;
				}
				else if (path.contains(current) && Extra.senseIfClear(control, current)) {
					ArrayList<Direction> d = closed.valueOfKey(prev);
					d.add(Extra.dirTo(tar, current)); //dirTo is backwards because the calculated path is going backwards
					return d;
				}
				else {
					//Adding to closed
					ArrayList<Direction> dirs;
					if (closed.contains(prev))
						dirs = Extra.copyDirAL(closed.valueOfKey(prev));
					else
						dirs = new ArrayList<Direction>();
					Direction d = Extra.dirTo(current, prev); //dirTo is backwards because the calculated path is going backwards
					if (d != Direction.OMNI && d != Direction.NONE)
						dirs.add(d); 
					closed.add(current, dirs);
					//////////////////////////////////////////////
					
					//Check adjacent squares
					ArrayList<MapLocation> adj = Extra.locsNextTo(current);
					for (MapLocation loc: adj) {
						//Not sure how to tell if a terrain is traversable or not yet. Will fix later. 
						//Until then, will assume all is traversable
						if (!open.contains(loc) && !closed.contains(loc)) {
							if (Extra.senseIfClear(control, loc))
								open.addPair(loc, dirs.size() + scoreTile(loc, tar));
							else //There's something in that tile, so we can't move there
								scores.add(loc, 1000 + additional);
						}
					}
					return calculate(current, tar, open, closed);	
				}
			}
			scores.add(current, open.firstValue()/2);
			open.removeFirst();
		}
		return closed.valueOfKey(prev);
	}

	/**
	 * Removes all of the entries in path before upTo
	 * @param upTo
	 */
	private void trimPath(MapLocation upTo) {
		while (!path.getKey(0).equals(upTo))
			path.remove(0);
	}

	private Map<MapLocation, Direction> createPath(MapLocation cur, ArrayList<Direction> dirs) {
		Map<MapLocation, Direction> route = new Map<MapLocation, Direction>();
		Direction prev;
		MapLocation tile = cur;
		for (int x = dirs.size()-1; x>=0; x--) {
			prev = dirs.get(x);
			if (prev != Direction.NONE && prev != Direction.OMNI) {
				route.add(tile, dirs.get(x));
				tile = tile.add(prev);
			}
		}
		return route;
	}
	
	//***********************************************8
	//Returned AL must be traversed starting from the end and heading to 0 to get the directions from the starting location (not the original target)
	@SuppressWarnings("unchecked")
	private ArrayList<Direction> calculate(MapLocation cur, MapLocation tar) {
		SortedMap<MapLocation, Double> open = new SortedMap<MapLocation, Double>();
		//Keeps track of directions to get to each location
		Map<MapLocation, ArrayList<Direction>> closed = new Map<MapLocation, ArrayList<Direction>>();
		open.add(tar, 0);
		try {
			return calculate(tar, cur, open, closed);
		} catch (GameActionException e) {
			System.out.println("Problem with A_Star calculate: ");
			e.printStackTrace();
		}
		return closed.getValue(0);
	}
	
	/**
	 * Supposed to be a recursive method is called by former calculate
	 * @param prev
	 * @param tar
	 * @param open
	 * @param closed
	 * @return
	 * @throws GameActionException
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Direction> calculate(MapLocation prev, MapLocation tar, SortedMap<MapLocation, Double> open, Map<MapLocation, ArrayList<Direction>> closed) throws GameActionException {
		if (!open.isEmpty()) {
			MapLocation current = open.firstKey();
			scores.add(current, open.firstValue());
			open.removeFirst();
			if (current.equals(tar)) {
				ArrayList<Direction> d = closed.valueOfKey(prev);
				d.add(Extra.dirTo(tar, current)); //dirTo is backwards because the calculated path is going backwards
				open = new SortedMap<MapLocation, Double>();
				System.out.println(d);
				return d;
			}
			else {
				//Adding to closed
				ArrayList<Direction> dirs;
				if (closed.contains(prev))
					dirs = Extra.copyDirAL(closed.valueOfKey(prev));
				else
					dirs = new ArrayList<Direction>();
				Direction d = Extra.dirTo(current, prev); //dirTo is backwards because the calculated path is going backwards
				if (d != Direction.OMNI && d != Direction.NONE)
					dirs.add(d); 
				closed.add(current, dirs);
				//////////////////////////////////////////////
				
				//Check adjacent squares
				ArrayList<MapLocation> adj = Extra.locsNextTo(current);
				System.out.println(adj);
				for (MapLocation loc: adj) {
					//Not sure how to tell if a terrain is traversable or not yet. Will fix later. 
					//Until then, will assume all is traversable
//					if (!open.contains(loc)) {
					if (!open.contains(loc) && !closed.contains(loc)) {
						if (Extra.senseIfClear(control, loc))
							open.addPair(loc, dirs.size() + scoreTile(loc, tar));
						else //There's something in that tile, so we can't move there
							scores.add(loc, 1000.0);
					}
				}
				return calculate(current, tar, open, closed);	
			}
		}
		System.out.println(closed.valueOfKey(prev));
		return closed.valueOfKey(prev);
	}
}