package org.usfirst.frc.team1188.robot.AutonomousModes;

import org.usfirst.frc.team1188.robot.Calibrations;
import org.usfirst.frc.team1188.robot.IAutonomousMode;
import org.usfirst.frc.team1188.robot.Robot;

public class AutonomousCrossLowBar implements IAutonomousMode {
	protected static final int driveToAlignWithLowGoal = 1;
	protected static final int waitForTeleop = 2;
	
	protected static final int lowerArmForLowBar = 1;
	
	protected int driveFunction;
	protected boolean driveWaiting;
	
	protected int armFunction;
	protected boolean armWaiting;
	
	public Robot robot;
	
	public AutonomousCrossLowBar(Robot robot) {
		this.robot = robot;
	}
	
	public void init() {
		driveFunction = AutonomousCrossLowBar.driveToAlignWithLowGoal;
		driveWaiting = false;
		
		armFunction = AutonomousCrossLowBar.lowerArmForLowBar;
		armWaiting = false;
	}
		
	public void maintainState() {
		maintainDriveState();
		maintainArmState();
	}
	
	public void maintainDriveState() {
		if (driveWaiting == false) {
			switch (driveFunction) {
				case AutonomousCrossLowBar.driveToAlignWithLowGoal:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToCrossLowBar, Calibrations.drivingForward, Calibrations.autonomousLowBarSpeed);
					break;
				case AutonomousCrossLowBar.waitForTeleop:
					robot.driveTrain.stopAndWait();
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
				case AutonomousCrossLowBar.lowerArmForLowBar:
					robot.arm.moveArmToBottomOfRange();
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
