package hex;

import java.util.ArrayList;

public class Map<T1, T2> {
	private ArrayList<T1> key;
	private ArrayList<T2> value;
	
	public Map() {
		key = new ArrayList<T1>();
		value = new ArrayList<T2>();
	}
	
	public boolean add(T1 k, T2 v) {
		return key.add(k) && value.add(v);
	}
	
	public boolean contains(T1 k) {
		return key.contains(k);
	}
	
	public T2 valueOfKey(T1 k) {
		return value.get(key.indexOf(k));
	}
	
	public void remove(T1 k) {
		int i = key.indexOf(k);
		key.remove(i);
		value.remove(i);
	}
}
