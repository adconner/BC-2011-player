package hex.data;

import battlecode.common.Chassis;
import battlecode.common.ComponentType;

import java.util.ArrayList;

// the order of the components indicates in which order they should be built
public enum RobotSchematic {
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
		RobotSchematic.RECYCLER.addComponent(ComponentType.RECYCLER);
		RobotSchematic.RECYCLER.addOptionalComp(ComponentType.ANTENNA); // example
		RobotSchematic.RECYCLER.totalCost(); // to cache this number, this may be a bad idea if these calculations are done for every robot
	}
	static {
		RobotSchematic.FACTORY.addComponent(ComponentType.FACTORY);
		RobotSchematic.FACTORY.totalCost();
	}
	static {
		RobotSchematic.ARMORY.addComponent(ComponentType.ARMORY);
		RobotSchematic.ARMORY.totalCost();
	}
	
	// built by the RECYCLER
	static {
		RobotSchematic.LIGHT_ATTACK.addComponent(ComponentType.HAMMER);
		RobotSchematic.LIGHT_ATTACK.addComponent(ComponentType.SIGHT);
		RobotSchematic.LIGHT_ATTACK.addComponent(ComponentType.ANTENNA);
		RobotSchematic.LIGHT_ATTACK.addComponent(ComponentType.SHIELD);
		RobotSchematic.LIGHT_ATTACK.addComponent(ComponentType.SHIELD);
		RobotSchematic.LIGHT_ATTACK.totalCost();
	}
	static {
		RobotSchematic.LIGHT_CONSTRUCTOR.addComponent(ComponentType.CONSTRUCTOR);
		RobotSchematic.LIGHT_CONSTRUCTOR.addComponent(ComponentType.SIGHT);
		RobotSchematic.LIGHT_CONSTRUCTOR.totalCost();
	}
	
	
	
	
	// -------- Class Methods --------
	public Chassis chassis;
	public ArrayList<ComponentType> components = new ArrayList<ComponentType>();
	
	// generally, components built at another building
	public ArrayList<ComponentType> optionalComps = new ArrayList<ComponentType>();
	
	double baseCost;
	boolean baseCostUpToDate = false;
	
	double optionalCost;
	boolean optionalCostUpToDate = false;
	
	RobotSchematic(Chassis c) {
		chassis = c;
	}
	
	void addComponent(ComponentType comp) {
		components.add(comp);
		baseCostUpToDate = false;
		optionalCostUpToDate = false;
	}
	
	void addOptionalComp(ComponentType comp) {
		optionalComps.add(comp);
		optionalCostUpToDate = false;
	}
	
	double baseCost() {
		if (!baseCostUpToDate) {
			double cost = chassis.cost;
			for (ComponentType c: components)
				cost += c.cost;
			baseCost = cost;
			baseCostUpToDate = true;
		}
		return baseCost;
	}
	
	double optionalCost() {
		if (!optionalCostUpToDate) {
			double cost = 0;
			for (ComponentType c:optionalComps)
				cost += c.cost;
			optionalCost = cost;
			optionalCostUpToDate = true;
		}
		return optionalCost;
	}
	
	double totalCost() {
		return baseCost() + optionalCost();
	}
	
	boolean valid() {
		int weight = 0;
		for (ComponentType c: components)
			weight += c.weight;
		return weight <= chassis.weight;
	}
}
