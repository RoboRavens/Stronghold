package org.usfirst.frc.team1188.robot;

import java.util.LinkedList;
import edu.wpi.first.wpilibj.DigitalInput;

public class BufferedDigitalInput {
	public DigitalInput digitalInput;
	protected int listSize = 9;
	
	LinkedList<Boolean> sensorValues;
	
	public BufferedDigitalInput(int channel) {
		digitalInput = new DigitalInput(channel);
		sensorValues = new LinkedList<Boolean>();
	}
	
	// Adds the current sensor value to the list, and
	// removes the first item if the list is larger than the list size.
	public void maintainState() {
		sensorValues.add(digitalInput.get());
		if (sensorValues.size() > listSize) {
			sensorValues.remove();
		}
	}
	
	public boolean get() {
		int trues = 0;
		
		// Count the instances of "true" in the list values.
		for (Boolean value : sensorValues) {
			if (value) {
				trues++;
			}
		}
		
		// If trues is greater than half the list size, return true. Otherwise, false.
		return (trues * 2 > listSize);
	}

}
