package frc.robot;

public class Ports {

    // CAN Bus ports
    public static final int
        /*
        DRIVE_MOTOR_RIGHT_BACK  = 1,
        DRIVE_MOTOR_RIGHT_TOP   = 2,
        DRIVE_MOTOR_RIGHT_FRONT = 3,
        DRIVE_MOTOR_LEFT_FRONT  = 4,
        DRIVE_MOTOR_LEFT_TOP    = 5,
        DRIVE_MOTOR_LEFT_BACK   = 6, 
        */
        // TODO: drive motor ports on test bot are flipped, CHANGE BACK BEFORE COMPETITION
        DRIVE_MOTOR_LEFT_BACK  = 1,
        DRIVE_MOTOR_LEFT_TOP   = 2,
        DRIVE_MOTOR_LEFT_FRONT = 3,
        DRIVE_MOTOR_RIGHT_FRONT  = 4,
        DRIVE_MOTOR_RIGHT_TOP    = 5,
        DRIVE_MOTOR_RIGHT_BACK   = 6, 

        ARM_MOTOR = 7,
        INTAKE_FLYWHEEL_LEFT = 8,
        INTAKE_FLYWHEEL_RIGHT = 9,
        WRIST_MOTOR =10,
        INTAKE_ARM_FLYWHEEL =11
    ;

    //Analog Ports
    public static final int 
        WRIST_POTENTIOMETER = 0,
        ARM_POTENTIOMETER = 1
    ;
    
    public static final int
        FRONT_ULTRASONIC_TRIGGER = 0, //TRIGGER IS ALWAYS SINGLE WIRED ONE
        FRONT_ULTRASONIC_ECHO = 1
    ;

    // Drive station USB ports
    public static final int
        LEFT_JOYSTICK  = 0,
        RIGHT_JOYSTICK = 1,
        XBOX_CONTROLLER = 2

    ;

    // solenoid ports
    public static final int
        CLIMB_CLUTCH_SOLENOID_TWO = 0 ,
        CLIMB_CLUTCH_SOLENOID_ONE = 1, 
        INTAKE_SOLENOID = 2 ,
        MANIPULATOR_SOLENOID = 3,
        CLIMB_BOOSTER_SOLENOID_ONE = 4,
        CLIMB_BOOSTER_SOLENOID_TWO = 5
    ;

}