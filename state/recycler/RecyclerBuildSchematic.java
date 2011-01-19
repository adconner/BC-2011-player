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
			while (builder.isActive())
				myRC.yield();
        	
			boolean can = motor.canMove(myRC.getDirection());
        	if (myRC.getTeamResources() >= schematic.totalCost() && !builder.isActive() && (!can || !motor.isActive())) {
        	
				if(!can) //Determines if the tile in front is empty, otherwise, rotates
					motor.setDirection(Extra.findClearDir(myRC.getDirection().rotateRight(), motor, -1));
				
				BuildHelper.buildInFront(builder, schematic);
<<<<<<< HEAD
        	}		
=======
        	}
			
>>>>>>> 87102257428f1aaf823cbc765609aa528390b19a
        } 
        catch (Exception e) {
            System.out.println("caught exception:");
            e.printStackTrace();
        }
	}

}