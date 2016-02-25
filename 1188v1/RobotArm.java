package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.CANTalon;

public class RobotArm {

	CANTalon intakeRollerMotor;
	CANTalon ArmLeftSide;
	CANTalon ArmRightSide;
	//EncoderToMotor armEncoder;
	int masterArmTalonId = 1;
	int followerArmTalonId = 0;
	int rollerTalonId = 2;
	double startPosition = 1;
	double targetPosition = 1;
	double powerToMotor;
	protected int armMode;
	
	public RobotArm() {
		
		setArmMode(0);
        ArmLeftSide = new CANTalon(followerArmTalonId);
        ArmRightSide = new CANTalon(masterArmTalonId);
        intakeRollerMotor = new CANTalon(rollerTalonId);
      //  armEncoder = new EncoderToMotor();
     //   ArmLeftSide.setControlMode(1);

   //     ArmLeftSide.reverseOutput(true);
        //ArmRightSide.setInverted(true);
   //     ArmLeftSide.setPosition(0);
   //     ArmLeftSide.setPID(0.15, 0, 0);
   //    ArmRightSide.setControlMode(5);

	//practice bot right side encoder

//        ArmLeftSide.setInverted(true);
  //       ArmRightSide.setPosition(0);
        
         

	
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

	
    public void move(boolean up, boolean down) {
    	switch (armMode) {
    		case 0:
    			armJoy(up,down);
    			break; 
    		case 1:
    			armPID(up,down);
    			break;
    	}
    }
    
    
    public void armJoy(boolean up, boolean down) {
    	
    	double armSpeed = 0;
    	if (up) {
    		armSpeed = 0.30;
    	}	
    	if (down) {
    		armSpeed = -0.30;
    	}
    	 
    	ArmLeftSide.set(armSpeed);
    	ArmRightSide.set(armSpeed);
    }
	
    public void armPosition(boolean up, boolean down) {
    	
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
    }
    
    public void armPID(boolean up, boolean down) {
    	
    	if (up) {
    		//ArmLeftSide.set(3000);
    		ArmRightSide.set(1000);
    	}
    	if (down) {
    		//ArmLeftSide.set();
    		ArmRightSide.set(0);
    	}

    //	ArmRightSide.set(0);
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
    		rollerSpeed = 1;
    	}	
    	if (out) {
    		rollerSpeed = -1;
    	}
    	 
    	intakeRollerMotor.set(rollerSpeed);
    }
    
}
