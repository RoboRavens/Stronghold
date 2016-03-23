package org.usfirst.frc.team1188.robot.AutonomousModes;

import org.usfirst.frc.team1188.robot.Calibrations;
import org.usfirst.frc.team1188.robot.IAutonomousMode;
import org.usfirst.frc.team1188.robot.Robot;

public class AutonomousCrossRoughTerrain implements IAutonomousMode {
	protected static final int driveToCrossRoughTerrain = 1;
	protected static final int waitForTeleop = 2;
	
	protected int driveFunction;
	protected boolean driveWaiting;
	
	public Robot robot;
	
	public AutonomousCrossRoughTerrain(Robot robot) {
		this.robot = robot;
	}
	
	public void init() {
		driveFunction = AutonomousCrossRoughTerrain.driveToCrossRoughTerrain;
		driveWaiting = false;
	}
		
	public void maintainState() {
		maintainDriveState();
	}
	
	public void maintainDriveState() {
		if (driveWaiting == false) {
			switch (driveFunction) {
				case AutonomousCrossRoughTerrain.driveToCrossRoughTerrain:
					robot.driveTrain.driveForwardInches(Calibrations.driveInchesToCrossRoughTerrain, Calibrations.drivingForward, Calibrations.autonomousDriveObstacleSpeed);
					break;
				case AutonomousCrossRoughTerrain.waitForTeleop:
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

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
