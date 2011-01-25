package team146.data;

import java.util.ArrayList;

/**This assumes we will only have numerical values
 * T2 is assumed to be Double or Integer */
@SuppressWarnings({ "rawtypes" })
public class SortedMap<T1, T2> extends Map {
	
	public SortedMap() {
		super();
	}
	
	//Was getting an error so I just changed the name
	@SuppressWarnings("unchecked")
	public void addPair(T1 k, T2 v) { 
		super.add(k, v);
		//Create mapping of Double to original positions 
		Map<Integer, Double> origPos = new Map<Integer, Double>();
		for (int x = 0; x<value.size(); x++) {
			origPos.add(x, (Double)value.get(x));
		}
		//Sort values
		Double[] sorted = new Double[value.size()]; 
		for (int x=0; x<sorted.length; x++) {
			sorted[x] = (Double)value.get(x);
		}
		sort(sorted);
		
		//Create new mapping from sorted
		ArrayList<T1> newKey = new ArrayList<T1>(); 
		ArrayList<Double> newDouble = new ArrayList<Double>();
		for (Double d:sorted) {
			newKey.add((T1)getKeyFromValue(d));
			newDouble.add(d);
		}
		
		key = newKey;
		value = newDouble;
		//Well that was easy
	}
	
	@SuppressWarnings("unchecked")
	public T1 firstKey() {
		return (T1)key.get(0);
	}
	@SuppressWarnings("unchecked")
	public T2 firstValue() {
		return (T2)value.get(0);
	}
	
	public void removeFirst() {
		super.remove(0);
	}

	//I forgot MergeSort implementation. Feel free to change later
	private void sort(Double[] arr) {
		for (int i = 1; i < arr.length; i++){
	      int j = i;
	      Double B = arr[i];
	      while ((j > 0) && (arr[j-1] > B)){
	        arr[j] = arr[j-1];
	        j--;
	      }
	      arr[j] = B;
	    }
	}
}