package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Talon;

public class SixWheelTankDrive {
	Talon driveRight1;
	Talon driveRight2;
	Talon driveRightInverted;
	Talon driveLeft1;
	Talon driveLeft2;
	Talon driveLeftInverted;  
	protected int driveMode;
	
	public SixWheelTankDrive(){
		driveRight1 = new Talon(0);
		driveRight2 = new Talon(1);
		driveRightInverted = new Talon(2);
		driveLeft1 = new Talon(3);
		driveLeft2 = new Talon(4);
		driveLeftInverted = new Talon(5);
		setDriveMode(1);
	}

	public void setDriveMode(int driveMode) {
    	this.driveMode = driveMode;
    }
    
    public void drive(double left, double rightY, double rightX) {
    	switch (driveMode) {
    		case 0:
    			bulldozerTank(left, rightY);
    			break; 
    		case 1:
    			powerTank(left, rightX);
    			break;
    	}
    }
	
	public void bulldozerTank(double left, double right) {
    
    	//invert left drive train
    	left *= -1;
    			
    	driveLeft1.set(left);
    	driveLeft2.set(left);
    	driveLeftInverted.set(left * -1);
    	driveRight1.set(right);
    	driveRight2.set(right);
    	driveRightInverted.set(right * -1);	
    }
    
    public void powerTank(double right, double left) {
    	
    	driveLeft1.set(left - right);
    	driveLeft2.set(left - right);
    	driveLeftInverted.set((left - right) * -1);
    	driveRight1.set(left + right);
    	driveRight2.set(left + right);
    	driveRightInverted.set((left + right) * -1);	
    }
}
