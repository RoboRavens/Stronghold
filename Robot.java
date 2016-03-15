package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1188.robot.AutonomousModes.*;

import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.CameraServer;

public class Robot extends IterativeRobot {
	public static OI oi;
	
	public SixWheelTankDrive driveTrain;
	public RobotArm arm;
	Lighting lighting;
	
	Joystick driveController;
	Joystick operatorController;
	
	AutonomousModesClass autoMode;
	
	Command autonomousCommand;
	String autoFromDashboard;
	SendableChooser chooser;
	
	CameraServer server;
	
    boolean booCount1;
    boolean booCount2;
    
    RavenAccelerometer accelerometer;
    
    IAutonomousMode autonomousMode;
    
    public Robot() {
		server = CameraServer.getInstance();
		server.setQuality(Calibrations.cameraQuality);
		server.startAutomaticCapture(RobotMap.cameraName);
    }

    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        SmartDashboard.putData("Auto mode", chooser);
        
        driveController = new Joystick(RobotMap.driverJoystick);
        operatorController = new Joystick(RobotMap.operatorJoystick);

        driveTrain = new SixWheelTankDrive(this);
        arm = new RobotArm();
        lighting = new Lighting();
        
        autoMode = new AutonomousModesClass(arm, driveTrain);
        booCount1 = false;
        booCount2 = false;
       
        ADXL362 adxl362 = new ADXL362(Calibrations.accelerometerRange);
        accelerometer = new RavenAccelerometer(adxl362);
    }

    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		this.maintainState();
		
        // String stringZero = SmartDashboard.getString("DB/String 0", "myDefaultData");
		autoFromDashboard = SmartDashboard.getString("DB/String 0", "myDefaultData");
		
		//check input
		switch (autoFromDashboard){
		case "RT":
			SmartDashboard.putString("DB/String 1", autoFromDashboard);
			break;
		case "LB":
			SmartDashboard.putString("DB/String 1", autoFromDashboard);
			break;
		case "PT":
			SmartDashboard.putString("DB/String 1", autoFromDashboard);
			break;
		case "BT":
			SmartDashboard.putString("DB/String 1", autoFromDashboard);
			break;
		default:
			SmartDashboard.putString("DB/String 1", "Error");
    }

		//arm.readEncoder();
//		driveTrain.getRightDriveEncoder();
    //    driveTrain.getLeftDriveEncoder();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        
        autoMode.startTimer();
        driveTrain.resetDriveGyro();
        
        switch (autoFromDashboard) {
        	case "LG":
        		autonomousMode = new AutonomousLowGoalScore(this);
            	autonomousMode.init();
            	break;
        	default:
        		autonomousMode = new AutonomousDoNothing();
            	autonomousMode.init();
            	break;
        }
        
        
        
        
                
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */

    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
        switch (autoFromDashboard){
    		case "RT":
    			autoMode.roughTerrain();
    			break;
    		case "LB":
    			autoMode.lowBar();
    			break;
    		case "PT":
    			autoMode.porty();
    			break;
    		case "BT":
    			autoMode.beta();
    			break;
        }
        
        this.maintainAutonomousState();
    }
    
    
    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
    	
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        // Start teleop in manual mode.
        this.driveTrain.overrideAutomatedDriving();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        double leftYAxisValue = driveController.getRawAxis(1);
    	double rightYAxisValue = driveController.getRawAxis(5);
    	double rightXAxisValue = driveController.getRawAxis(4);
    	
    	if(driveController.getRawButton(7)){
    		driveTrain.setGyroMode(0);
    	}
    	if(driveController.getRawButton(8)){
    //		driveTrain.setGyroMode(1);
    	}
    	if(driveController.getRawButton(5) || driveController.getRawButton(6)){
    		driveTrain.setLimitedPower(1);
    	}
    	else{
    		driveTrain.setLimitedPower(0);
    	}
    	if(driveController.getRawButton(2)){
    		if(!booCount1){
    			driveTrain.setGyroZero(90);
    			booCount1 = true;
    		}
    	}
    	else{
    		booCount1 = false;
    	}
    	
    	if(driveController.getRawButton(3)){
    		if(!booCount2){
    			driveTrain.setGyroZero(-90);
    			booCount2 = true;
    		}
    	}
    	else{
    		booCount2 = false;
    	}
    	
    
    	driveTrain.drive(leftYAxisValue, rightYAxisValue, rightXAxisValue);
        //driveTrain.getRightDriveEncoder();
       // driveTrain.getLeftDriveEncoder();
        driveTrain.getDriveGyro();
        
        if(operatorController.getRawButton(7)){
    		arm.setArmMode(0);
    	}
        if(operatorController.getRawButton(8)){
    		//arm.setArmMode(1);
    	}
        if(operatorController.getRawButton(2)){
    		arm.resetEncoder();
    	}
        arm.move(operatorController.getRawButton(1), operatorController.getRawButton(4));
        arm.intakeRoller(operatorController.getRawButton(5), operatorController.getRawButton(6));
        // arm.readEncoder();
        
        this.maintainState();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public void maintainAutonomousState() {
    	this.maintainState();
    	autonomousMode.maintainState();
    }
    
    public void maintainState() {
    	arm.maintainState();
    	accelerometer.maintainState();
    	lighting.maintainState();
    }
}