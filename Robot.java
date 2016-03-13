package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
// import org.usfirst.frc.team1188.robot.commands.ExampleCommand;
// import org.usfirst.frc.team1188.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static OI oi;
	
	SixWheelTankDrive driveTrain;
	RobotArm arm;
	Lighting lighting;
	
	Joystick driveController;
	Joystick operatorController;
	
	AutonomousModes autoMode;
	
	Command autonomousCommand;
	String autoFromDashboard;
	SendableChooser chooser;
	
	CameraServer server;
	
    boolean booCount1;
    boolean booCount2;
    
    RavenAccelerometer accelerometer;
        
  
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
        lighting = new Lighting();
        
        autoMode = new AutonomousModes(arm, driveTrain);
        booCount1 = false;
        booCount2 = false;
       
        ADXL362 adxl362 = new ADXL362(Range.k8G);
        accelerometer = new RavenAccelerometer(adxl362);
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
    
    public void maintainState() {
    	arm.maintainState();
    	accelerometer.maintainState();
    	lighting.maintainState();
    }
}