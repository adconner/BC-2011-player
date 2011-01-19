package hex.data;

import battlecode.common.*;
import java.util.ArrayList;

public class RobotControls {	
	public SensorController sensor; //Assuming one sensor per unit for now
	public ArrayList<WeaponController> weapons = new ArrayList<WeaponController>();
	public ArrayList<ComponentController> extra = new ArrayList<ComponentController>(); //The only ones left are BUG and DROPSHIP should I just make variables (single or ArrayList) for them?
	public MovementController mover;
	public BuilderController builder;
	public BroadcastController comUnit;
	public ArrayList<JumpController> jumpers = new ArrayList<JumpController>();
	
	public void addJump(JumpController j) {
		if (j!=null && !jumpers.contains(j)) jumpers.add(j);
	}
	
	public boolean hasSensor() {
		return sensor != null;
	}
	public boolean hasJump() {
		return jumpers.size()>0;
	}
}