package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Talon;

public class RavenTalon {

	public Talon talon;
	protected double outputSpeed;
	
	// The default slew rate of 2 means no acceleration cutting will occur,
	// as this enables changing between -1 and 1 in a single tick.
	protected double maxSlewRate = 2;
	
	protected double deadband = .05;
	
	public RavenTalon(int channel) {
		talon = new Talon(channel);
	}
	
	public RavenTalon(int channel, double slewRate) {
		talon = new Talon(channel);
		setSlewRate(slewRate);
	}
	
	// For now, the slew rate is defined in "magnitude of change to
	// motor output, on a -1 to 1 scale, per 'control system tick'" (50hz.)
	// Protip: this number should be greater than zero, but likely not by much.
	// If it's zero the motor will never change output.
	public void setSlewRate(double slewRate) {
		this.maxSlewRate = slewRate;
	}
	
	public void set(double targetOutput) {
		double newOutputSpeed = outputSpeed;
		
		// Never change the speed by more than the difference between target and actual,
		// regardless of what the slew rate is.
		double slewRate = Math.min(maxSlewRate, Math.abs(targetOutput - outputSpeed));
		
		// Increment or decrement the new output speed,
		// but never to a magnitude larger than 1.		
		if (targetOutput > outputSpeed) {
			newOutputSpeed = outputSpeed + slewRate;
			
			newOutputSpeed = Math.min(newOutputSpeed, 1);
		}
		
		if (targetOutput < outputSpeed) {
			newOutputSpeed = outputSpeed - slewRate;
			
			newOutputSpeed = Math.max(newOutputSpeed, -1);
		}
		
		
		if (Math.abs(targetOutput) < this.deadband && Math.abs(outputSpeed) < this.maxSlewRate) {
			newOutputSpeed = 0;
		}
		
		
		// Update and set the output speed.
		outputSpeed = newOutputSpeed;
		
		//System.out.println("Target: " + targetOutput + " Actual: " + outputSpeed + " Slew: " + maxSlewRate);
		
		talon.set(outputSpeed);
	}
}
