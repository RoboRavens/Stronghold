package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class SixWheelTankDrive {
	RavenTalon driveRight1;
	RavenTalon driveRight2;
	RavenTalon driveRightInverted;
	RavenTalon driveLeft1;
	RavenTalon driveLeft2;
	RavenTalon driveLeftInverted;  
	
	Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;
    
    AnalogGyro orientationGyro;
    Timer gyroCooldownTimer;
    
    protected int driveMode;
	protected int gyroMode;
	protected int limitedPower;
	protected double gyroZero;
	protected double orientation;
	protected double slewRate;
	
	public SixWheelTankDrive(){
		// I believe the driver station cycles at 50hz, so a rate of .02
		// means it takes one second to go from output voltage of 1 to 0.
		// Setting to .01 now for more obvious visual detection in testing.
		slewRate = .01;
		
		driveRight1 = new RavenTalon(0, slewRate);
		driveRight2 = new RavenTalon(1, slewRate);
		driveRightInverted = new RavenTalon(2, slewRate);
		driveLeft1 = new RavenTalon(3, slewRate);
		driveLeft2 = new RavenTalon(4, slewRate);
		driveLeftInverted = new RavenTalon(5, slewRate);
		
		orientationGyro = new AnalogGyro(0);
		gyroCooldownTimer = new Timer();
		
		leftDriveEncoder = new Encoder(0, 1);
		rightDriveEncoder = new Encoder(2, 3);
		setDriveMode(1);
		setLimitedPower(0);
		setGyroMode(0);
		gyroZero = setGyroZero();
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
	
	public double deadzone(double input) {
		double output = input;
		
		if (Math.abs(output) < .1) {
			output = 0;
		}
		
		return output;
	}
    
    public void drive(double left, double rightY, double rightX) {
    	left = deadzone(left);
    	rightY = deadzone(rightY);
    	rightX = deadzone(rightX);
    	
    	// double turn = Math.abs(left - right);
    	
    	switch (driveMode) {
    		case 0:
    			bulldozerTank(left, rightY);
    			break; 
    		case 1:
    			fpsTank(left, rightX);
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
    
    public void fpsTank(double turn, double movement) {
    	
    	if (limitedPower == 1){
    		turn *= 0.3;
    		movement *= 0.5;
    	}
    	
    	// double turn = Math.abs(movement - right);
    	
        double gyroAdjust = getTurnableGyroAdjustment(turn); 
    	
    	//gyroAdjust = gyroAdjustment(right); 
    	
    	
    	driveLeft1.set(movement - turn + gyroAdjust);
    	driveLeft2.set(movement - turn + gyroAdjust);
    	driveLeftInverted.set((movement - turn + gyroAdjust) * -1);
    	driveRight1.set(movement + turn + gyroAdjust);
    	driveRight2.set(movement + turn + gyroAdjust);
    	driveRightInverted.set((movement + turn + gyroAdjust) * -1);	
    }
    
    public void driveOutput() {
    	
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
    	System.out.println("Gyro angle: " + Math.round(orientationGyro.getAngle()) + " Gyro mode: " + gyroMode);
    	return orientationGyro.getAngle();
    }
    
    public void resetDriveGyro() {
    	orientationGyro.reset();
    }
    
    public double setGyroZero(){
//    	return orientationGyro.getAngle();
    	
    	this.gyroZero = orientationGyro.getAngle();
    	
    	return gyroZero;
    }

    private boolean adjustGyroDueToTimer() {
    	double time = this.gyroCooldownTimer.get();
    	
    	boolean adjust = false;
    	
    	if (time > 0 && time < 1.0) {
    		adjust = true;
    	}
    	else if (time > 1.0) {
    		gyroCooldownTimer.stop();
    	}
    	
    	return adjust;
    }

    public double getTurnableGyroAdjustment(double turn){
    	// If the gyro mode is zero, just return immediately.
    	if (gyroMode == 0) {
    		return 0;
    	}
    	
    	if (Math.abs(turn) > 0 || this.adjustGyroDueToTimer()) {
        	this.setGyroZero();
        	
        	if (Math.abs(turn) > 0) {
	        	this.gyroCooldownTimer.reset();
	        	this.gyroCooldownTimer.start();
        	}
        }
    	
    	return getStaticGyroAdjustment();
    }
    
    public double getStaticGyroAdjustment() {
    	// If the gyro mode is zero, just return immediately.
    	if (gyroMode == 0) {
    		return 0;
    	}
    	
    	double adjustmentScaleFactor = 0.03;             // tune to robot
    	
    	// Mod to eliminate extra rotations.
    	double gyroAdjust = (Math.round(orientationGyro.getAngle()) - gyroZero) % 360;
    	
    	// This snippet ensures that the robot will spin in the fastest direction to zero
    	// if it ends up more than 180 degrees off of intention.
    	if (gyroAdjust < -180){
    		gyroAdjust = gyroAdjust + 360;
    	}
    	if (gyroAdjust > 180){
    		gyroAdjust = gyroAdjust - 360;
    	}
    	
    	// Mod again in case the directional snippet was applied.
    	gyroAdjust = Math.round(gyroAdjust) % 360;
    	
    	
    	gyroAdjust *= adjustmentScaleFactor;
    	
		return gyroAdjust;
    }

}

