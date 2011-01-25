package team146.data;

import battlecode.common.*;

public class PathNode extends Node {
	
	public PathNode(MapLocation cur) {
		super(cur);
		//prevN = null; self = cur;
	}
	public PathNode(PathNode prev, MapLocation cur) {
		super(prev, cur);
		//self = cur; prevN = prev;
	}	
	public PathNode(PathNode prev, MapLocation cur, PathNode next) {
		super(prev, cur, next);
		//self = cur; prevN = prev; nextN = next;
	}
	public PathNode(MapLocation prev, MapLocation loc) {
		super(prev, loc);
		//prevN = new PathNode(prev);
		//self = loc;
	}

	public PathNode add(MapLocation next) {
		return (PathNode)super.add(next);
	}
	
	@Override
	public MapLocation self() {
		return (MapLocation)self;
	}
	@Override
	public PathNode previous() {
		return (PathNode)prevN;
	}
	@Override
	public PathNode next() {
		return (PathNode)nextN;
	}
	
	public void setNext(MapLocation next) {
		if (next.isAdjacentTo((MapLocation)self)) 
			add(next);			
	}

	@Override
	public MapLocation goToStart() {
		return (MapLocation)super.goToStart();
	}
	@Override
	public MapLocation goToEnd() {
		return (MapLocation)super.goToEnd();
	}
	
	@Override
	public MapLocation nextTile() {
		return (MapLocation)nextN.self;
	}
	@Override
	public MapLocation prevTile() {
		return (MapLocation)prevN.self;
	}
}