package team146.data;

import java.util.ArrayList;

import battlecode.common.MapLocation;

public class Path extends NodeStream {
	
	public Path(PathNode start) {
		super(start);
	}
	
	public void add(PathNode next) {
		if (stream.get(stream.size()-1).self().equals(next.previous()))
			stream.add(next);
	}
	public void add(MapLocation next) {
		if (next.isAdjacentTo(end()))
			stream.add(stream.get(stream.size()-1).add(next));			
	}
	
	public MapLocation start() {
		return (MapLocation)super.start();
	}
	public MapLocation end() {
		return (MapLocation)super.end();
	}

	@Override
	public PathNode firstNode() {
		return (PathNode)super.firstNode();
	}
	@Override
	public PathNode lastNode() {
		return (PathNode)super.lastNode();
	}

	//Tells if the MapLocation can be found anywhere in the path
	public boolean contains(MapLocation loc) {
		return super.contains(loc);
	}
		
	

	//Will edit actual paths AL
	public static Path addToALOfPaths(MapLocation prev, MapLocation loc, ArrayList<Path> paths) {
		for (Path path : paths) {
			if (path.end().equals(prev)) {
				path.add(loc);
				return path;
			}
		}
		Path p = new Path(new PathNode(prev, loc));
		paths.add(p);
		return p;
	}

	public static boolean containsInALOfPaths(MapLocation loc, ArrayList<Path> paths) {
		for (Path path : paths)
			if (path.contains(loc))
				return true;
		return false;
	}
}