package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class RobotArm {
	CANTalon intakeRollerMotor;
	CANTalon ArmLeftSide;
	CANTalon ArmRightSide;
	DigitalInput armLowerLimit;
	DigitalInput armUpperLimit;
	BufferedDigitalInput rollerHasBoulderSwitch;
	
	double startPosition = 1;
	double targetPosition = 1;
	double powerToMotor;
	protected int armMode;
	
	protected boolean automatedMovementEnabled = false;
	protected boolean ejectingBall = false;
	protected Timer ballEjectionTimer;
	protected boolean waiting = false;
	
	protected int armTargetPosition = Calibrations.armStartingPosition;
	
	public RobotArm() {
        ArmLeftSide = new CANTalon(RobotMap.followerArmTalonId);
        ArmRightSide = new CANTalon(RobotMap.masterArmTalonId);
        intakeRollerMotor = new CANTalon(RobotMap.rollerTalonId);
        armLowerLimit = new DigitalInput(RobotMap.armLowerLimitChannel);
        armUpperLimit = new DigitalInput(RobotMap.armUpperLimitChannel);
        rollerHasBoulderSwitch = new BufferedDigitalInput(RobotMap.boulderDetectionSensorChannel);
      
		setArmMode(0);
	}
	
    public void wake() {
    	this.waiting = false;
    	this.automatedMovementEnabled = false;
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
    	if (up && armIsAtTopOfRange() == false) {
    		armSpeed = Calibrations.armBaseSpeed;
    	}
    	
    	if (down && armIsAtBottomOfRange() == false) {
    		armSpeed = Calibrations.armBaseSpeed * -1;
    	}
    	
    	ArmLeftSide.set(armSpeed);	
    	ArmRightSide.set(armSpeed);
    }
    
    public void armAuto(double armSpeed) {
    	// Include limit switch calls in the if statements.
    	if (armUpperLimit.get() == false) {
    		armSpeed = 0;
    	}
    	if (armLowerLimit.get() == false) {
    		armSpeed = 0;
    	}
    	
    	ArmLeftSide.set(armSpeed);	
    	ArmRightSide.set(armSpeed);
    }
    
    // Position is an integer; represents encoder ticks from 0.
    public void moveArmToPosition(int position) {
    	this.automatedMovementEnabled = true;
    	
    	// TODO: Implemented moving arm to encoder position.
    }
	
	public void moveArmToTopOfRange() {
		this.automatedMovementEnabled = true;
		
		this.armTargetPosition = Calibrations.armAtTopOfRange;
	}
	
	public void moveArmToBottomOfRange() {
		this.automatedMovementEnabled = true;
	
		this.armTargetPosition = Calibrations.armAtBottomOfRange;
	}
    
	public boolean armIsAtTopOfRange() {
		return this.armUpperLimit.get() == false;
	}
	
	public boolean armIsAtBottomOfRange() {
		return this.armLowerLimit.get() == false;
	}
	
    public void ejectBoulder() {
		this.ejectingBall = true;
    	this.automatedMovementEnabled = true;
    }
    
    public boolean automatedActionHasCompleted() {
    	// Just return the opposite of automatedMovementEnabled.
    	return this.automatedMovementEnabled == false;
    }
    
    
    public boolean rollerHasBoulder() {
    	return rollerHasBoulderSwitch.get() == false;
    } 
    
    // NOTE: The "up" and "down" labelings here are inconsistent.
    // Something needs to be checked and fixed.
    public void armPID(boolean up, boolean down) {
    	if (up) {
    		targetPosition = Calibrations.armDownTargetPosition;
    	}
    	if (down) {
    		targetPosition = Calibrations.armUpTargetPosition;
    	}

    	if(armLowerLimit.get() == false) {
    		targetPosition = ArmRightSide.getEncPosition();
    	}
    	ArmRightSide.set(targetPosition);
     	ArmLeftSide.set(1);
    }
    
    public void readEncoder() {
    	  System.out.println(" Encoder:" + ArmRightSide.getEncPosition());
    }
    
    public void resetEncoder() {
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
    	
    	if (this.automatedMovementEnabled == false) {
    		return;
    	}
    	
    	if (this.ejectingBall) {
    		maintainBallEjectionState();
    	}
    	
    	maintainArmMovementState();
    }
    
    public void maintainBallEjectionState() {
    	this.intakeRoller(false, true);
    	
    	// As soon as the roller switch is false, start a timer to run the
    	// roller for another half second.
    	if (this.rollerHasBoulder() == false) {
    		if (this.ballEjectionTimer.get() == 0) {
    			this.ballEjectionTimer.start();
    		}
    		
    		// If the timer has been going for a half second, shut it down.
    		if (this.ballEjectionTimer.get() >= .5) {
    			this.ballEjectionTimer.stop();
    			this.ballEjectionTimer.reset();
    			this.intakeRoller(false, false);    			
    			this.ejectingBall = false;
    			this.automatedMovementEnabled = false;
    		}
    	}
    }
    
    public void maintainArmMovementState() {
    	// TODO: Implement this method to check encoders vs target position.
    	// 			See SixWheelTankDrive.maintainStateDrivingStraight as a reference.
    	//			May need to add a class variable as a persistent target for the position.
    	
    	// For now assume the position is never reached.
    	boolean positionReached = false;
		
		if (armTargetPosition == Calibrations.armAtTopOfRange) {
			maintainArmMovingToTopOfRange();
			
			if (armIsAtTopOfRange()) {
				positionReached = true;
			}
		}
		
		if (armTargetPosition == Calibrations.armAtBottomOfRange) {
			maintainArmMovingToBottomOfRange();
			
			if (armIsAtBottomOfRange()) {
				positionReached = true;
			}
		}
    	
    	if (positionReached) {
    		this.automatedMovementEnabled = false;
    		return;
    	}
    }
	
	public void maintainArmMovingToTopOfRange() {
		this.armJoy(false, true);
	}
	
	public void maintainArmMovingToBottomOfRange() {
		this.armJoy(true, false);
	}    
}
