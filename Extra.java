package hex;

import java.util.ArrayList;

import battlecode.common.*;

public class Extra {
	
	/** Return the direction from loc to tar */
	public static Direction dirTo(MapLocation loc, MapLocation tar) {
		return loc.directionTo(tar);
	}

	public static ArrayList<MapLocation> locsNextTo(MapLocation loc) {
		ArrayList<MapLocation> locs = new ArrayList<MapLocation>();
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y<2; y++) {
				if (!(x==0 && y==0))
					locs.add(loc.add(x,y));
			}
		}
		return locs;
	}

}
