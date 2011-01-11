package hex.data;

import battlecode.common.*;
import java.util.ArrayList;

public class RobotControls {	
	public SensorController sensor; //Assuming one sensor per unit for now
	public ArrayList<WeaponController> weapons = new ArrayList<WeaponController>();
	public ArrayList<ComponentController> extra = new ArrayList<ComponentController>();
	public MovementController mover;
	public BuilderController builder;
	public BroadcastController comUnit;	
	
	public boolean hasSensor() {
		return sensor != null;
	}
}
