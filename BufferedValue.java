package org.usfirst.frc.team1188.robot;

import java.util.LinkedList;

public class BufferedValue {
	protected int listSize = 9;
	
	LinkedList<Double> values;
	
	public BufferedValue() {
		values = new LinkedList<Double>();
	}
	
	// Adds the current value to the list, and
	// removes the first item if the list is larger than the list size.
	public void maintainState(double currentValue) {
		values.add(currentValue);
		
		if (values.size() > listSize) {
			values.remove();
		}
	}
	
	public double get() {
		double cumulativeValue = 0;
		
		for (Double value : values) {
			cumulativeValue += value;
		}
				
		return cumulativeValue / values.size();
	}

}
