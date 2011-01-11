package hex;

import battlecode.common.*;
import java.util.ArrayList;

public class RobotControls {	
	protected SensorController sensor; //Assuming one sensor per unit for now
	protected ArrayList<WeaponController> weapons = new ArrayList<WeaponController>();
	protected ArrayList<ComponentController> extra = new ArrayList<ComponentController>();
	protected MovementController mover;
	protected BuilderController builder;
	protected BroadcastController comUnit;	
}
