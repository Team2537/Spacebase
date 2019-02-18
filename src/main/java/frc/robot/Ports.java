package frc.robot;

public class Ports {


    // CAN Bus ports
    public static final int
        DRIVE_MOTOR_RIGHT_BACK  = 1,
        DRIVE_MOTOR_RIGHT_TOP   = 2,
        DRIVE_MOTOR_RIGHT_FRONT = 3,
        DRIVE_MOTOR_LEFT_FRONT  = 4,
        DRIVE_MOTOR_LEFT_TOP    = 5,
        DRIVE_MOTOR_LEFT_BACK   = 6, 
        ARM_MOTOR = 7,
        WRIST_MOTOR =10,
        INTAKE_ARM_FLYWHEEL =11,
        INTAKE_FLYWHEEL_ONE = 8,
        INTAKE_FLYWHEEL_TWO = 9
    ;

    //Analog Ports
    public static final int 
        WRIST_POTENTIOMETER = 0,
        ARM_POTENTIOMETER = 1
    ;
    
    public static final int
        FRONT_ULTRASONIC_INPUT = 0,
        FRONT_ULTRASONIC_OUTPUT = 1
    ;

    // Drive station USB ports
    public static final int
        LEFT_JOYSTICK  = 0,
        RIGHT_JOYSTICK = 1,
        XBOX_CONTROLLER = 2

    ;

    // solenoid ports
    public static final int
        INTAKE_PNEUMATIC_ONE = 2 ,
        CLIMB_SOLENOID_TWO = 0 ,
        CLIMB_SOLENOID_ONE = 1, 
        ARM_SOLENOID_ONE = 3
    ;

}