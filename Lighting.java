package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Relay.Value;

public class Lighting {
	Relay mainArray;
	Timer timer;
	boolean toggling;
	
	public Lighting () {
		mainArray = new Relay(7);
		timer = new Timer();
		toggling = false;
	}
	
	public void turnOn() {
		cancelToggle();
		mainArray.set(Value.kOn);
	}
	
	public void turnOff() {
		cancelToggle();
		mainArray.set(Value.kOff);
	}
	
	public void quickToggle() {
		// If not already toggling, initialize a toggle sequence.
		// If already toggling, no need to do anything.
		if (toggling == false) {
			timer.reset();
			timer.start();
			toggling = true;
		}
	}
	
	public void cancelToggle() {
		toggling = false;
		timer.stop();
		timer.reset();
	}
	
	public void maintainState() {
		// If not toggling, this method does nothing.
		if (toggling == false) {
			return;
		}
		
		double timerMs = timer.get() * 1000;
		
		if (timerMs > Calibrations.lightingFlashTotalDurationMs) {
			turnOff();
		}
		
		Value lightsValue;
		
		// If we haven't returned yet, we're toggling.
		// The duration of each on/off cycle is the total flash duration,
		// divided by the number of flashes. An individual on/off is half of that.
		double cycleDuration = Calibrations.lightingFlashTotalDurationMs / Calibrations.lightingFlashes;
		
		// Modding the timer by the cycle duration gives us the time elapsed in the current cycle.
		double msElapsedInCurrentCycle = timerMs % cycleDuration;
		
		// The lights should be on for the first half of each cycle, and off for the second half.
		if (msElapsedInCurrentCycle * 2 < cycleDuration) {
			lightsValue = Value.kOn;
		}
		else {
			lightsValue = Value.kOff;
		}
		
		mainArray.set(lightsValue);
	}
}
