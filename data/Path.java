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
		
	public MapLocation end() {
		return path.get(path.size()-1).tile();
	}
}