package team146.state.recycler;

import battlecode.common.RobotController;
import team146.data.RobotControls;
import team146.state.AbstractState;

public class RecyclerIdleState extends RecyclerAbstractState {

	public RecyclerIdleState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}

	@Override
	public void run() {
		myRC.yield();
	}

}