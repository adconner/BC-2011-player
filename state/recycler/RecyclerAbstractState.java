package hex.state.recycler;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import hex.data.RobotControls;
import hex.data.Schematics;
import hex.data.Tunable;
import hex.state.AbstractState;

// this class exists to bring the recycler state change information to one place
public abstract class RecyclerAbstractState extends AbstractState {

	public RecyclerAbstractState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}
	
	@Override
	public RecyclerAbstractState getNextState() {
		// TODO remove debug string
    	myRC.setIndicatorString(0, String.valueOf(myRC.getTeamResources()));
    	
		// TODO modify when recyclers should be in production mode
		if (Clock.getRoundNum() < Tunable.RECYCLER_ROUNDS_TO_PRODUCE_BUILDERS)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.LIGHT_CONSTRUCTOR.s);
		else if (myRC.getTeamResources() >= Tunable.RECYCLER_MINIMUM_FLUX_LIGHT_PRODUCTION  && Clock.getRoundNum() < Tunable.RECYCLER_ROUNDS_TO_STOP_LIGHT_PRODUCTION)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.LIGHT_ATTACK.s);
		else return new RecyclerIdleState(myRC, robotComps);
	}

}