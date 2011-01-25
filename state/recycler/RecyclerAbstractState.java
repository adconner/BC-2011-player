package team146.state.recycler;

import battlecode.common.Clock;
import battlecode.common.RobotController;
import team146.data.Extra;
import team146.data.RobotControls;
import team146.data.Schematics;
import team146.data.Tunable;
import team146.state.AbstractState;

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
		if (Clock.getRoundNum() > 200 && Clock.getRoundNum() < Tunable.RECYCLER_ROUNDS_TO_PRODUCE_BUILDERS)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.LIGHT_CONSTRUCTOR.s);
//    	if (!Extra.chassisFull(myRC.getChassis(), myRC.components()))
//    		return new RecyclerOptionalBuildSchematic(myRC, robotComps, Schematics.RECYCLER.s);
		else if (myRC.getTeamResources() >= Tunable.RECYCLER_MINIMUM_FLUX_LIGHT_PRODUCTION  && Clock.getRoundNum() > Tunable.RECYCLER_ROUNDS_TO_PRODUCE_BUILDERS && Clock.getRoundNum() < Tunable.RECYCLER_ROUNDS_TO_STOP_LIGHT_PRODUCTION)
			return new RecyclerBuildSchematic(myRC, robotComps, Schematics.LIGHT_ATTACK.s);
		else 
			return new RecyclerIdleState(myRC, robotComps);
	}

}