package hex.data;

import battlecode.common.Chassis;
import battlecode.common.ComponentType;

import java.util.ArrayList;

// the order of the components indicates in which order they should be built
public class RobotSchematic {
	public Chassis chassis;
	public ArrayList<ComponentType> components = new ArrayList<ComponentType>();
	
	RobotSchematic(Chassis c) {
		chassis = c;
	}
	
	void addComponent(ComponentType comp) {
		components.add(comp);
	}
	
	double cost() {
		double cost = chassis.cost;
		for (ComponentType c: components)
			cost += c.cost;
		return cost;
	}
	
	boolean valid() {
		int weight = 0;
		for (ComponentType c: components)
			weight += c.weight;
		return weight <= chassis.weight;
	}
}
