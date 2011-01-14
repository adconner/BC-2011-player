package hex.data;

import com.sun.xml.internal.bind.v2.runtime.Location;

import battlecode.common.BuilderController;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotLevel;

public class BuildHelper {
	
	// components are built in the order added to RobotScematic
	public static void buildInFront(BuilderController builder, RobotSchematic robot) throws GameActionException {
		
		RobotController myRC = builder.getRC();
		
		// wait for flux
		while (builder.getRC().getTeamResources()<robot.cost()) 
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
	
	private BuildHelper() {} // class cannot be instantiated
}
