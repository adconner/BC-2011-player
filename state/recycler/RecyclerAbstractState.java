package hex.state.recycler;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import hex.data.RobotControls;
import hex.data.Schematics;
import hex.state.AbstractState;

// this class exists to bring the recycler state change information to one place
public abstract class RecyclerAbstractState extends AbstractState {
	
	public static final int ROUNDS_TO_PRODUCE_BUILDERS = 200;
	public static final int ROUNDS_TO_STOP_LIGHT_PRODUCTION = 2000;
	public static final int MINIMUM_FLUX_LIGHT_PRODUCTION = 30;

	public RecyclerAbstractState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}
	
	@Override
	public RecyclerAbstractState getNextState() {
		// TODO remove debug string
    	myRC.setIndicatorString(0, String.valueOf(myRC.getTeamResources()));
    	
		// TODO modify when recyclers should be in production mode
		if (Clock.getRoundNum() < ROUNDS_TO_PRODUCE_BUILDERS)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.lightConstructor);
		else if (myRC.getTeamResources() >= MINIMUM_FLUX_LIGHT_PRODUCTION  && Clock.getRoundNum() < ROUNDS_TO_STOP_LIGHT_PRODUCTION)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.lightAttack);
		else return new RecyclerIdleState(myRC, robotComps);
	}

}
