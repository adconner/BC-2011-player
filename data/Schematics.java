package hex.data;

import battlecode.common.Chassis;
import battlecode.common.ComponentType;

public class Schematics {
	// the order of components listed is the order of component construction
	
	
	// can be built at the RECYCLER
	public static RobotSchematic lightAttack = new RobotSchematic(Chassis.LIGHT);
	static {
		lightAttack.addComponent(ComponentType.HAMMER);
		lightAttack.addComponent(ComponentType.SIGHT);
		lightAttack.addComponent(ComponentType.ANTENNA);
		lightAttack.addComponent(ComponentType.SHIELD);
		lightAttack.addComponent(ComponentType.SHIELD);
	}
	public static RobotSchematic lightConstructor = new RobotSchematic(Chassis.LIGHT);
	static {
		lightConstructor.addComponent(ComponentType.CONSTRUCTOR);
		lightConstructor.addComponent(ComponentType.SIGHT);
	}

}
