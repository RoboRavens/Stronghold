package org.usfirst.frc.team1188.robot.AutonomousModes;

import org.usfirst.frc.team1188.robot.IAutonomousMode;

public class AutonomousCrossChevalTwice implements IAutonomousMode {
	protected static final int driveToCheval = 1;
	protected static final int driveOverCheval = 2;
	protected static final int turnAround = 3;
	protected static final int realignWithCheval = 4;
	protected static final int driveOverCheval2 = 5;
	protected static final int turnAround2 = 6;
	
	protected static final int lowerArmToLowerCheval = 1;
	protected static final int raiseArmToClearCheval = 2;
	protected static final int lowerArmToLowerCheval2 = 3;
	protected static final int raiseArmToClearCheval2 = 4;
	protected static final int lowerArmToLowerCheval3 = 5;
	
	public void init() {
		
	}
	
	public void maintainState() {
		
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}
}
