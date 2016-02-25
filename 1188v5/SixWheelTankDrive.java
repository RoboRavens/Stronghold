package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class SixWheelTankDrive {
	Talon driveRight1;
	Talon driveRight2;
	Talon driveRightInverted;
	Talon driveLeft1;
	Talon driveLeft2;
	Talon driveLeftInverted;  
	Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;
    AnalogGyro  robotGyro;
	protected int driveMode;
	protected int gyroMode;
	protected int limitedPower;
	protected double gyroAdjust;
	protected double orientation;
	
	public SixWheelTankDrive(){
		driveRight1 = new Talon(0);
		driveRight2 = new Talon(1);
		driveRightInverted = new Talon(2);
		driveLeft1 = new Talon(3);
		driveLeft2 = new Talon(4);
		driveLeftInverted = new Talon(5);
		robotGyro = new AnalogGyro(0);
		leftDriveEncoder = new Encoder(0, 1);
		rightDriveEncoder = new Encoder(2, 3);
		setDriveMode(1);
		setLimitedPower(0);
		setGyroMode(0);
		gyroAdjust = 0;
		orientation = 0;
		
		resetDriveGyro();
	}

	public void setDriveMode(int driveMode) {
    	this.driveMode = driveMode;
    }
	
	public void setLimitedPower(int powerMode) {
    	this.limitedPower = powerMode;
    }
	
	public void setGyroMode(int gyroMode) {
    	this.gyroMode = gyroMode;
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
    	if (limitedPower == 1){
    		right *= 0.3;
    		left *= 0.5;
    	}
    	
    	driveLeft1.set(left);
    	driveLeft2.set(left);
    	driveLeftInverted.set(left * -1);
    	driveRight1.set(right);
    	driveRight2.set(right);
    	driveRightInverted.set(right * -1);	
    }
    
    public void powerTank(double right, double left) {
    	
    	if (limitedPower == 1){
    		right *= 0.3;
    		left *= 0.5;
    	}
    	
        gyroAdjust = gyroAdjustment(right); 
    	
    	driveLeft1.set(left - right + gyroAdjust);
    	driveLeft2.set(left - right + gyroAdjust);
    	driveLeftInverted.set((left - right + gyroAdjust) * -1);
    	driveRight1.set(left + right - gyroAdjust);
    	driveRight2.set(left + right - gyroAdjust);
    	driveRightInverted.set((left + right - gyroAdjust) * -1);	
    }
    
	public int getRightDriveEncoder(){
		System.out.println(rightDriveEncoder.get());
		return rightDriveEncoder.get();
	}
	
	public int getLeftDriveEncoder(){
		System.out.println(leftDriveEncoder.get());
		return leftDriveEncoder.get();
	}    


    public double getDriveGyro() {
    	System.out.println(robotGyro.getAngle());
    	return robotGyro.getAngle();
    }
    
    public void resetDriveGyro() {
    	robotGyro.reset();
    }


    public double gyroAdjustment(double right){
    	double turnConstant = 0.1;
    	double kp = 0.03;             // tune to robot
    	
    	//check to see if the driver is turning 
    	if(right > turnConstant || right < -turnConstant){
    		resetDriveGyro();
    	}
    	gyroAdjust = robotGyro.getAngle() * kp;
    	switch(gyroMode){
    		case 0:
    			return 0;
    		case 1:
    			return gyroAdjust;
    		default:
    			return 0;
    	}
    }

}

