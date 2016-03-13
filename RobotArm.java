package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class RobotArm {
	CANTalon intakeRollerMotor;
	CANTalon ArmLeftSide;
	CANTalon ArmRightSide;
	DigitalInput armLowerLimit;
	DigitalInput armUpperLimit;
	BufferedDigitalInput rollerHasBoulderSwitch;
	
	//EncoderToMotor armEncoder;
	double startPosition = 1;
	double targetPosition = 1;
	double powerToMotor;
	protected int armMode;
	
	public RobotArm() {
        ArmLeftSide = new CANTalon(RobotMap.followerArmTalonId);
        ArmRightSide = new CANTalon(RobotMap.masterArmTalonId);
        intakeRollerMotor = new CANTalon(RobotMap.rollerTalonId);
        armLowerLimit = new DigitalInput(RobotMap.armLowerLimitChannel);
        armUpperLimit = new DigitalInput(RobotMap.armUpperLimitChannel);
        rollerHasBoulderSwitch = new BufferedDigitalInput(RobotMap.boulderDetectionSensorChannel);
      
		setArmMode(0);
		ArmRightSide.setPosition(0);	
	}
	
	public void setArmMode(int armMode) {
    	this.armMode = armMode;
    	switch (this.armMode) {
    		case Calibrations.armManualControlMode:
    	        ArmRightSide.setControlMode(0);
    	        ArmLeftSide.setControlMode(0);
    			break;
    		case Calibrations.armPIDControlMode:
    	        ArmRightSide.setControlMode(1);
    	        ArmLeftSide.setControlMode(5);
    	        ArmRightSide.reverseOutput(true);
    	        ArmRightSide.setPID(Calibrations.armPIDLoopPValue, Calibrations.armPIDLoopIValue, Calibrations.armPIDLoopDValue);
    	        break;
    	}
	 }
	
    public void move(boolean down, boolean up) {
    	switch (armMode) {
    		case Calibrations.armManualControlMode:
    			armJoy(down, up);
    			break; 
    		case Calibrations.armPIDControlMode:
    			armPID(down, up);
    			break;
    	}
    }
    
    public void armJoy(boolean down, boolean up) {
    	double armSpeed = 0;
    	
    	// Include limit switch calls in the if statements.
    	if (up && armUpperLimit.get()) {
    		armSpeed = Calibrations.armBaseSpeed;
    	}
    	
    	if (down && armLowerLimit.get()) {
    		armSpeed = Calibrations.armBaseSpeed * -1;
    	}
    	
    	ArmLeftSide.set(armSpeed);	
    	ArmRightSide.set(armSpeed);
    }
    
    public void armAuto(double armSpeed) {
    	// Include limit switch calls in the if statements.
    	if (!armUpperLimit.get()) {
    		armSpeed = 0;
    	}
    	if (!armLowerLimit.get()) {
    		armSpeed = 0;
    	}
    	
    	ArmLeftSide.set(armSpeed);	
    	ArmRightSide.set(armSpeed);
    }
    
    
    public boolean rollerHasBoulder() {
    	return rollerHasBoulderSwitch.get();
    }
	
/*    public void armPosition(boolean up, boolean down) {
    	
    	if (up) {
    		targetPosition = 50000;
    		startPosition = ArmLeftSide.getEncPosition();
    	}
    	if (down) {
    		targetPosition = 1;
    		startPosition = ArmLeftSide.getEncPosition();
    	}

        //powerToMotor = armEncoder.power(startPosition, targetPosition, ArmLeftSide.getEncPosition(), 0);
        System.out.println("Target Position:" + targetPosition +" Encoder:" + ArmLeftSide.getEncPosition() + " Power:" + powerToMotor);
        ArmLeftSide.set(-powerToMotor/3);
    	//ArmRightSide.set(powerToMotor);
    } */
    
    
    // NOTE: The "up" and "down" labelings here are inconsistent.
    // Something needs to be checked and fixed.
    public void armPID(boolean up, boolean down) {
    	if (up) {
    		targetPosition = Calibrations.armDownTargetPosition;
    	}
    	if (down) {
    		targetPosition = Calibrations.armUpTargetPosition;
    	}

    	if(!armLowerLimit.get()){
    		targetPosition = ArmRightSide.getEncPosition();
    	}
    	ArmRightSide.set(targetPosition);
     	ArmLeftSide.set(1);
    }
    
    public void readEncoder(){
    	  System.out.println(" Encoder:" + ArmRightSide.getEncPosition());
    }
    
    public void resetEncoder(){
    	ArmRightSide.setPosition(0);
    }
    

    public void intakeRoller(boolean in, boolean out) {
    	
    	double rollerSpeed = 0;
    	if (in) {
    		rollerSpeed = Calibrations.armRollerSpeed;
    	}	
    	if (out) {
    		rollerSpeed = Calibrations.armRollerSpeed * -1;
    	}
    	 
    	intakeRollerMotor.set(rollerSpeed);
    }
    
    public void maintainState() {
    	rollerHasBoulderSwitch.maintainState();
    }
    
}
