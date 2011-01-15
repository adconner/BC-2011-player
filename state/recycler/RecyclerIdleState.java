package hex.state.recycler;

import battlecode.common.RobotController;
import hex.data.RobotControls;
import hex.state.AbstractState;

public class RecyclerIdleState extends RecyclerAbstractState {

	public RecyclerIdleState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}

	@Override
	public void run() {
		myRC.yield();
	}


}