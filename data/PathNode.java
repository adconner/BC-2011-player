package hex.data;

import battlecode.common.*;

public class PathNode {
	private MapLocation curTile;
	private PathNode prevPN, nextPN;
	
	public PathNode(PathNode prev, MapLocation cur) {
		curTile = cur; prevPN = prev;
	}
	
	public PathNode(PathNode prev, MapLocation cur, PathNode next) {
		curTile = cur; prevPN = prev; nextPN = next;
	}
	
	public PathNode addToPath(MapLocation next) {
		PathNode p = new PathNode(this, next);
		nextPN = p;
		return p;
	}
	
	public MapLocation tile() {
		return curTile;
	}
	public PathNode previous() {
		return prevPN;
	}
	public PathNode next() {
		return nextPN;
	}
	
	public void setNext(MapLocation next) {
		if (next.isAdjacentTo(curTile)) 
			addToPath(next);			
	}
	
	public boolean isRoot() {
		return prevPN == null;
	}
	public boolean isEnd() {
		return nextPN == null;
	}

	public MapLocation goToStart() {
		PathNode p = this;
		while (!p.isRoot())
			p = prevPN;
		return p.curTile;
	}
	public MapLocation goToEnd() {
		PathNode p = this;
		while (!p.isEnd())
			p = nextPN;
		return p.curTile;
	}
}