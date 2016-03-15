package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Timer;

public class AutonomousModesClass {
	Timer autoTimer;
	RobotArm arm;
	SixWheelTankDrive driveTrain;
	EncoderToMotor rightEncoder;
	EncoderToMotor leftEncoder;
	
	public AutonomousModesClass(RobotArm arm, SixWheelTankDrive driveTrain){
		 autoTimer = new Timer();
		 this.arm = arm;
		 this.driveTrain = driveTrain;
		 
		 rightEncoder = new EncoderToMotor(0.1,0.001);
		 leftEncoder = new EncoderToMotor(0.1,0.001);
	}
	
	public void startTimer(){
		autoTimer.start();
	}
	
	public void lowBar() {
    	// armAuto the arm down.
        // The limit switches will turn it off when it gets to the bottom of its travel.
        if (autoTimer.get() > 0 && autoTimer.get() < 3) {
            arm.armAuto(-0.5);	
        }
        
        if (autoTimer.get() > 3) {
        	arm.armAuto(0);
        }
                
        if (autoTimer.get() > 3 && autoTimer.get() < 5) {
        	driveTrain.drive(-0.75, 0, 0);
        }
        else {
          // Stop the robot.
          driveTrain.drive(0, 0, 0);
        }
    }
    
    
    public void roughTerrain() {
    	// armAuto the arm down.
        // The limit switches will turn it off when it gets to the bottom of its travel.
        if (autoTimer.get() > 0) {
            arm.armAuto(-0.5);	
        }
        
        if (autoTimer.get() > .7) {
        	arm.armAuto(0);
        }
        
        if (autoTimer.get() < 2.3) {
          // Drive straight backward.
          driveTrain.drive(1, 0, 0);
        }
        else {
          // Stop the robot.
          driveTrain.drive(0, 0, 0);
        }
    }

    public void porty(){
    	
    	 arm.intakeRoller(true, false);
        if (autoTimer.get() > 0) {
            arm.armAuto(-0.5);	
        }
        
        if (autoTimer.get() > 3) {
        	arm.armAuto(0);
        }
        
        if (autoTimer.get() > 3 && autoTimer.get() < 8) {
          // Drive straight backward.
          driveTrain.drive(-0.5, 0, 0);
        }
        else {
          // Stop the robot.
          driveTrain.drive(0, 0, 0);
        }
    }
    
    public void beta(){
    	if (autoTimer.get() < 1) {
           leftEncoder.setMax(1);
           rightEncoder.setMax(1);
        }
    	driveTrain.bulldozerTank(leftEncoder.power(1000, driveTrain.getLeftDriveEncoder()), rightEncoder.power(1000, driveTrain.getRightDriveEncoder()));
    }
}


