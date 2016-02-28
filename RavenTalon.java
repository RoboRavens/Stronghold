package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Talon;

public class RavenTalon {

	public Talon talon;
	protected double outputSpeed;
	
	// The default slew rate of 2 means no acceleration cutting will occur,
	// as this enables changing between -1 and 1 in a single tick.
	protected double slewRate = 2;
	
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
		this.slewRate = slewRate;
	}
	
	public void set(double targetOutput) {
		double newOutputSpeed = outputSpeed;
		
		// Increment or decrement the new output speed,
		// but never to a magnitude larger than 1.		
		if (targetOutput > outputSpeed) {
			newOutputSpeed = outputSpeed + slewRate;
			
			newOutputSpeed = Math.min(outputSpeed, 1);
		}
		
		if (targetOutput < outputSpeed) {
			newOutputSpeed = outputSpeed - slewRate;
			
			newOutputSpeed = Math.max(outputSpeed, -1);
		}
		
		// Update and set the output speed.
		outputSpeed = newOutputSpeed;
		talon.set(outputSpeed);
	}
}
