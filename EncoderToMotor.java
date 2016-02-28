package org.usfirst.frc.team1188.robot;

public class EncoderToMotor {

	public double power(double startPosition,double targetPosition, double encoderPosition, double minimumOutput) {
		double targetDistance, distanceTraveled, powerToMotors = 0;
		targetDistance = targetPosition - startPosition;
		distanceTraveled = encoderPosition - startPosition;
		powerToMotors = (targetDistance - distanceTraveled) / Math.abs(targetDistance);

		//scale output to range
		if(powerToMotors > 1)
			powerToMotors = 1;
		if(powerToMotors < -1)
			powerToMotors = -1;
		if(powerToMotors > -minimumOutput && powerToMotors < minimumOutput)
			powerToMotors = 0;
		return powerToMotors;
	}
}
