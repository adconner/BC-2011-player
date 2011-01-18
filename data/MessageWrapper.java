package hex.data;

import battlecode.common.MapLocation;
import battlecode.common.Message;

// every message gives at least a type code and time stamp
// the ints field is only used for validation. All info is encoded into 1 string and maplocations
public class MessageWrapper {
	// to distinguish friendly messages from other team
	// 'random' 32-bit number for validation
	public static final int[] VALIDATION = {0x364ef44d};
	
	// number of bits for message code, increase this as more codes added
	public static final int CODE_LEN = 1;
	
	public static final int ATTACK_LOCATION = 0;
	public static final int BUILD_INIT = 1;
	
	// number of bits for time stamp, in rounds. 
	public static final int TIME_STAMP_LEN = 14; // maximum 10000 rounds
	
	public static final int HEADER_LEN = CODE_LEN + TIME_STAMP_LEN;
	
	public Message m;
	
	public MessageWrapper(Message message) {
		m = message; 
	}
	
	public MessageWrapper(int code, int round) {
		
	}
	
	//gets num in [startBit, endBit]
	// assumes strings[0] exists
	public int getNum(int startBit, int endBit) {
		
		String data = m.strings[0];
		int startByte = startBit/8;
		int endByte = endBit/8;
		
		if (data.length() < endByte)
			return -1; // error
		
		int out = 0;
		for (int i = startByte; i <= endByte; i++) {
			byte b = (byte)data.charAt(i);
		}
		
	}
	
	public boolean isFriendly() {
		if (m.ints.equals(VALIDATION))
			return true;
		else return false;
	}
	
	public int getCode() {
		
	}
	
	public void validate() {
		m.ints = VALIDATION;
	}
	
	// general methods
	public MapLocation getLocation() {
		if (m.locations.length > 0)
			return m.locations[0];
		else return null;
	}

	// ATTACK_LOCATION methods
	public static final int A_VALUE_LEN = 4;
	public static final int A_HEALTH_LEN = 4;
	
	
	
}
