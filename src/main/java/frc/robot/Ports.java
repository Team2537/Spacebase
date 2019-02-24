package frc.robot;

public class Ports {

    
    public static final int
        DRIVE_MOTOR_RIGHT_FRONT = 1,
        DRIVE_MOTOR_LEFT_FRONT  = 0
    ;
    

    /*
    // CAN Bus ports
    public static final int
        DRIVE_MOTOR_RIGHT_BACK  = 0,
        DRIVE_MOTOR_RIGHT_TOP   = 1,
        DRIVE_MOTOR_RIGHT_FRONT = 2,
        DRIVE_MOTOR_LEFT_FRONT  = 3,
        DRIVE_MOTOR_LEFT_TOP    = 4,
        DRIVE_MOTOR_LEFT_BACK   = 5
    ;
    */
    
    public static final int
        FRONT_ULTRASONIC_INPUT = 0, //INPUT IS ALWAYS SINGLE WIRED ONE
        FRONT_ULTRASONIC_OUTPUT = 1
    ;

    public static final int
        LINE_FOLLOWER_FRONT_UPPER = 2,
        LINE_FOLLOWER_FRONT_LOWER = 0,
        LINE_FOLLOWER_CENTER = 1
    ;

    // Drive station USB ports
    public static final int
        LEFT_JOYSTICK  = 0,
        RIGHT_JOYSTICK = 1
    ;
}