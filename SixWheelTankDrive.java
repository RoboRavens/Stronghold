package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class SixWheelTankDrive {
	
	RavenTalon driveRight1;
	RavenTalon driveRight2;
	RavenTalon driveRightInverted;
	RavenTalon driveLeft1;
	RavenTalon driveLeft2;
	RavenTalon driveLeftInverted;
	
	/*
	Talon driveRight1;
	Talon driveRight2;
	Talon driveRightInverted;
	Talon driveLeft1;
	Talon driveLeft2;
	Talon driveLeftInverted;
	*/
	
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
	
	Joystick calibrationStick = new Joystick(3);
	
	public SixWheelTankDrive(){
		// I believe the driver station cycles at 50hz, so a rate of .02
		// means it takes one second to go from output voltage of 1 to 0.
		// Setting to .01 now for more obvious visual detection in testing.
		slewRate = Calibrations.slewRate;
		
		driveRight1 = new RavenTalon(0, slewRate);
		driveRight2 = new RavenTalon(1, slewRate);
		driveRightInverted = new RavenTalon(2, slewRate);
		driveLeft1 = new RavenTalon(3, slewRate);
		driveLeft2 = new RavenTalon(4, slewRate);
		driveLeftInverted = new RavenTalon(5, slewRate);
		
		
		
		/*
		driveRight1 = new Talon(0);
		driveRight2 = new Talon(1);
		driveRightInverted = new Talon(2);
		driveLeft1 = new Talon(3);
		driveLeft2 = new Talon(4);
		driveLeftInverted = new Talon(5);
		*/
		
		orientationGyro = new AnalogGyro(1);
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
	
	public void setSlewRate(double slewRate) {
		
		slewRate = Calibrations.slewRate;
		driveRight1.setSlewRate(slewRate);
		driveRight2.setSlewRate(slewRate);
		driveRightInverted.setSlewRate(slewRate);
		driveLeft1.setSlewRate(slewRate);
		driveLeft2.setSlewRate(slewRate);
		driveLeftInverted.setSlewRate(slewRate);
	}
    
    public void drive(double left, double rightY, double rightX) {
    	left = deadzone(left);
    	rightY = deadzone(rightY);
    	rightX = deadzone(rightX);
    	
    	this.setSlewRate(Math.abs(this.calibrationStick.getZ() * 2));
    	
    	// double turn = Math.abs(left - right);
    	
    	//System.out.println("Left: " + left + " right: " + rightX);
    	
    	
    	
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
    		left *= Calibrations.cutPowerModeMovementRatio;
    		right *= Calibrations.cutPowerModeTurnRatio;
    	}
    	
    	driveLeft1.set(left);
    	driveLeft2.set(left);
    	driveLeftInverted.set(left * -1);
    	driveRight1.set(right);
    	driveRight2.set(right);
    	driveRightInverted.set(right * -1);	
    }
    
    public void fpsTank(double movement, double turn) {
    	
    	if (limitedPower == 1){
    		movement *= Calibrations.cutPowerModeMovementRatio;
    		turn *= Calibrations.cutPowerModeTurnRatio;
    	}
    	
    	// double turn = Math.abs(movement - right);
    	
        double gyroAdjust = getTurnableGyroAdjustment(turn); 
    	
    	//gyroAdjust = gyroAdjustment(right); 
    	
       // System.out.println("Gyro adjust: " + gyroAdjust + " gyro: " + this.orientationGyro.getAngle());
        
        //gyroAdjust = 0;
        
        driveLeft1.set((movement - turn) * - 1 - gyroAdjust);
    	driveLeft2.set((movement - turn)  * -1 - gyroAdjust);
    	driveLeftInverted.set((movement - turn) + gyroAdjust);
    	driveRight1.set((movement + turn) - gyroAdjust);
    	driveRight2.set((movement + turn) - gyroAdjust);
    	driveRightInverted.set((movement + turn) * -1 + gyroAdjust);
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
    
    public double setGyroZero(double angle){
    	this.gyroZero += angle;
    	return gyroZero;
    }

    private boolean adjustGyroDueToTimer() {
    	double time = this.gyroCooldownTimer.get();
    	
    	boolean adjust = false;
    	
    	if (time > 0 && time < Calibrations.gyroCooldownTimerTime) {
    		adjust = true;
    	}
    	else if (time > Calibrations.gyroCooldownTimerTime) {
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
    	
    	double adjustmentScaleFactor = Calibrations.gyroAdjustmentScaleFactor;     // tune to robot
    	
    	// Mod to eliminate extra rotations.
    	double gyroAdjust = (Math.round(orientationGyro.getAngle()) - gyroZero) % 360;
    	
    	// This snippet ensures that the robot will spin in the fastest direction to zero
    	// if it ends up more than 180 degrees off of intention.
    	if (gyroAdjust < -180){
    		gyroAdjust = gyroAdjust - 360;
    	}
    	if (gyroAdjust > 180 || gyroAdjust < -180){
    		gyroAdjust *= -1;
    	}
    	
    	// Mod again in case the directional snippet was applied.
    	gyroAdjust = Math.round(gyroAdjust) % 360;
    	
    	
    	gyroAdjust *= adjustmentScaleFactor;
    	
        System.out.println("Gyro adjust: " + gyroAdjust + " gyro: " + this.orientationGyro.getAngle() +  "Zero" + gyroZero);
    	
//    	return 0;
	    return gyroAdjust;
    }

}

