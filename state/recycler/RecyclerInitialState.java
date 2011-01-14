package hex.state.recycler;

import hex.data.RobotControls;
import battlecode.common.RobotController;

public class RecyclerInitialState extends RecyclerAbstractState {

	public RecyclerInitialState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}

	@Override
	public void run() {
		// nothing
	}

}
