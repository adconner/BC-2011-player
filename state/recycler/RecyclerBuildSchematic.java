package hex.state.recycler;

import hex.data.BuildHelper;
import hex.data.Extra;
import hex.data.RobotControls;
import hex.data.RobotSchematic;
import battlecode.common.*;

public class RecyclerBuildSchematic extends RecyclerAbstractState {

	RobotSchematic schematic;
	public RecyclerBuildSchematic(RobotController RC, RobotControls comp, RobotSchematic schematic) {
		super(RC, comp);
		this.schematic = schematic;
	}

	@Override
	public void run() {
		MovementController motor = robotComps.mover;
		BuilderController builder = robotComps.builder;
        try {
			while (builder.isActive() || motor.isActive() || myRC.getTeamResources() <= 2*schematic.totalCost())
				myRC.yield();
        	
			BuildHelper.buildInLocation(builder, schematic, myRC.getLocation().add(Extra.findClearDir(myRC.getDirection(), motor)));
			
        } 
        catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}

}