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
	int masterArmTalonId = 1;
	int followerArmTalonId = 0;
	int rollerTalonId = 2;
	double startPosition = 1;
	double targetPosition = 1;
	double powerToMotor;
	protected int armMode;
	
	public RobotArm() {
		
        ArmLeftSide = new CANTalon(followerArmTalonId);
        ArmRightSide = new CANTalon(masterArmTalonId);
        intakeRollerMotor = new CANTalon(rollerTalonId);
        armLowerLimit = new DigitalInput(4);
        armUpperLimit = new DigitalInput(5);
        rollerHasBoulderSwitch = new BufferedDigitalInput(6);
      
		setArmMode(0);
		ArmRightSide.setPosition(0);	
	}
	
	public void setArmMode(int armMode) {
	    	this.armMode = armMode;
	    	switch (this.armMode) {
    		case 0:
    	        ArmRightSide.setControlMode(0);
    	        ArmLeftSide.setControlMode(0);
    			break; 
    		case 1:
    	        ArmRightSide.setControlMode(1);
    	        ArmLeftSide.setControlMode(5);
    	        ArmRightSide.reverseOutput(true);
    	        ArmRightSide.setPID(0.30, 0, 0);
    	        break;
    	}
	 }

	
    public void move(boolean down, boolean up) {
    	switch (armMode) {
    		case 0:
    			armJoy(down,up);
    			break; 
    		case 1:
    			armPID(down,up);
    			break;
    	}
    }
    
    
    public void armJoy(boolean down, boolean up) {
    	double baseSpeed = .5;
    	double armSpeed = 0;
    	
    	// Include limit switch calls in the if statements.
    	if (up && armUpperLimit.get()) {
    		armSpeed = baseSpeed;
    	}
    	
    	if (down && armLowerLimit.get()) {
    		armSpeed = baseSpeed * -1;
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

    public void armPID(boolean up, boolean down) {
    	if (up) {
    		targetPosition=1000;
    	}
    	if (down) {
    		targetPosition=0;
    	}

    	if(!armLowerLimit.get()){
    		targetPosition = ArmRightSide.getEncPosition();
    	}
    	ArmRightSide.set(targetPosition);
     	ArmLeftSide.set(1);
    }
    
    public double getEncoder(){
    	  return ArmRightSide.getEncPosition();
    }
    
    
    
    public void resetEncoder(){
    	ArmRightSide.setPosition(0);
    }
    
    public void intakeRoller(boolean in, boolean out) {
    	
    	double rollerSpeed = 0;
    	if (in) {
    		rollerSpeed = 1;
    	}	
    	if (out) {
    		rollerSpeed = -1;
    	}
    	 
    	intakeRollerMotor.set(rollerSpeed);
    }
    
    public void maintainState() {
    	rollerHasBoulderSwitch.maintainState();
    }
    
}
