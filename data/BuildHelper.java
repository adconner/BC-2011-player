package hex.data;

import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotLevel;

public class BuildHelper {
	
	// only the components listed as required are built
	public static void buildInFront(BuilderController builder, RobotSchematic robot) throws GameActionException {
		
		RobotController myRC = builder.getRC();
		
		// wait for flux
		while (builder.getRC().getTeamResources()<robot.baseCost()) 
			myRC.yield();
		
		// collect build information
		RobotLevel level = (robot.chassis == Chassis.FLYING?RobotLevel.IN_AIR:RobotLevel.ON_GROUND);
		MapLocation loc = myRC.getLocation().add(myRC.getDirection());
		
		// build chassis
		builder.build(robot.chassis, loc);
		while (builder.isActive()) myRC.yield();
		
		// build components
		for (ComponentType c: robot.components) {
			builder.build(c, loc, level);
			while (builder.isActive()) myRC.yield();
		}
	}
	
	public static void buildOptionalComponentsInFront(BuilderController builder, RobotSchematic robot) throws GameActionException {
		
		RobotController myRC = builder.getRC();
		
		// wait for flux
		while (builder.getRC().getTeamResources()<robot.optionalCost()) 
			myRC.yield();
		
		// collect build information
		RobotLevel level = (robot.chassis == Chassis.FLYING?RobotLevel.IN_AIR:RobotLevel.ON_GROUND);
		MapLocation loc = myRC.getLocation().add(myRC.getDirection());
		
		// NO CHASSIS BUILT, this is an optional UPGRADE
		
		// build components
		for (ComponentType c: robot.optionalComps) {
			builder.build(c, loc, level);
			while (builder.isActive()) myRC.yield();
		}
	}
	
	private BuildHelper() {} // class cannot be instantiated
}