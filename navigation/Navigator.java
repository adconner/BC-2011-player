package hex.navigation;

import hex.data.RobotControls;
import battlecode.common.*;

public abstract class Navigator {
	protected MapLocation target;
	protected RobotControls control; //Is this necessary? Since we have motor and myRC
	protected MovementController motor;
	protected RobotController myRC;
	
	public Navigator(MapLocation tar, RobotControls cont) {
		target = tar;
		control = cont;
		motor = control.mover;
		myRC = control.mover.getRC();
	}
	
	public abstract void move();
	
	public void setTarget(MapLocation tar) {
		target = tar;
	}
}
