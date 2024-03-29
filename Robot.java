package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1188.robot.AutonomousModes.*;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
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
       
        BuiltInAccelerometer rioAccelerometer = new BuiltInAccelerometer();
        accelerometer = new RavenAccelerometer(rioAccelerometer);
    	System.out.println("Test.");
    }

    public void disabledInit(){
    	System.out.println("Disabled Init.");
    }
	
	public void disabledPeriodic() {
		System.out.println("Has boulder: " + this.arm.rollerHasBoulder());
		
		Scheduler.getInstance().run();
		this.maintainState();
		
		autoFromDashboard = SmartDashboard.getString("DB/String 0", "myDefaultData");
		
		switch (autoFromDashboard){
			case Calibrations.autonomousRoughTerrain:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousRockWall:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousPortcullis:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousCheval:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousMoat:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousRamparts:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousLowBar:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousReach:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			case Calibrations.autonomousLowBarGoal:
				putSmartDashboardStringOne(autoFromDashboard);
				break;
			default:
				putSmartDashboardStringOne("Error");
				break;
		}
	}
	
	public void putSmartDashboardStringOne(String value) {
		SmartDashboard.putString("DB/String 1", value);
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
			case Calibrations.autonomousRoughTerrain:
				autonomousMode = new AutonomousCrossRoughTerrain(this);
				break;
			case Calibrations.autonomousRockWall:
				autonomousMode = new AutonomousCrossDriveObstacle(this);
				break;
			// Portcullis autonomous hasn't been implemented yet.
			//case Calibrations.autonomousPortcullis:
			//	autonomousMode = new AutonomousCrossPortcullis(this);
			//	break;
			case Calibrations.autonomousCheval:
				autonomousMode = new AutonomousCrossCheval(this);
				break;
			case Calibrations.autonomousMoat:
				autonomousMode = new AutonomousCrossDriveObstacle(this);
				break;
			case Calibrations.autonomousRamparts:
				autonomousMode = new AutonomousCrossDriveObstacle(this);
				break;
			case Calibrations.autonomousLowBar:
				autonomousMode = new AutonomousCrossLowBar(this);
				break;
			case Calibrations.autonomousReach:
				autonomousMode = new AutonomousReachOuterWorks(this);
				break;
			case Calibrations.autonomousLowBarGoal:
				autonomousMode = new AutonomousLowGoalScore(this);
				break;
        	default:
        		autonomousMode = new AutonomousDoNothing();
            	break;
        }
		
		autonomousMode.init();
        
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
        switch (autoFromDashboard) {
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

    public void teleopPeriodic() { 	
        Scheduler.getInstance().run();
        double leftYAxisValue = driveController.getRawAxis(1);
    	double rightYAxisValue = driveController.getRawAxis(5);
    	double rightXAxisValue = driveController.getRawAxis(4);
    	
    	if (driveController.getRawButton(7)) {
    		driveTrain.setGyroMode(0);
    	}
    	if (driveController.getRawButton(8)) {
    //		driveTrain.setGyroMode(1);
    	}
    	if (driveController.getRawButton(5) || driveController.getRawButton(6)) {
    		driveTrain.setLimitedPower(1);
    	}
    	else {
    		driveTrain.setLimitedPower(0);
    	}
    	if (driveController.getRawButton(2)) {
    		if(booCount1 == false){
    			driveTrain.setGyroZero(90);
    			booCount1 = true;
    		}
    	}
    	else {
    		booCount1 = false;
    	}
    	
    	if (driveController.getRawButton(3)) {
    		if (booCount2 == false ) {
    			driveTrain.setGyroZero(-90);
    			booCount2 = true;
    		}
    	}
    	else{
    		booCount2 = false;
    	}
    	
    
    	driveTrain.drive(leftYAxisValue, rightYAxisValue, rightXAxisValue);
        driveTrain.getDriveGyro();
        
        if (operatorController.getRawButton(7)) {
    		arm.setArmMode(0);
    	}
        if (operatorController.getRawButton(8)) {

    	}
        if(operatorController.getRawButton(2)) {
    		arm.resetEncoder();
    	}
		
        arm.move(operatorController.getRawButton(1), operatorController.getRawButton(4));
        arm.intakeRoller(operatorController.getRawButton(5), operatorController.getRawButton(6));		
        
        if (operatorController.getRawButton(2)) {
        	this.lighting.quickToggle();
        }
        if (operatorController.getRawButton(3)) {
        	this.lighting.turnOn();
        }
        if (operatorController.getRawButton(4)) {
        	this.lighting.turnOff();
        }
        
        this.maintainState();
    }
    
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    public void maintainAutonomousState() {
    	this.maintainState();
    	autonomousMode.maintainState();
    	
    	this.logAutonomous();
    }
    
    public void logAutonomous() {
    	System.out.println("Autonomous step: " + this.autonomousMode.getStatus());
    }
    
    public void maintainState() {
    	arm.maintainState();
    	accelerometer.maintainState();
    	lighting.maintainState();
    }
}