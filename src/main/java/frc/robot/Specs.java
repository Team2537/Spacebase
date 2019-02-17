package frc.robot;

import frc.lib.motion.RobotConstraints;

public class Specs {
    public static final double ROBOT_PERIOD_SECONDS = 0.02;
    public static final double JOYSTICK_DEADZONE = 0.1;
    public static final double MOTION_PROFILE_TIMESTEP_SECONDS = 0.01;

    // TODO: these values are temp
    public static final double DRIVE_VOLTAGE_MIN = 1,  DRIVE_VOLTAGE_kV = 1, DRIVE_VOLTAGE_kA = 1;
    public static final double DRIVE_MAX_VELOCITY = 30;     // inches/second
    public static final double DRIVE_MAX_ACCELERATION = 15; // inches/second^2
    public static final double DRIVE_AXLE_LENGTH = 12;      // inches
    public static final double DRIVE_WHEEL_DIAMETER = 6.0;

    public static final RobotConstraints CONSTRAINTS = new RobotConstraints(
        DRIVE_MAX_VELOCITY, DRIVE_MAX_ACCELERATION, DRIVE_AXLE_LENGTH
    );
    
}