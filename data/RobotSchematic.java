package hex.data;

import battlecode.common.Chassis;
import battlecode.common.ComponentType;

import hex.state.AbstractState;
import hex.state.MineScoutState;
import hex.state.NavigationState;
import hex.state.recycler.RecyclerInitialState;

import java.util.ArrayList;

// the order of the components indicates in which order they should be built
public class RobotSchematic {
	
	public Chassis chassis;
	public ArrayList<ComponentType> components = new ArrayList<ComponentType>();
	
	// generally, components built at another building
	public ArrayList<ComponentType> optionalComps = new ArrayList<ComponentType>();
	
	public Class initialState;
	
	private double baseCost;
	private boolean baseCostUpToDate = false;
	
	private double optionalCost;
	private boolean optionalCostUpToDate = false;
	
	public RobotSchematic() {}
	public RobotSchematic(Chassis c) {
		chassis = c;
	}
	
	public void setChassis(Chassis c) {
		chassis = c;
	}
	
	public void addComponent(ComponentType comp) {
		components.add(comp);
		baseCostUpToDate = false;
		optionalCostUpToDate = false;
	}
	
	public void addOptionalComp(ComponentType comp) {
		optionalComps.add(comp);
		optionalCostUpToDate = false;
	}
	
	public AbstractState getNewInitialState() {
		try {
			return (AbstractState) initialState.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public double baseCost() {
		if (!baseCostUpToDate) {
			double cost = chassis.cost;
			for (ComponentType c: components)
				cost += c.cost;
			baseCost = cost;
			baseCostUpToDate = true;
		}
		return baseCost;
	}
	
	public double optionalCost() {
		if (!optionalCostUpToDate) {
			double cost = 0;
			for (ComponentType c:optionalComps)
				cost += c.cost;
			optionalCost = cost;
			optionalCostUpToDate = true;
		}
		return optionalCost;
	}
	
	public double totalCost() {
		return baseCost() + optionalCost();
	}
	
	public boolean valid() {
		int weight = 0;
		for (ComponentType c: components)
			weight += c.weight;
		return weight <= chassis.weight;
	}
}
