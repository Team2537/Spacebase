package frc.robot.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.units.Units;
import frc.robot.Ports;
import frc.robot.Specs;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Ultrasonic;

//import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class DriveSubsystem extends Subsystem {
    
	/****************************************************************************/
	/*                            PUBLIC CONSTANTS                              */
	/****************************************************************************/

    // convert from revolutions to inches
    public static final double ENCODER_POSITION_FACTOR = 
        Units.revolutions_to_inches(1, Specs.DRIVE_WHEEL_DIAMETER*Specs.DRIVE_GEARBOX_RATIO);
    
    // convert from revolutions/minute to inches/second
    public static final double ENCODER_VELOCITY_FACTOR = 
        Units.revoltionsPerMinute_to_inchesPerSecond(1, Specs.DRIVE_WHEEL_DIAMETER*Specs.DRIVE_GEARBOX_RATIO);
    
    public static final double SIGN_LEFT = 1.0, SIGN_RIGHT = -1.0;

    public static final IdleMode DEFAULT_IDLE_MODE = IdleMode.kCoast;
    public static final MotorType MOTOR_TYPE = MotorType.kBrushless;
    private static final int NUM_VELS_TO_SAMPLE = 4;
    private static final double RUNNING_CURRENT = 2.0;

    public static final int[] MOTOR_PORTS_LEFT = { 
        Ports.DRIVE_MOTOR_LEFT_FRONT, 
        Ports.DRIVE_MOTOR_LEFT_TOP,
        Ports.DRIVE_MOTOR_LEFT_BACK 
    };
    public static final int[] MOTOR_PORTS_RIGHT = { 
        Ports.DRIVE_MOTOR_RIGHT_FRONT, 
        Ports.DRIVE_MOTOR_RIGHT_TOP,
        Ports.DRIVE_MOTOR_RIGHT_BACK 
    };


    /****************************************************************************/
	/*                           INSTANCE VARIABLES                             */
	/****************************************************************************/

    private CANSparkMax[] motorsLeft, motorsRight;
    private CANEncoder[] encodersLeft, encodersRight;

    private AHRS navX;
    private Ultrasonic frontUltrasonic;

    private Notifier accelUpdater;
    private double[] encoderVelWindowLeft, encoderVelWindowRight;
    private double accelLeft, accelRight;

    public DriveSubsystem() {
        /*********************************/
	    /*   INIT LEFT MOTORS/ENCODERS   */
	    /*********************************/
        motorsLeft = new CANSparkMax[MOTOR_PORTS_LEFT.length];
        encodersLeft = new CANEncoder[MOTOR_PORTS_LEFT.length];
        for (int i = 0; i < MOTOR_PORTS_LEFT.length; i++) {
            motorsLeft[i] = new CANSparkMax(MOTOR_PORTS_LEFT[i], MOTOR_TYPE);
            //if (i > 0) motorsLeft[i].follow(motorsLeft[0]);

            CANEncoder enc = motorsLeft[i].getEncoder();
            enc.setPositionConversionFactor(SIGN_LEFT*ENCODER_POSITION_FACTOR);
            enc.setVelocityConversionFactor(SIGN_LEFT*ENCODER_VELOCITY_FACTOR);
            enc.setPosition(0);
            encodersLeft[i] = enc;
        }

        /*********************************/
	    /*  INIT RIGHT MOTORS/ENCODERS   */
	    /*********************************/
        motorsRight = new CANSparkMax[MOTOR_PORTS_RIGHT.length];
        encodersRight = new CANEncoder[MOTOR_PORTS_RIGHT.length];
        for (int i = 0; i < MOTOR_PORTS_RIGHT.length; i++) {
            motorsRight[i] = new CANSparkMax(MOTOR_PORTS_RIGHT[i], MOTOR_TYPE);
            //if (i > 0) motorsRight[i].follow(motorsRight[0]);

            CANEncoder enc = motorsRight[i].getEncoder();
            enc.setPositionConversionFactor(SIGN_RIGHT*ENCODER_POSITION_FACTOR);
            enc.setVelocityConversionFactor(SIGN_RIGHT*ENCODER_VELOCITY_FACTOR);
            enc.setPosition(0);
            encodersRight[i] = enc;
        }

        setIdleMode(DEFAULT_IDLE_MODE);


        encoderVelWindowLeft = new double[NUM_VELS_TO_SAMPLE];
        encoderVelWindowRight = new double[NUM_VELS_TO_SAMPLE];

        accelUpdater = new Notifier(new Runnable(){
            @Override
            public void run(){ updateAccels(); }
        });
        accelUpdater.startPeriodic(Specs.ROBOT_PERIOD_SECONDS);
        
        navX = new AHRS(SPI.Port.kMXP);
        frontUltrasonic = new Ultrasonic(Ports.FRONT_ULTRASONIC_INPUT, Ports.FRONT_ULTRASONIC_OUTPUT);
        frontUltrasonic.setAutomaticMode(true);
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }


    /****************************************************************************/
	/*                              MOTOR METHODS                               */
	/****************************************************************************/

    public void setMotorsLeft(double percentOutput) {
        for(CANSparkMax motor : motorsLeft) {
            motor.set(SIGN_LEFT*percentOutput);
        }
        // motorsLeft[2].set(-percentOutput);
    }

    public void setMotorsRight(double percentOutput) {
        for(CANSparkMax motor : motorsRight) {
            motor.set(SIGN_RIGHT*percentOutput);
        }
        //motorsRight[0].set(percentOutput);
    }

    public void setMotors(double percentOutputLeft, double percentOutputRight) {
        setMotorsLeft(percentOutputLeft);
        setMotorsRight(percentOutputRight);
    }

    public void setIdleMode(IdleMode mode){
        for(CANSparkMax motor : motorsLeft) motor.setIdleMode(mode);
        for(CANSparkMax motor : motorsRight) motor.setIdleMode(mode);
    }


    /****************************************************************************/
	/*                             ENCODER METHODS                              */
	/****************************************************************************/

    private double getEncoderPos(CANEncoder[] encoders) {
        double[] positions = new double[encoders.length];
        for (int i = 0; i < encoders.length; i++) {
            positions[i] = encoders[i].getPosition();
        }
        return averageWithoutZeroes(positions);
    }

    public double getEncoderPosRight() {
        return getEncoderPos(encodersRight);
    }

    public double getEncoderPosLeft() {
        return getEncoderPos(encodersLeft);
    }

    private double getEncoderVel(CANEncoder[] encoders) {
        double[] vels = new double[encoders.length];
        for (int i = 0; i < encoders.length; i++) {
            vels[i] = encoders[i].getVelocity();
        }
        return averageWithoutZeroes(vels);
    }

    public double getEncoderVelRight() {
        return getEncoderVel(encodersRight);
    }

    public double getEncoderVelLeft() {
        return getEncoderVel(encodersLeft);
    }

    private void updateAccels() {
        final double velLeft = getEncoderVelLeft(), velRight = getEncoderVelRight();
        for (int i = 0; i < NUM_VELS_TO_SAMPLE - 1; i++) {
            encoderVelWindowLeft[i + 1] = encoderVelWindowLeft[i];
            encoderVelWindowRight[i + 1] = encoderVelWindowRight[i];
        }
        encoderVelWindowLeft[0] = velLeft;
        encoderVelWindowRight[0] = velRight;

        final double dt = 4 * Specs.ROBOT_PERIOD_SECONDS;
        accelLeft = (velLeft - encoderVelWindowLeft[NUM_VELS_TO_SAMPLE - 1]) / dt;
        accelRight = (velRight - encoderVelWindowRight[NUM_VELS_TO_SAMPLE - 1]) / dt;
    }

    public double getEncoderAccRight() {
        return accelRight;
    }

    public double getEncoderAccLeft() {
        return accelLeft;
    }

    public double getAppliedVoltageLeft() {
        return motorsLeft[0].getAppliedOutput();
    }

    public double getAppliedVoltageRight() {
        return motorsRight[0].getAppliedOutput();
    }

    public void resetEncoders(){
        for(CANEncoder enc : encodersLeft) enc.setPosition(0);
        for(CANEncoder enc : encodersRight) enc.setPosition(0);
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

    private static double averageWithoutZeroes(double[] samples) {
        double total = 0;
        for (double val : samples) {
            if (val != 0) {
                total += val;
            }
        }

        if (samples.length == 0) {
            return 0;
        } else {
            return total / samples.length;
        }
    }


    /****************************************************************************/
	/*                              GYRO METHODS                                */
	/****************************************************************************/

    public void resetGyro() {
        navX.reset();
    }
    
    public double getGyroDegrees() {
        return navX.getAngle();
    }
    
    public double getGyroRadians() {
        return Units.degrees_to_radians(getGyroDegrees());
    }

    /****************************************************************************/
	/*                          Test Methods                                    */
	/****************************************************************************/

    public void currentTest(){
        Boolean noError = true;
        String[] leftErrorArray = new String[motorsLeft.length];
        String[] rightErrorArray = new String[motorsRight.length];
        for(int i = 0; i < motorsLeft.length; i++){
            if (motorsLeft[i].getOutputCurrent() <= RUNNING_CURRENT){
                leftErrorArray[i] =  (motorsLeft[i] + "ERROR CHECK MOTOR");
                noError = false;
            } else {
                leftErrorArray[i] =  (motorsLeft[i] + "All Clear");
            }
        }
        for(int i = 0; i < motorsRight.length; i++){
            if (motorsLeft[i].getOutputCurrent() <= RUNNING_CURRENT){
                rightErrorArray[i] = (motorsRight[i] + "ERROR CHECK MOTOR");
                noError = false;
            } else {
                rightErrorArray[i] = (motorsRight[i] + "All Clear");
            }
        }
        SmartDashboard.putStringArray("leftErrorArray", leftErrorArray);
        SmartDashboard.putStringArray("rightErrorArray", rightErrorArray);
        SmartDashboard.putBoolean("driveTest", noError);
        

    }

    /****************************************************************************/
	/*                          MISC. SENSOR METHODS                            */
	/****************************************************************************/

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
        return frontUltrasonic.getRangeInches();
    }
    
}
