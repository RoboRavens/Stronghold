package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class RavenAccelerometer {
	public Accelerometer accelerometer;

	public BufferedValue xAxis;
	public BufferedValue yAxis;
	public BufferedValue zAxis;
	
	// A RavenAccelerometer can be instantiated for any kind of accelerometer.
	public RavenAccelerometer(Accelerometer accelerometer) {
		this.accelerometer = accelerometer;
		
		xAxis = new BufferedValue();
		yAxis = new BufferedValue();
		zAxis = new BufferedValue();
	}
	
	public void maintainState() {
		xAxis.maintainState(accelerometer.getX());
		yAxis.maintainState(accelerometer.getY());
		zAxis.maintainState(accelerometer.getZ());
	}
		
	public double getX() {
		return xAxis.get();
	}
	
	public double getY() {
		return yAxis.get();
	}
	
	public double getZ() {
		return zAxis.get();
	}
}
