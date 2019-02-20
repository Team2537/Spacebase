package frc.robot.drive;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Ports;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
//import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;

public class DriveSubsystem extends Subsystem {
    //private WPI_TalonSRX RightFront, LeftFront;
    private Encoder RightEnc, LeftEnc;
    
    private static final MotorType MOTOR_TYPE = MotorType.kBrushless;
    
    private static final int[] MOTOR_PORTS_LEFT = { 
        Ports.DRIVE_MOTOR_LEFT_FRONT, 
        Ports.DRIVE_MOTOR_LEFT_TOP,
        Ports.DRIVE_MOTOR_LEFT_BACK 
    
    };
    private static final int[] MOTOR_PORTS_RIGHT = { 
        Ports.DRIVE_MOTOR_RIGHT_FRONT, 
        Ports.DRIVE_MOTOR_RIGHT_TOP,
        Ports.DRIVE_MOTOR_RIGHT_BACK 
    };

    

    

    private CANSparkMax[] motorsLeft, motorsRight;
    private CANEncoder[] encodersLeft, encodersRight;
    
    private AHRS navX;
    private Ultrasonic driveUltrasonic;
    private DigitalInput IR_frontUpper, IR_frontLower, IR_center;

    public DriveSubsystem() {
        //MAKE SURE ULTRASONIC IS CORRECT FOR PEANUT!!
        driveUltrasonic = new Ultrasonic(Ports.FRONT_ULTRASONIC_INPUT, Ports.FRONT_ULTRASONIC_OUTPUT);
        driveUltrasonic.setAutomaticMode(true);
        driveUltrasonic.setEnabled(true);
        //RightFront = new WPI_TalonSRX(Ports.DRIVE_MOTOR_RIGHT_FRONT);
        //LeftFront = new WPI_TalonSRX(Ports.DRIVE_MOTOR_LEFT_FRONT);
        
        motorsLeft = new CANSparkMax[MOTOR_PORTS_LEFT.length];
        //encodersLeft = new CANEncoder[MOTOR_PORTS_LEFT.length];
        /*
        IR_frontUpper = new DigitalInput(0); IR_frontLower = new DigitalInput(1);
        IR_center = new DigitalInput(2);
        */

        
        for (int i = 0; i < MOTOR_PORTS_LEFT.length; i++) {
            motorsLeft[i] = new CANSparkMax(MOTOR_PORTS_LEFT[i], MOTOR_TYPE);
            encodersLeft[i] = motorsLeft[i].getEncoder();
        }

        motorsRight = new CANSparkMax[MOTOR_PORTS_RIGHT.length];
        encodersRight = new CANEncoder[MOTOR_PORTS_RIGHT.length];
        for (int i = 0; i < MOTOR_PORTS_RIGHT.length; i++) {
            motorsRight[i] = new CANSparkMax(MOTOR_PORTS_RIGHT[i], MOTOR_TYPE);
            encodersRight[i] = motorsRight[i].getEncoder();
        }
        
        
        navX = new AHRS(SPI.Port.kMXP);
        /*
        IR_frontUpper = new DigitalInput(Ports.LINE_FOLLOWER_FRONT_UPPER);
        IR_frontLower = new DigitalInput(Ports.LINE_FOLLOWER_FRONT_LOWER);
        IR_center = new DigitalInput(Ports.LINE_FOLLOWER_CENTER);
        */

    }

    @Override
    public void initDefaultCommand() {
        //setDefaultCommand(new DriveCommand());
    }
    
    private void setMotorsSide(double percentOutput, CANSparkMax[] motors) {
        for (CANSparkMax motor : motors) {
            motor.set(percentOutput);
        }
    }
    

    public void setMotorsLeft(double percentOutput) {
        //LeftFront.set(percentOutput);
        setMotorsSide(percentOutput, motorsLeft);
    }

    public void setMotorsRight(double percentOutput) {
        //RightFront.set(percentOutput);
        setMotorsSide(percentOutput, motorsRight);
    }

    public void setMotors(double percentOutputLeft, double percentOutputRight) {
        //setMotorsLeft(percentOutputLeft);
        System.out.println("LEFT MOTOR: " + percentOutputLeft);

        //setMotorsRight(percentOutputRight);
        System.out.println("RIGHT MOTOR: " + percentOutputRight);
    }

    public String encoderStatus() {
        String out = "";
        for (int i = 0; i < encodersLeft.length; i++) {
            out += "Left  Encoder " + i + ": " + encodersLeft[i].getPosition() + "\n";
        }
        for (int i = 0; i < encodersRight.length; i++) {
            out += "Right Encoder " + i + ": " + encodersRight[i].getPosition() + "\n";
        }
        return out;
    }

    private double getEncoderPos(CANEncoder[] encoders) {
        double total = 0;
        double position;
        for (CANEncoder encoder : encoders) {
            position = encoder.getPosition();
            if (position != 0) {
                total += position;
            }
        }

        if (total == 0) {
            return 0;
        } else {
            return total / encoders.length;
        }
    }

    public double getEncoderPosRight() {
        return RightEnc.getDistance()*8*Math.PI/360.0;
        //return getEncoderPos(encodersRight);
    }

    public double getEncoderPosLeft() {
        return -LeftEnc.getDistance()*8*Math.PI/360.0;
        //return getEncoderPos(encodersLeft);
    }

    public void resetEncoders(){
        RightEnc.reset();
        LeftEnc.reset();
    }

    public void resetGyro() {
        navX.reset();
    }
    

    public double getGyroDegrees() {
        //return navX.getAngle();
        return -navX.getAngle(); //TODO: set back to positive values for comp bot
    }
    
    public double getGyroRadians() {
        return getGyroDegrees() * Math.PI / 180;
    }
    /*
    // FALSE MEANS ON THE LINE
    /*
    public boolean getIR_frontUpper() {
        System.out.println("IR Front Upper: " + IR_frontUpper.get());
        return !IR_frontUpper.get();
    }

    public boolean getIR_frontLower() {
        System.out.println("IR Front Lower: " + IR_frontLower.get());
        return !IR_frontLower.get();
    }

    public boolean getIR_center() {
        System.out.println("IR Center: " + IR_center.get());
        return !IR_center.get();
    }
    */

    // @return the range of the drive ultrasonic in inches 
    public double getUltrasonic() {
        System.out.println("ULTRASONIC RANGE: " + driveUltrasonic.getRangeInches());
        return driveUltrasonic.getRangeInches();
    }
    
}
