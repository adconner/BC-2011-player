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
	
	public void move() {
		try {
            /*** beginning of main loop ***/
            while (motor.isActive()) {
                myRC.yield();
            }

            if (motor.canMove(myRC.getDirection())) {
                //System.out.println("about to move");
                motor.moveForward();
            } else {
                motor.setDirection(myRC.getDirection().rotateRight());
            }

            /*** end of main loop ***/
        } catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}
	
	public void setTarget(MapLocation tar) {
		target = tar;
	}
}
