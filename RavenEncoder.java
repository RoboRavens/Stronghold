package org.usfirst.frc.team1188.robot;

import edu.wpi.first.wpilibj.Encoder;

public class RavenEncoder {
    Encoder encoder;
    
    int cyclesPerRevolution;
    double driveWheelDiameterInches;
    double driveWheelCircumferenceInches;
    
    public RavenEncoder(Encoder encoder, int cyclesPerRevolution, double driveWheelDiameterInches) {
        this.encoder = encoder;
        this.cyclesPerRevolution = cyclesPerRevolution;
        this.driveWheelDiameterInches = driveWheelDiameterInches;
        
        this.driveWheelCircumferenceInches = Math.Pi * driveWheelDiameterInches;
    }
    
    public double getNetRevolutions() {
        return (double) encoder.get() / cyclesPerRevolution;
    }
    
    public double getNetInchesTraveled() {
        double netRevolutions = getNetRevolutions();
        double netInchesTraveled = netRevolutions * driveWheelCircumferenceInches;
        
        return netInchesTraveled;
    }
}