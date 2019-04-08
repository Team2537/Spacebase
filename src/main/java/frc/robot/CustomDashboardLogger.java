package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.Robot;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class CustomDashboardLogger {
    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry PdpEntry1, PdpEntry2, UltraDistance, EncoderEntry, ClutchCompressor, BoostCompressor,
            ArmPot, WristPot, ArmAmp, WristAmp, IntakeAmp, ArmSetpoint, VisionInput, LeftSpeed, RightSpeed;

    public CustomDashboardLogger() {
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("database");
        PdpEntry1 = table.getEntry("Temperature");
        PdpEntry2 = table.getEntry("Current");
        UltraDistance = table.getEntry("Ultra");
        EncoderEntry = table.getEntry("Encoder");
        ClutchCompressor = table.getEntry("ClutchCompressor");
        BoostCompressor = table.getEntry("BoostCompressor");
        ArmPot = table.getEntry("ArmPot");
        WristPot = table.getEntry("WristPot");
        ArmAmp = table.getEntry("ArmAmp");
        WristAmp = table.getEntry("WristAmp");
        IntakeAmp = table.getEntry("IntakeAmp");
        ArmSetpoint = table.getEntry("ArmSetpoint");
        VisionInput = table.getEntry("VisionInput");
        LeftSpeed = table.getEntry("LeftSpeed");
        RightSpeed = table.getEntry("RightSpeed");

    }

    public void log() {
        UltraDistance.setDouble(Robot.driveSys.getUltrasonic());
        EncoderEntry.setDouble(Robot.driveSys.getEncoderPosRight());
        ClutchCompressor.setBoolean(Robot.climbSys.getClutchSolenoid());
        BoostCompressor.setBoolean(Robot.climbSys.getBoosterSolenoid());
        ArmPot.setDouble(Robot.armSys.getPotentiometer());
        WristPot.setDouble(Robot.wristSys.getPotentiometer());
        ArmSetpoint.setString(Robot.awSetpoints.getCurrentLevel().toString());
        // VisionInput.setDouble(Target.getMidpoint(Robot.visionInput.getVisionPacket()).x);

        SmartDashboard.putString("Arm On Target?", Robot.armSys.onTarget() ? "YES" : "NO");
        SmartDashboard.putString("Wrist On Target?", Robot.wristSys.onTarget() ? "YES" : "NO");
        SmartDashboard.putString("Placement Mode", 
            Robot.manipSys.getPlacementMode() == PlacementMode.CARGO ? "Cargo" : "Hatch"
        );
        SmartDashboard.putString("Drive Precision Mode",
            Robot.driveSys.getDrivePrecision() ? "Precision ON" : "Default"
        );
        SmartDashboard.putData("Drive Train", Robot.driveSys.getDriveTrain());
    }

}
