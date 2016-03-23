package org.usfirst.frc.team1188.robot.AutonomousModes;

import org.usfirst.frc.team1188.robot.Calibrations;
import org.usfirst.frc.team1188.robot.IAutonomousMode;
import org.usfirst.frc.team1188.robot.Robot;

public class AutonomousLowGoalScore implements IAutonomousMode {
	protected static final int driveToAlignWithLowGoal = 1;
	protected static final int turnTowardsLowGoal = 2;
	protected static final int driveToLowGoal = 3;
	protected static final int waitAtLowGoalToScore = 4;
	protected static final int driveBackToAlignWithLowBar = 5;
	protected static final int turnRightTowardsLowBar = 6;
	protected static final int driveBackToNeutralZone = 7;
	protected static final int waitForTeleop = 8;
	
	protected static final int lowerArmForLowBar = 1;
	protected static final int scoreBoulder = 2;
	protected static final int wakeDriveTrain = 3;
	
	protected int driveFunction;
	protected boolean driveWaiting;
	
	protected int armFunction;
	protected boolean armWaiting;
	
	public Robot robot;
	
	public AutonomousLowGoalScore(Robot robot) {
		this.robot = robot;
	}
	
	public void init() {
		driveFunction = AutonomousLowGoalScore.driveToAlignWithLowGoal;
		driveWaiting = false;
		
		armFunction = AutonomousLowGoalScore.lowerArmForLowBar;
		armWaiting = false;
	}
		
	public void maintainState() {
		maintainDriveState();
		maintainArmState();
	}
	
	public void maintainDriveState() {
		if (driveWaiting == false) {
			switch (driveFunction) {
				case AutonomousLowGoalScore.driveToAlignWithLowGoal:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToAlignWithLowGoal, Calibrations.drivingForward, Calibrations.autonomousLowBarSpeed);
					break;
				case AutonomousLowGoalScore.turnTowardsLowGoal:
					robot.driveTrain.turnRelativeDegrees(Calibrations.turnDegreesToAlignWithLowGoalFromLowBar);
					break;
				case AutonomousLowGoalScore.driveToLowGoal:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToReachLowGoal, Calibrations.drivingForward, Calibrations.autonomousLowBarSpeed);
					break;
				case AutonomousLowGoalScore.waitAtLowGoalToScore:
					robot.driveTrain.stopAndWait();
					break;
				case AutonomousLowGoalScore.driveBackToAlignWithLowBar:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToReachLowGoal, Calibrations.drivingBackward, Calibrations.autonomousLowBarSpeed);
					break;
				case AutonomousLowGoalScore.turnRightTowardsLowBar:
					// Turn the 180 degree complement of what we turned before.
					robot.driveTrain.turnRelativeDegrees(180 - Calibrations.turnDegreesToAlignWithLowGoalFromLowBar);
					break;
				case AutonomousLowGoalScore.driveBackToNeutralZone:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToCrossBackToNeutralZone, Calibrations.drivingForward, Calibrations.autonomousLowBarSpeed);
					break;
				case AutonomousLowGoalScore.waitForTeleop:
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
				case AutonomousLowGoalScore.lowerArmForLowBar:
					robot.arm.moveArmToBottomOfRange();
					break;
				case AutonomousLowGoalScore.scoreBoulder:
					robot.arm.ejectBoulder();
					break;
				case AutonomousLowGoalScore.wakeDriveTrain:
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
}
