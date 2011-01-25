package team146.data;

import java.util.ArrayList;

public class Map<T1, T2> {
	protected ArrayList<T1> key;
	protected ArrayList<T2> value;
	
	public Map() {
		key = new ArrayList<T1>();
		value = new ArrayList<T2>();
	}
	
	protected ArrayList<T1> keys() {
		return key;
	}
	protected ArrayList<T2> values() {
		return value;
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
	
	public T2 getValue(int i) {
		return value.get(i);
	}
	public T1 getKey(int i) {
		return key.get(i);
	}
	public T1 getKeyFromValue(T2 v) {
		return key.get(value.indexOf(v));
	}
	
	public void changeValue(T1 theKey, T2 newValue) {
		value.set(key.indexOf(theKey), newValue);
	}
	
	public void remove(T1 k) {
		int i = key.indexOf(k);
		key.remove(i);
		value.remove(i);
	}
	
	public void remove(int i) {
		key.remove(i);
		value.remove(i);
	}
	
	public boolean isEmpty() {
		return key.size()==0;
	}

	public int size() {
		return key.size();
	}
}