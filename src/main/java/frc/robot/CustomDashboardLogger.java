package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.vision.Target;
import frc.robot.Robot;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class CustomDashboardLogger {

    public void log() {
        SmartDashboard.putNumber("Encoders [Left]", Robot.driveSys.getEncoderPosLeft());
        SmartDashboard.putNumber("Encoders [Right]", Robot.driveSys.getEncoderPosRight());
        SmartDashboard.putBoolean("Clutch Compressor", Robot.climbSys.getClutchSolenoid());
        SmartDashboard.putBoolean("Boost Compressor", Robot.climbSys.getBoosterSolenoid());
        SmartDashboard.putString("Arm/Wrist Level", Robot.awSetpoints.getCurrentLevel().toString());

        SmartDashboard.putNumber("Arm Potentiometer", Robot.armSys.getPotentiometer());
        SmartDashboard.putNumber("Wrist Potentiometer", Robot.wristSys.getPotentiometer());
        SmartDashboard.putNumber("Ultrasonic", Robot.driveSys.getUltrasonic());
        SmartDashboard.putNumber("Vision Midpoint", Target.getMidpoint(Robot.visionInput.getVisionPacket()).x);

        SmartDashboard.putString("Arm On Target?", Robot.armSys.onTarget() ? "YES" : "NO");
        SmartDashboard.putString("Wrist On Target?", Robot.wristSys.onTarget() ? "YES" : "NO");
        SmartDashboard.putString("Placement Mode", 
            Robot.manipSys.getPlacementMode() == PlacementMode.CARGO ? "Cargo" : "Hatch"
        );
        SmartDashboard.putString("Drive Precision Mode",
            Robot.driveSys.getDrivePrecision() ? "Precision ON" : "Default"
        );
        SmartDashboard.putData("Drive Train", Robot.driveSys.getDriveTrain());
        SmartDashboard.putData("Arm PID", Robot.armSys);
        SmartDashboard.putData("Wrist PID", Robot.wristSys);
    }

}
