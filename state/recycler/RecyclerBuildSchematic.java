package hex.state.recycler;

import hex.data.BuildHelper;
import hex.data.RobotControls;
import hex.data.RobotSchematic;
import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;
import battlecode.common.MovementController;
import battlecode.common.RobotController;
import battlecode.common.RobotLevel;

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
			
			if(!motor.canMove(myRC.getDirection())) //Determines if the tile in front is empty, otherwise, rotates
				motor.setDirection(myRC.getDirection().rotateRight());
			
			BuildHelper.buildInFront(builder, schematic);
			
        } 
        catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}

}
