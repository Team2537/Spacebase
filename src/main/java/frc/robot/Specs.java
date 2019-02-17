package frc.robot;

import frc.lib.motion.DriveSpecs;

public class Specs {
    public static final double ROBOT_PERIOD_SECONDS = 0.02;
    public static final double JOYSTICK_DEADZONE = 0.1;
    public static final double MOTION_PROFILE_TIMESTEP_SECONDS = 0.01;

    // TODO: these values are temp
    public static final double DRIVE_VOLTAGE_MIN = 1.0;
    public static final double DRIVE_VOLTAGE_kV  = 1.0;
    public static final double DRIVE_VOLTAGE_kA  = 1.0;
    
    public static final double DRIVE_AXLE_LENGTH = 12; // inches
    public static final double DRIVE_WHEEL_DIAMETER = 6.0; // inches

    public static final DriveSpecs DRIVE_SPECS = new DriveSpecs(
        DRIVE_AXLE_LENGTH, DRIVE_WHEEL_DIAMETER,
        DRIVE_VOLTAGE_kV, DRIVE_VOLTAGE_kA, DRIVE_VOLTAGE_MIN
    );
    
}