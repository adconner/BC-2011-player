package hex.state.attack;

import battlecode.common.Message;
import battlecode.common.RobotController;
import hex.data.RobotControls;
import hex.state.AbstractState;

public class attackNormalState extends AbstractState {
	
	public attackNormalState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}
	

	// check for radioed targets
	// wait for attack idle
	@Override
	public void run() {
		
		while (true) {
			// TODO reclaim unused byte-codes?
			myRC.yield();
			
			for (Message m = myRC.getNextMessage(); m != null; m = myRC.getNextMessage())
				m.
			
			if (robotComps.hasSensor())	{
				robotComps.sensor.
			}
		}
	}

	@Override
	public AbstractState getNextState() {
		// TODO Auto-generated method stub
		return null;
	}

}
