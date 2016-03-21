package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public final class Calibrations {
	public static double slewRate = .35;
	public static double cutPowerModeMovementRatio = .3;
	public static double cutPowerModeTurnRatio = .5;
	public static double gyroAdjustmentScaleFactor = .03;
	public static double gyroCooldownTimerTime = 1;
	
	public static double armBaseSpeed = .5;
	public static double armRollerSpeed = 1;
	
	public static double lightingFlashTotalDurationMs = 1000;
	public static double lightingFlashes = 10;
	
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
	
	// Drive encoders (INCORRECT, NEEDS TO BE DETERMINED)
	public static final int encoderE4TCyclesPerRevolution = 360;
	public static final int encoderE4PCyclesPerRevolution = 250;
	public static final double driveWheelCircumferenceInches = 7.7;
	public static final double driveEncoderTicksPerInch = (double) Calibrations.driveEncoderTicksPerRevolution / Calibrations.driveWheelCircumferenceInches;
	
  public static final int leftEncoderCyclesPerRevolution = Calibrations.encoderE4TCyclesPerRevolution;
  public static final int rightEncoderCyclesPerRevolution = Calibrations.encoderE4PCyclesPerRevolution;
  
	// Accelerometer detection of robot surface (batter, floor, etc.)
	public static final Range accelerometerRange = Range.k8G;
	
	// INCORRECT, NEEDS TO BE DETERMINED. GUESSES BASED OFF OF 1G ON FLOOR.
	public static final double carpetRangeMinimum = .95;
	public static final double carpetRangeMaximum = 1.05;
	
	public static final double batterRangeMinimum = .85;
	public static final double batterRangeMaximum = .95;
	
	// Camera
	public static final int cameraQuality = 50;
	
	// Direction magic numbers
	public static final int drivingForward = 1;
	public static final int drivingBackward = -1;
	
	// Until we have more genuine motion profiling capabilities, we'll use this.
	// This is "feet necessary to decelerate per 1 magnitude of motor output."
	// Problems with this include that motor output is acceleration, but necessary
	// deceleration distance is correlated with distance.
	public static final double decelerationInchesPerMotorOutputMagnitude = 18;
	public static final double decelerationTicksPerMotorOutputMagnitude = Calibrations.decelerationInchesPerMotorOutputMagnitude * Calibrations.driveEncoderTicksPerInch;
	
	// Autonomous
	public static final double autonomousLowBarSpeed = .5;
	public static final int driveInchesToAlignWithLowGoalFromLowBar = 60;
	public static final int autonomousAlignmentSpeed = 1;
	public static final double turnDegreesToAlignWithLowGoalFromLowBar = 45;
	public static final int driveInchesToReachLowGoalFromLowBar = 84;
	public static final int driveInchesToClearOuterworks = 12;
	
	// Cheval
	public static final double autonomousChevalSpeed = .5;
	public static final double autonomousChevalCrossSpeed = .5;
	public static final int driveInchesToReachCheval = 36;
	public static final int driveInchesToCrossCheval = 36;
	
}
