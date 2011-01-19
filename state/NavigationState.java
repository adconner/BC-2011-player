package hex.state;

import hex.data.Extra;
import hex.data.RobotControls;
import hex.navigation.*;

import java.util.ArrayList;

import battlecode.common.*;

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
