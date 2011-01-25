package team146.state.recycler;

import team146.data.BuildHelper;
import team146.data.RobotControls;
import team146.data.RobotSchematic;
import battlecode.common.*;

public class RecyclerOptionalBuildSchematic extends RecyclerBuildSchematic {

	RobotSchematic schematic;
	public RecyclerOptionalBuildSchematic(RobotController RC, RobotControls comp, RobotSchematic schematic) {
		super(RC, comp, schematic);
		this.schematic = schematic;
	}

	@Override
	public void run() {
		MovementController motor = robotComps.mover;
		BuilderController builder = robotComps.builder;
        try {
        	MapLocation unit = findUnit();
        	
			while (builder.isActive() || motor.isActive() || myRC.getTeamResources() >= 2*schematic.totalCost())
				myRC.yield();
        	
			BuildHelper.buildOptionalComponentsInLocation(builder, schematic, unit);
			
        } 
        catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}

	private MapLocation findUnit() {
		return myRC.getLocation();
	}
}