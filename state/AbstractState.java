package hex.state;

import battlecode.common.RobotController;
import hex.data.RobotControls;

public abstract class AbstractState implements Runnable {
	
	protected RobotController myRC; 
	protected RobotControls robotComps;
	
	public AbstractState(RobotController RC, RobotControls comp) {
		myRC = RC;
		robotComps = comp;
	}

	// abstract methods
	public abstract void run();
	public abstract AbstractState getNextState();
	
}
