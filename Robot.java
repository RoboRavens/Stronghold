
package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
// import org.usfirst.frc.team1188.robot.commands.ExampleCommand;
// import org.usfirst.frc.team1188.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	SixWheelTankDrive driveTrain;
	RobotArm arm;
	Joystick driveController;
	Joystick operatorController;
	Command autonomousCommand;
	SendableChooser chooser;
	CameraServer server;

  public Robot() {
	server = CameraServer.getInstance();
	server.setQuality(50);
	//the camera name (ex "cam0") can be found through the roborio web interface
	server.startAutomaticCapture("cam0");
  }
  
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        // chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
                
        driveController = new Joystick(0);
        operatorController = new Joystick(1);

        driveTrain = new SixWheelTankDrive();
        arm = new RobotArm();
        
        
    }
        
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
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

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
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
    		driveTrain.setDriveMode(0);
    	}
    	if(driveController.getRawButton(8)){
    		driveTrain.setDriveMode(1);
    	}
    	if(driveController.getRawButton(5) || driveController.getRawButton(6)){
    		driveTrain.setLimitedPower(1);
    	}
    	else{
    		driveTrain.setLimitedPower(0);
    	}
    	if(driveController.getRawButton(2)){
    		driveTrain.setGyroMode(0);
    	}
    	if(driveController.getRawButton(3)){
    		driveTrain.setGyroMode(1);
    	}
        driveTrain.drive(leftYAxisValue, rightYAxisValue, rightXAxisValue);
        //driveTrain.getRightDriveEncoder();
       // driveTrain.getLeftDriveEncoder();
        driveTrain.getDriveGyro();
        
        if(operatorController.getRawButton(7)){
    		arm.setArmMode(0);
    	}
        if(operatorController.getRawButton(8)){
    		arm.setArmMode(1);
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
    
    public void maintainState() {
    	arm.maintainState();
    }
}
