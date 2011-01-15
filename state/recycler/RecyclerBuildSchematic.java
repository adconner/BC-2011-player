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
			
        	Direction d = myRC.getDirection(); //Reduces calls to getDirection()
			if(!motor.canMove(d)) //Determines if the tile in front is empty, otherwise, rotates
//				motor.setDirection(d.rotateRight());
				motor.setDirection(Extra.findClearDir(d, motor, -1));
			
			while (myRC.getTeamResources() < schematic.totalCost()) {
				//Do nothing, then build
			}
			BuildHelper.buildInFront(builder, schematic);
			
        } 
        catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}

}