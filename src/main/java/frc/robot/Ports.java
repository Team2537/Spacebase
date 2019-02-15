package frc.robot;

public class Ports {
    // CAN Bus ports
    public static final int
        DRIVE_MOTOR_RIGHT_BACK  = 0,
        DRIVE_MOTOR_RIGHT_TOP   = 1,
        DRIVE_MOTOR_RIGHT_FRONT = 2,
        DRIVE_MOTOR_LEFT_FRONT  = 3,
        DRIVE_MOTOR_LEFT_TOP    = 4,
        DRIVE_MOTOR_LEFT_BACK   = 5, 
        ARM_MOTOR = 6,
        WRIST_MOTOR = 7,
        INTAKE_ARM_FLYWHEEL = 8,
        INTAKE_FLYWHEEL_ONE = 9,
        INTAKE_FLYWHEEL_TWO = 10
    ;

    //Analog Ports
    public static final int 
        WRIST_POTENTIOMETER = 0
    ;

    // Drive station USB ports
    public static final int
        LEFT_JOYSTICK  = 0,
        RIGHT_JOYSTICK = 1
    ;

        
    // infrared sensor ports for intake
    public static final int
        INTAKE_INFRARED = 0
    ;

    // solenoid ports
    public static final int
        INTAKE_PNEUMATIC_ONE = 0 ,
        INTAKE_PNEUMATIC_TWO = 1 ,
        CLIMB_SOLENOID_ONE = 2 
    ;

    //buttons left joystick
    public static final int
        INTAKE_FLYWHEEL_IN = 0,
        INTAKE_FLWYEEL_OUT = 3,
        CLIMB_ENGAGE_CLUTCH = 2
    ;

    //buttons right joystick
    public static final int
        ARM_UP_BUTTON = 1,
        ARM_DOWN_BUTTON = 2,
        ARM_INTAKE_FLYWHEEL_IN = 3,
        ARM_INTAKE_FLYWHEEL_OUT = 4,
        INTAKE_PNEUMATIC_EXTEND = 5

    ;



}