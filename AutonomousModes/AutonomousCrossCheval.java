package org.usfirst.frc.team1188.robot.AutonomousModes;

import org.usfirst.frc.team1188.robot.Calibrations;
import org.usfirst.frc.team1188.robot.IAutonomousMode;
import org.usfirst.frc.team1188.robot.Robot;

public class AutonomousCrossCheval implements IAutonomousMode {
	protected static final int driveToCheval = 1;
	protected static final int wakeArm = 2;
	protected static final int driveOverCheval = 3;
	
	protected static final int lowerArmToLowerCheval = 1;
	protected static final int wakeDriveTrain = 2;
	
	public Robot robot;
	
	protected int driveFunction;
	protected boolean driveWaiting;
	
	protected int armFunction;
	protected boolean armWaiting;
	
	public AutonomousCrossCheval(Robot robot) {
		this.robot = robot;
	}
	
	public void init() {
		driveFunction = AutonomousCrossCheval.driveToCheval;
		driveWaiting = false;
		
		armFunction = AutonomousCrossCheval.lowerArmToLowerCheval;
		armWaiting = true;
	}
		
	public void maintainState() {
		maintainDriveState();
		maintainArmState();
	}
	
	public void maintainDriveState() {
		if (driveWaiting == false) {
			switch (driveFunction) {
			case AutonomousCrossCheval.driveToCheval:
				robot.driveTrain.driveForwardInches(Calibrations.driveInchesToReachCheval, Calibrations.drivingForward, Calibrations.autonomousChevalSpeed);
				break;
			case AutonomousCrossCheval.wakeArm:
				robot.driveTrain.stopAndWait();
				robot.arm.wake();
				break;
			case AutonomousCrossCheval.driveOverCheval:
				robot.driveTrain.driveForwardInches(Calibrations.driveInchesToCrossCheval,  Calibrations.drivingForward, Calibrations.autonomousChevalCrossSpeed);
				break;
			}
			
			driveWaiting = true;
		}
		
		if (robot.driveTrain.automatedActionHasCompleted()) {
			driveWaiting = false;
			
			// Incrementing the drive function pushes the program to the next state.
			// Think of it like an array of actions (in fact, this *should* be a linked list or something instead.)
			driveFunction++;
		}
		
		robot.driveTrain.maintainState();
	}
	
	public void maintainArmState() {
		if (armWaiting == false) {
			switch (armFunction) {
				case AutonomousCrossCheval.lowerArmToLowerCheval:
					robot.arm.moveArmToBottomOfRange();
					break;
				case AutonomousCrossCheval.wakeDriveTrain:
					robot.driveTrain.wake();
					break;
			}
			
			armWaiting = true;
		}
		
		if (robot.arm.automatedActionHasCompleted()) {
			armWaiting = false;
			
			// Incrementing the arm function pushes the program to the next state.
			// Think of it like an array of actions (in fact, this *should* be a linked list or something instead.)
			armFunction++;
		}
		
		robot.arm.maintainState();
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
