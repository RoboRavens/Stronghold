package org.usfirst.frc.team1188.robot;

public class EncoderToMotor {
	protected double minimumOutput;
	protected double maximumOutput;
	protected double startPosition;
	protected double adjustmentFactor;

	public EncoderToMotor(double minimum, double adjustmentFactor){
		this.minimumOutput = minimum;
		this.adjustmentFactor = adjustmentFactor;
	}
	
	public void setMax(double maximum){
		this.maximumOutput = maximum;
	}
	
	public double power(double targetPosition, double encoderPosition) {
		double targetDistance, distanceTraveled, powerToMotors = 0;
		targetDistance = targetPosition - startPosition;
		distanceTraveled = encoderPosition - startPosition;
		powerToMotors = (targetDistance - distanceTraveled) * adjustmentFactor;

		//scale output to range
		if(powerToMotors > maximumOutput)
			powerToMotors = maximumOutput;
		if(powerToMotors < -maximumOutput)
			powerToMotors = -maximumOutput;
		if(powerToMotors > -minimumOutput && powerToMotors < minimumOutput)
			powerToMotors = 0;
		return powerToMotors;
	}
}
