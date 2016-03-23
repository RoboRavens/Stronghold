package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public final class Calibrations {
	// Drive calibration
	public static double slewRate = .35;
	public static double cutPowerModeMovementRatio = .3;
	public static double cutPowerModeTurnRatio = .5;
	public static double gyroAdjustmentScaleFactor = .03;
	public static double gyroCooldownTimerTime = 1;
	public static double translationMaxTurnScaling = .5;
	
	// Arm calibration
	public static double armBaseSpeed = .8;
	public static double armRollerSpeed = 1;
	
	// Lighting
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
	public static final int defaultGyroMode = Calibrations.gyroEnabled;
	
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
	public static final double driveEncoderE4TCyclesPerInch = (double) Calibrations.encoderE4TCyclesPerRevolution / Calibrations.driveWheelCircumferenceInches;
	public static final double driveEncoderE4PCyclesPerInch = (double) Calibrations.encoderE4PCyclesPerRevolution / Calibrations.driveWheelCircumferenceInches;
	
	public static final int leftEncoderCyclesPerRevolution = Calibrations.encoderE4TCyclesPerRevolution;
	public static final int rightEncoderCyclesPerRevolution = Calibrations.encoderE4TCyclesPerRevolution;
  
	// Accelerometer detection of robot surface (batter, floor, etc.)
	public static final Range accelerometerRange = Range.k8G;
	
	public static final double carpetRangeMinimum = .95;
	public static final double carpetRangeMaximum = 1.05;
	
	public static final double batterRangeMinimum = .85;
	public static final double batterRangeMaximum = .95;
	
	// These are guesses based on the manual, and still need to be tuned.
	// Polarity also needs to be checked; will depend on how the gyro is mounted.
	public static final double gyroCarpetRangeMinimum = -2;
	public static final double gyroCarpetRangeMaximum = 2;
	
	public static final double gyroBatterRangeMinimum = 5;
	public static final double gyroBatterRangeMaximum = 9;
		
	// Camera
	public static final int cameraQuality = 50;
	
	// Direction magic numbers
	public static final int drivingForward = -1;
	public static final int drivingBackward = 1;
	
	// Until we have more genuine motion profiling capabilities, we'll use this.
	// This is "feet necessary to decelerate per 1 magnitude of motor output."
	// Problems with this include that motor output is acceleration, but necessary
	// deceleration distance is correlated with distance.
	public static final double decelerationInchesPerMotorOutputMagnitude = 18;
	
	// Autonomous Modes
	public static final String autonomousRoughTerrain = "RT";
	public static final String autonomousRockWall = "RW";
	public static final String autonomousPortcullis = "PC";
	public static final String autonomousCheval = "CDF";
	public static final String autonomousMoat = "MOAT";
	public static final String autonomousRamparts = "RAMP";
	public static final String autonomousLowBar = "LB";
	
	public static final String autonomousReach = "REACH";
	public static final String autonomousLowBarGoal = "LBG";
		
	// Autonomous
	public static final double autonomousLowBarSpeed = .5;
	public static final int driveInchesToAlignWithLowGoalFromLowBar = 60;
	public static final int autonomousAlignmentSpeed = 1;
	public static final double turnDegreesToAlignWithLowGoalFromLowBar = 60;
	public static final int driveInchesToReachLowGoalFromLowBar = 84;
	public static final int driveInchesToClearOuterworks = 12;
	public static final int driveInchesToReachOuterWorks = 44;
	public static final int autonomousReachSpeed = .5;
	
	public static final int driveInchesToCrossLowBar = 140;
	public static final int driveInchesToAlignWithLowGoal = 223;
	public static final int driveInchesToReachLowGoal = 140;
	public static final int driveInchesToCrossBackToNeutralZone = 110;
	
	public static final int driveInchesToCrossRoughTerrain = 190;
	
	// For obstacles where the wheels might spin, set a high number of drive inches,
	// but *try* to avoid crashing into the driver station wall.
	public static final int driveInchesToCrossDriveObstacles = 260;
	public static final double autonomousDriveObstacleSpeed = 1;
	
		
	// Arm positions
	public static final int armAtTopOfRange = 1;
	public static final int armAtBottomOfRange = 2;
	
	public static final int armStartingPosition = Calibrations.armAtTopOfRange;
	
	// Cheval
	public static final double autonomousChevalSpeed = .5;
	public static final double autonomousChevalCrossSpeed = .5;
	public static final int driveInchesToReachCheval = 36;
	public static final int driveInchesToCrossCheval = 36;
	
}
