package frc.robot;

public class Ports {
    // CAN Bus ports
    public static final int
        DRIVE_MOTOR_RIGHT_BACK  = 0,
        DRIVE_MOTOR_RIGHT_TOP   = 1,
        DRIVE_MOTOR_RIGHT_FRONT = 2,
        DRIVE_MOTOR_LEFT_FRONT  = 3,
        DRIVE_MOTOR_LEFT_TOP    = 4,
        DRIVE_MOTOR_LEFT_BACK   = 5
    ;

    // Drive station USB ports
    public static final int
        LEFT_JOYSTICK  = 0,
        RIGHT_JOYSTICK = 1
    ;

    // flywheel ports for intake
    public static final int
        INTAKE_MOTOR_ONE = 0,
        INTAKE_MOTOR_TWO = 1
    ;
        
    // infrared sensor ports for intake
    public static final int
        INTAKE_INFRARED = 0
    ;

    // solenoid ports
    public static final int
        INTAKE_PNEUMATIC_ONE = 0 ,
        INTAKE_PNEUMATIC_TWO = 1
    ;

    //buttons for intake
    public static final int
        INTAKE_ON = 0,
        PNEUMATIC_EXTEND = 1,
        PNEUMATIC_RETRACT = 2
    ;


}