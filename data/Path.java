package hex.data;

import java.util.ArrayList;

import battlecode.common.MapLocation;

public class Path {
	private ArrayList<PathNode> path;
	
	public Path(PathNode start) {
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
	
	public MapLocation getStart() {
		return path.get(0).tile();
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
	public static Path addToALOfPaths(MapLocation loc, ArrayList<Path> paths) {
		return null;
	}
}