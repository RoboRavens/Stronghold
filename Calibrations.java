package org.usfirst.frc.team1188.robot;

public final class Calibrations {
	public static double slewRate = .35;
	public static double cutPowerModeMovementRatio = .3;
	public static double cutPowerModeTurnRatio = .5;
	public static double gyroAdjustmentScaleFactor = .03;
	public static double gyroCooldownTimerTime = 1;
	
	public static double armBaseSpeed = .5;
	public static double armRollerSpeed = 1;
	
	public static double lightingFlashTotalDurationMs = 1000;
	public static double lightingFlashes = 5;
	
	// Drive and gyro modes
	public static final int bulldozerTank = 0;
	public static final int fpsTank = 1;
	
	public static final int gyroDisabled = 0;
	public static final int gyroEnabled = 1;
	
	// Deadband
	public static final double deadbandMagnitude = .1;
	
	// Default drive and gyro modes
	public static final int defaultDriveMode = Calibrations.fpsTank;
	public static final int defaultGyroMode = Calibrations.gyroDisabled;
	
	// Arm control mode
	public static final int armManualControlMode = 0;
	public static final int armPIDControlMode = 1;
	
	// Arm PID loop
	public static final double armPIDLoopPValue = .3;
	public static final double armPIDLoopIValue = 0;
	public static final double armPIDLoopDValue = 0;
	
	public static final int armDownTargetPosition = 1000;
	public static final int armUpTargetPosition = 0;
}
