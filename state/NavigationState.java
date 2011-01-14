package hex.state;

import hex.data.Extra;
import hex.data.RobotControls;
import hex.navigation.A_Star;
import hex.navigation.Bug;
import hex.navigation.Navigator;

import java.util.ArrayList;
import java.util.Random;

import battlecode.common.BroadcastController;
import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentController;
import battlecode.common.ComponentType;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameObject;
import battlecode.common.MapLocation;
import battlecode.common.Mine;
import battlecode.common.MovementController;
import battlecode.common.RobotController;
import battlecode.common.RobotLevel;
import battlecode.common.SensorController;
import battlecode.common.WeaponController;

public class NavigationState extends AbstractState {
	
	private Navigator navi; //Perhaps have an array of Navigators, instead of just one, because of D_Star
	
	
	public NavigationState(RobotController RC, RobotControls comp, Navigator n) {
		super(RC, comp);
		navi = n;
	}
	
	@Override
	public void run() {
    	MovementController motor = robotComps.mover;
    	while (true) {
            try {
                /*** beginning of main loop ***/
                while (motor.isActive()) {
                    myRC.yield();
                }
               
                navi.blindMove();
                
//                navi.move();
                
                /*if (motor.canMove(myRC.getDirection())) {
                    //System.out.println("about to move");
                    navi.move();
                }
                else {
                	ComponentController [] comps = myRC.newComponents();
                	if (comps.length>0) {
                		robotComps.weapons.add((WeaponController)comps[0]);
                		if (comps.length>1)
                			robotComps.sensors.add((SensorController)comps[1]);
                	}
                	if (robotComps.sensors.size()>0) {
                		GameObject[] obj = robotComps.sensors.get(0).senseNearbyGameObjects(Robot.class);
                		for (GameObject ob : obj) {
                			myRC.yield();
	                		if (ob instanceof Robot && !ob.getTeam().equals(myRC.getTeam()))
	                			robotComps.weapons.get(0).attackSquare(myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
	                		}
                	}
                	robotComps.mover.setDirection(myRC.getDirection().rotateRight());
                }*/

                /*** end of main loop ***/
            } catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
        }
    }    

    
    @Override
    public AbstractState getNextState() {
    	return this;
    }
}
