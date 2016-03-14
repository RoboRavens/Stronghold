package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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

	Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;
    
    ADXRS450_Gyro orientationGyro;
    Timer gyroCooldownTimer;
    
    protected int driveMode;
	protected int gyroMode;
	protected int limitedPower;
	protected double gyroZero;
	protected double orientation = 0;
	protected double slewRate;
	
	Joystick calibrationStick = new Joystick(RobotMap.calibrationJoystick);
	
	protected int netEncoderTicksTraveled = 0;
	protected int targetNetEncoderTicksTraveled = 0;
	protected boolean automatedDrivingEnabled = false;
	protected int automatedDrivingDirection = Calibrations.drivingForward;
	protected double automatedDrivingSpeed = 0;
	
	protected int leftEncoderReferencePoint = 0;
	protected int rightEncoderReferencePoint = 0;
	
	public SixWheelTankDrive(){
		slewRate = Calibrations.slewRate;
		
		driveRight1 = new RavenTalon(RobotMap.rightBigCim1Channel, slewRate);
		driveRight2 = new RavenTalon(RobotMap.rightBigCim2Channel, slewRate);
		driveRightInverted = new RavenTalon(RobotMap.rightMiniCimChannel, slewRate);
		driveLeft1 = new RavenTalon(RobotMap.leftBigCim1Channel, slewRate);
		driveLeft2 = new RavenTalon(RobotMap.leftBigCim2Channel, slewRate);
		driveLeftInverted = new RavenTalon(RobotMap.leftMiniCimChannel, slewRate);
		
		orientationGyro = new ADXRS450_Gyro();
		gyroCooldownTimer = new Timer();
		
		leftDriveEncoder = new Encoder(RobotMap.leftDriveEncoder1, RobotMap.leftDriveEncoder2);
		rightDriveEncoder = new Encoder(RobotMap.rightDriveEncoder1, RobotMap.rightDriveEncoder2);
		
		setDriveMode(Calibrations.defaultDriveMode);
		setLimitedPower(0);
		setGyroMode(Calibrations.defaultGyroMode);
		gyroZero = setGyroZero();
		
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
	
	public double deadband(double input) {
		double output = input;
		
		if (Math.abs(output) < Calibrations.deadbandMagnitude) {
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
    	left = deadband(left);
    	rightY = deadband(rightY);
    	rightX = deadband(rightX);
    	
    	this.setSlewRate(Math.abs(this.calibrationStick.getZ() * 2));
    	
    	switch (driveMode) {
    		case Calibrations.bulldozerTank:
    			bulldozerTank(left, rightY);
    			break; 
    		case Calibrations.fpsTank:
    			fpsTank(left, rightX);
    			break;
    	}
    }
	
	public void bulldozerTank(double left, double right) {
		// Invert the left side.
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
    	    	
        double gyroAdjust = getTurnableGyroAdjustment(turn); 
    	
    	// System.out.println("Gyro adjust: " + gyroAdjust + " gyro: " + this.orientationGyro.getAngle());
        
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
    	// If the gyro is in disabled mode, just return immediately.
    	if (gyroMode == Calibrations.gyroDisabled) {
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
    	// If the gyro is in disabled mode, just return immediately.
    	if (gyroMode == Calibrations.gyroDisabled) {
    		return 0;
    	}
    	    	
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
    	
    	gyroAdjust *= Calibrations.gyroAdjustmentScaleFactor;
    	
        System.out.println("Gyro adjust: " + gyroAdjust + " gyro: " + this.orientationGyro.getAngle() +  "Zero" + gyroZero);
    	
        return gyroAdjust;
    }
    
    public void driveForwardInches(double inches, int direction, double speed) {
    	leftEncoderReferencePoint = this.leftDriveEncoder.get();
    	rightEncoderReferencePoint = this.rightDriveEncoder.get();
    	
    	int targetEncoderTicks = (int) Math.round(inches * Calibrations.driveEncoderTicksPerInch);
    	this.targetNetEncoderTicksTraveled = targetEncoderTicks;
    	this.automatedDrivingEnabled = true;
    	this.automatedDrivingDirection = direction;
    	this.automatedDrivingSpeed = speed;
    }
    
    public void maintainState() {
    	// Maintain state only does things while automated driving is enabled.
    	if (automatedDrivingEnabled == false) {
    		return;
    	}
    	
    	this.maintainEncoders();
    	
    	// Check if we've made it to the destination.
    	if (netEncoderTicksTraveled <= targetNetEncoderTicksTraveled) {
    		automatedDrivingEnabled = false;
    		return;
    	}
    	
    	
    	// Automated driving: confirm direction, and set power based on speed.
    	// But, if within deceleration zone, start decelerating using "poor man's PID".
    	double powerCoefficient = getPowerCoefficient();
    	
    	double power = this.automatedDrivingSpeed * powerCoefficient;
    	
    	power *= this.automatedDrivingDirection;

    	this.fpsTank(power, 0);    	
    }
    
    public void maintainEncoders() {
    	int leftEncoderTicks = this.leftDriveEncoder.get() - this.leftEncoderReferencePoint;
    	int rightEncoderTicks = this.rightDriveEncoder.get() - this.rightEncoderReferencePoint;
    	
    	this.netEncoderTicksTraveled = leftEncoderTicks + rightEncoderTicks / 2;
    }
    
    public double getPowerCoefficient() {
    	double decelerationRangeEncoderTicks = Calibrations.decelerationTicksPerMotorOutputMagnitude * this.automatedDrivingSpeed;
    	
    	int ticksToGo = targetNetEncoderTicksTraveled - netEncoderTicksTraveled;
    	
    	// Any power cuts will be applied through this coefficient. 1 means no cuts.
    	double powerCoefficient = 1;
    	
    	// The power coefficient will be what percent of the deceleration range has been
    	// is yet to be traversed, but never higher than one.
    	powerCoefficient = Math.min(1, ticksToGo / decelerationRangeEncoderTicks);
    	
    	return powerCoefficient;
    }
    
}