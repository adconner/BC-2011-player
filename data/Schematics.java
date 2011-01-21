package hex.data;

import hex.state.*;
import battlecode.common.Chassis;
import battlecode.common.ComponentType;

public enum Schematics {
	// built by the CONSTRUCTOR
	RECYCLER(Chassis.BUILDING),
	FACTORY(Chassis.BUILDING),
	ARMORY(Chassis.BUILDING),
	
	// built by the RECYCLER
	LIGHT_ATTACK(Chassis.LIGHT),
	LIGHT_CONSTRUCTOR(Chassis.LIGHT)
	// ...
	;
	
	// built by the CONSTRUCTOR
	static {
		Schematics.RECYCLER.s.addComponent(ComponentType.RECYCLER);
		Schematics.RECYCLER.s.addOptionalComp(ComponentType.ANTENNA); // example
		Schematics.RECYCLER.s.totalCost(); // to cache this number, this may be a bad idea if these calculations are done for every robot
	}
	static {
		Schematics.FACTORY.s.addComponent(ComponentType.FACTORY);
		Schematics.FACTORY.s.totalCost();
	}
	static {
		Schematics.ARMORY.s.addComponent(ComponentType.ARMORY);
		Schematics.ARMORY.s.totalCost();
	}
	
	// built by the RECYCLER
	static {
		Schematics.LIGHT_ATTACK.s.addComponent(ComponentType.HAMMER);
		Schematics.LIGHT_ATTACK.s.addComponent(ComponentType.SIGHT);
		Schematics.LIGHT_ATTACK.s.addComponent(ComponentType.ANTENNA);
		Schematics.LIGHT_ATTACK.s.addComponent(ComponentType.SHIELD);
		Schematics.LIGHT_ATTACK.s.addComponent(ComponentType.SHIELD);
		Schematics.LIGHT_ATTACK.s.totalCost();
	}
	static {
		Schematics.LIGHT_CONSTRUCTOR.s.addComponent(ComponentType.CONSTRUCTOR);
		Schematics.LIGHT_CONSTRUCTOR.s.addComponent(ComponentType.SIGHT);
		Schematics.LIGHT_CONSTRUCTOR.s.totalCost();
	}
	
	public RobotSchematic s;
	
	
	private Schematics(Chassis c) {
		s = new RobotSchematic(c);
	}
	
	
}