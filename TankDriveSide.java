package org.usfirst.frc.team1188.robot;

public class TankDriveSide {
    Robot robot;
    
    RavenTalon motor1;
    RavenTalon motor2;
    RavenTalon motor3;
    
    RavenEncoder encoder;
    
    public TankDriveSide(RavenTalon motor1, RavenTalon motor2, RavenTalon motor3, RavenEncoder encoder) {
        this.motor1 = motor1;
        this.motor2 = motor2;
        this.motor3 = motor3;
        this.encoder = encoder;
    }
    
    public void setSlewRate(double slewRate) {
        motor1.setSlewRate(slewRate);
        motor2.setSlewRate(slewRate);
        motor3.setSlewRate(slewRate);
    }
    
    public void drive(double magnitude) {
        motor1.set(magnitude);
        motor2.set(magnitude);
        motor3.set(magnitude);
    }
    
    public double getNetWheelRevolutions() {
        return encoder.getNetRevolutions();
    }
    
    public double getNetInchesTraveled() {
      return encoder.getNetInchesTraveled();
    }
}