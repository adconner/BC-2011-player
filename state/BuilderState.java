package hex.state;

import hex.data.RobotControls;
import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;
import battlecode.common.MovementController;
import battlecode.common.RobotController;
import battlecode.common.RobotLevel;

public class BuilderState extends AbstractState {

	public BuilderState(RobotController RC, RobotControls comp) {
		super(RC, comp);
	}

	@Override
	public void run() {
		MovementController motor = robotComps.mover;
		BuilderController builder = robotComps.builder;
		while (true) {
            try {
				
            	myRC.setIndicatorString(0, String.valueOf(myRC.getTeamResources()));
				
				if(!motor.canMove(myRC.getDirection())) //Determines if the tile in front is empty, otherwise, rotates
					motor.setDirection(myRC.getDirection().rotateRight());
				/*if (myRC.getTeamResources()>=Chassis.FLYING.cost + ComponentType.SIGHT.cost + ComponentType.CONSTRUCTOR.cost) {
					builder.build(Chassis.FLYING, myRC.getLocation().add(myRC.getDirection()));
					builder.build(ComponentType.SIGHT, myRC.getLocation().add(myRC.getDirection()), RobotLevel.IN_AIR);
					builder.build(ComponentType.CONSTRUCTOR, myRC.getLocation().add(myRC.getDirection()), RobotLevel.IN_AIR);
				}*/
				if (myRC.getTeamResources()>=Chassis.LIGHT.cost + ComponentType.CONSTRUCTOR.cost + Chassis.BUILDING.cost + ComponentType.RECYCLER.cost) {
					builder.build(Chassis.LIGHT, myRC.getLocation().add(myRC.getDirection()));
					while (builder.isActive()) myRC.yield();
					builder.build(ComponentType.CONSTRUCTOR, myRC.getLocation().add(myRC.getDirection()), RobotLevel.IN_AIR);
					builder.build(ComponentType.SIGHT, myRC.getLocation().add(myRC.getDirection()), RobotLevel.IN_AIR);
				}
				else if(myRC.getTeamResources()>=2*Chassis.LIGHT.cost + Chassis.BUILDING.cost + ComponentType.RECYCLER.cost) {
					builder.build(Chassis.LIGHT,myRC.getLocation().add(myRC.getDirection()));
					while (builder.isActive()) myRC.yield();
					builder.build(ComponentType.HAMMER, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
					builder.build(ComponentType.ANTENNA, myRC.getLocation().add(myRC.getDirection()), RobotLevel.ON_GROUND);
				}
				
            } 
            catch (Exception e) {
                System.out.println("caught exception:");
                e.printStackTrace();
            }
        }
	}

	@Override
	public AbstractState getNextState() {
		return null;
	}

}
