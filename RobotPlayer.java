package hex;


import hex.data.*;
import hex.navigation.*;
import hex.state.*;

import java.util.ArrayList;
import java.util.Random;

import battlecode.common.*;

public class RobotPlayer implements Runnable {
	
	
	// role member
	// stack member

	private final RobotController myRC;
	private RobotControls robotComps = new RobotControls();
	
	public RobotPlayer(RobotController rc) {
        myRC = rc;
    }
	
	@Override
	public void run() {
		checkForNewComponents();
		
		AbstractState current = new NavigationState(myRC, robotComps);
			// TODO: add code to change to appropriate initial state, perhaps dependant on chassis and components
		
		for (;;current.run(), current = current.getNextState())
			checkForNewComponents(); // updates component list every state change

	}
	
	//Tests how many ByteCodes a function or piece of code uses
	public void testBytecodeUsage() {
		while (true) {
			myRC.yield();
				//Place method toXtest here
			System.out.println(Clock.getBytecodeNum());
		}
	}
	
	private void checkForNewComponents() {
		ComponentController [] components = myRC.newComponents();
		
		for (ComponentController c: components) {
			switch (c.componentClass()) {
				case ARMOR:
				case MISC:
					robotComps.extra.add(c);
					break;
				case BUILDER:
					robotComps.builder = (BuilderController)c;
					break;
				case COMM:
					robotComps.comUnit = (BroadcastController)c;
				case MOTOR:
					robotComps.mover = (MovementController)c;
					break;
				case SENSOR:
					robotComps.sensor = (SensorController)c;
					break;
				case WEAPON:
					robotComps.weapons.add((WeaponController)c);
					break;
			}
		}
		
	}

}