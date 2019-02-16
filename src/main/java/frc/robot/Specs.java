package frc.robot;

import frc.lib.motion.RobotConstraints;

public class Specs {
    public static final double ROBOT_PERIOD_SECONDS = 0.02;
    public static final double JOYSTICK_DEADZONE = 0.1;
    public static final double MOTION_PROFILE_TIMESTEP_SECONDS = 0.01;
    public static final RobotConstraints CONSTRAINTS = new RobotConstraints(
        30, // Maximum velocity (in/2) [[TEMP]]
        15, // Maximum acceleration (in/s/s) [[TEMP]]
        12  // Drive axle length (in) [[TEMP]]
    );
    
    public static final double DRIVE_WHEEL_DIAMETER = 6.0;
}