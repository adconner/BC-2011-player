package hex.data;

import java.util.ArrayList;

import battlecode.common.MapLocation;

public class Path {
	private ArrayList<PathNode> path;
	
	public Path(PathNode start) {
		path = new ArrayList<PathNode>();
		path.add(start);
	}
	
	public void add(PathNode next) {
		if (path.get(path.size()-1).tile().equals(next.previous()))
			path.add(next);
	}
	public void add(MapLocation next) {
		if (next.isAdjacentTo(end()))
			path.add(path.get(path.size()-1).addToPath(next));			
	}
	
	public MapLocation start() {
		return path.get(0).tile();
	}

	public PathNode firstNode() {
		return path.get(0);
	}
	public PathNode lastNode() {
		return path.get(path.size()-1);
	}
	//Tells if the MapLocation can be found anywhere in the path
	public boolean contains(MapLocation loc) {
		PathNode p = path.get(0);
		while (!p.isEnd()) {
			if (p.tile().equals(loc))
				return true;
			p = p.next();
		}
		//Hasn't check the last node yet
		return p.tile().equals(loc);
	}
		
	public MapLocation end() {
		return path.get(path.size()-1).tile();
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
		for (Path path : paths) {
			if (path.contains(loc))
				return true;
		}
		return false;
	}
}