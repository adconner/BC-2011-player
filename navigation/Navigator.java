package hex.navigation;

import hex.data.RobotControls;
import battlecode.common.*;

public class Navigator {
	protected MapLocation target;
	protected RobotControls control; //Is this necessary? Since we have motor and myRC
	protected MovementController motor;
	protected RobotController myRC;
	
	//********Ignore
	private int bob = 10;
	private void changeBob() {
		if (bob%2 ==0) bob++;
		else bob/=2;
	}
	 
	
	public Navigator(MapLocation tar, RobotControls cont) {
		target = tar;
		control = cont;
		motor = control.mover;
		myRC = control.mover.getRC();
	}
	
	public void blindMove() {
		try {
            /*** beginning of main loop ***/
            while (motor.isActive()) {
                myRC.yield();
            }

            if (motor.canMove(myRC.getDirection())) {
                //System.out.println("about to move");
                motor.moveForward();
            } else {
            	if(bob%2==0) 
            		while(!motor.canMove(myRC.getDirection())) {
            			motor.setDirection(myRC.getDirection().rotateRight());
            			while (motor.isActive()) myRC.yield();
            		}
            	else
            		while(!motor.canMove(myRC.getDirection())) {
            			motor.setDirection(myRC.getDirection().rotateLeft());
            			while (motor.isActive()) myRC.yield();
            		}
            	changeBob();
            }

            /*** end of main loop ***/
        } catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}
	
	public void move() {
		blindMove();
	}
	
	public void setTarget(MapLocation tar) {
		target = tar;
	}
	
	//Calculates score based on distance to target
	//Uses Manhattan distance = (goal.x - s.x) + (goal.y - s.y)
	//May want to use Math.sqrt(s.distanceSquaredTo(goal)) at some point, that's why return type is double
	public double scoreTile(MapLocation cur, MapLocation tar) {
		return Math.abs(tar.x-cur.x) + Math.abs(tar.y-cur.y);
	}
}
