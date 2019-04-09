package frc.robot.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.units.Units;
import frc.robot.Ports;
import frc.robot.Specs;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

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
    
    public static final double SIGN_LEFT = -1.0, SIGN_RIGHT = -1.0; // TODO: revert before comp


    public static final IdleMode DEFAULT_IDLE_MODE = IdleMode.kCoast;
    public static final MotorType MOTOR_TYPE = MotorType.kBrushless;
    private static final int NUM_VELS_TO_SAMPLE = 4;
    private static final double RUNNING_CURRENT = 2.0;


    /****************************************************************************/
	/*                           INSTANCE VARIABLES                             */
	/****************************************************************************/

    private CANSparkMax[] motorsLeft, motorsRight;
    private CANEncoder[] encodersLeft, encodersRight;

    private AHRS navX;
    private Ultrasonic frontUltrasonic;
    private PowerDistributionPanel pdp;

    private Notifier accelUpdater;
    private double[] encoderVelWindowLeft, encoderVelWindowRight;
    private double accelLeft, accelRight;

    private DifferentialDrive driveTrain;
    private SpeedControllerGroup motorsControllerLeft, motorsControllerRight;

    private boolean drivePrecision;
    

    public DriveSubsystem() {
        
        /*********************************/
	    /*   INIT LEFT MOTORS/ENCODERS   */
	    /*********************************/
        motorsLeft = new CANSparkMax[] {
            new CANSparkMax(Ports.DRIVE_MOTOR_LEFT_FRONT, MOTOR_TYPE),
            new CANSparkMax(Ports.DRIVE_MOTOR_LEFT_TOP, MOTOR_TYPE),
            new CANSparkMax(Ports.DRIVE_MOTOR_LEFT_BACK, MOTOR_TYPE)
        };

        motorsControllerLeft = new SpeedControllerGroup(
            motorsLeft[0], motorsLeft[1], motorsLeft[2]
        );

        encodersLeft = new CANEncoder[motorsLeft.length];
        for (int i = 0; i < motorsLeft.length; i++) {
            CANEncoder enc = motorsLeft[i].getEncoder();
            enc.setPositionConversionFactor(SIGN_LEFT*ENCODER_POSITION_FACTOR);
            enc.setVelocityConversionFactor(SIGN_LEFT*ENCODER_VELOCITY_FACTOR);
            enc.setPosition(0);
            encodersLeft[i] = enc;
        }

        /*********************************/
	    /*  INIT RIGHT MOTORS/ENCODERS   */
	    /*********************************/
        motorsRight = new CANSparkMax[] {
            new CANSparkMax(Ports.DRIVE_MOTOR_RIGHT_FRONT, MOTOR_TYPE),
            new CANSparkMax(Ports.DRIVE_MOTOR_RIGHT_TOP, MOTOR_TYPE),
            new CANSparkMax(Ports.DRIVE_MOTOR_RIGHT_BACK, MOTOR_TYPE)
        };

        motorsControllerRight = new SpeedControllerGroup(
            motorsRight[0], motorsRight[1], motorsRight[2]
        );

        encodersRight = new CANEncoder[motorsRight.length];
        for (int i = 0; i < motorsRight.length; i++) {
            CANEncoder enc = motorsRight[i].getEncoder();
            enc.setPositionConversionFactor(SIGN_RIGHT*ENCODER_POSITION_FACTOR);
            enc.setVelocityConversionFactor(SIGN_RIGHT*ENCODER_VELOCITY_FACTOR);
            enc.setPosition(0);
            encodersRight[i] = enc;
        }

        
        driveTrain = new DifferentialDrive(motorsControllerLeft, motorsControllerRight);
        setIdleMode(DEFAULT_IDLE_MODE);


        encoderVelWindowLeft = new double[NUM_VELS_TO_SAMPLE];
        encoderVelWindowRight = new double[NUM_VELS_TO_SAMPLE];

        accelUpdater = new Notifier(new Runnable(){
            @Override
            public void run(){ updateAccels(); }
        });
        accelUpdater.startPeriodic(Specs.ROBOT_PERIOD_SECONDS);
        
        navX = new AHRS(SPI.Port.kMXP);
        frontUltrasonic = new Ultrasonic(Ports.FRONT_ULTRASONIC_TRIGGER, Ports.FRONT_ULTRASONIC_ECHO);
        frontUltrasonic.setAutomaticMode(true);

        drivePrecision = false;
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }


    /****************************************************************************/
	/*                              MOTOR METHODS                               */
	/****************************************************************************/

    public double getLeftSpeed(){
        double leftMotors = 0;
        for(CANSparkMax motor : motorsLeft) {
            leftMotors = motor.get();
        }
        return leftMotors;
    }

    public double getRightSpeed(){
        double rightMotors = 0;
        for(CANSparkMax motor : motorsLeft) {
            rightMotors = motor.get();
        }
        return rightMotors;
    }

    public void setMotors(double percentOutputLeft, double percentOutputRight) {
        driveTrain.tankDrive(SIGN_LEFT*percentOutputLeft, SIGN_RIGHT*percentOutputRight);
    }

    public void setIdleMode(IdleMode mode){
        for(CANSparkMax motor : motorsLeft) motor.setIdleMode(mode);
        for(CANSparkMax motor : motorsRight) motor.setIdleMode(mode);
    }

    public DifferentialDrive getDriveTrain(){
        return driveTrain;
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
        boolean noError = true;
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

    // @return the range of the drive ultrasonic in inches 
    public double getUltrasonic() {
        return frontUltrasonic.getRangeInches();
    }

    public boolean getDrivePrecision(){
        return drivePrecision;
    }

    public void toggleDrivePrecision(){
        drivePrecision = !drivePrecision;
    }
    
}
