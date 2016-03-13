package org.usfirst.frc.team1188.robot;

public class RobotMap {
	// Joysticks
	public static final int driverJoystick = 0;
	public static final int operatorJoystick = 1;
	public static final int calibrationJoystick = 3;
	
	// Drive motors
	public static final int rightBigCim1Channel = 0;
	public static final int rightBigCim2Channel = 1;
	public static final int rightMiniCimChannel = 2;
	
	public static final int leftBigCim1Channel = 3;
	public static final int leftBigCim2Channel = 4;
	public static final int leftMiniCimChannel = 5;
	
	// Drive encoders
	public static final int leftDriveEncoder1 = 0;
	public static final int leftDriveEncoder2 = 1;
	public static final int rightDriveEncoder1 = 2;
	public static final int rightDriveEncoder2 = 3;

	// Arm CAN talons
	public static final int masterArmTalonId = 1;
	public static final int followerArmTalonId = 0;
	public static final int rollerTalonId = 2;
	
	// Arm limit switches
	public static final int armLowerLimitChannel = 4;
	public static final int armUpperLimitChannel = 5;
	
	// Boulder detection switch
	public static final int boulderDetectionSensorChannel = 6;
	
	// Camera
	public static final String cameraName = "cam0";

}
