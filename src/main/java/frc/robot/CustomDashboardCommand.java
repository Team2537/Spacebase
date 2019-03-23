package frc.robot;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.Robot;

public class CustomDashboardCommand extends Command {

    private NetworkTableInstance inst;
    private NetworkTable table;
    private NetworkTableEntry PdpEntry1;
    private NetworkTableEntry PdpEntry2;
    private NetworkTableEntry UltraDistance;
    private NetworkTableEntry EncoderEntry;
    private NetworkTableEntry ClutchCompressor;
    private NetworkTableEntry BoostCompressor;
    private NetworkTableEntry ArmPot;
    private NetworkTableEntry WristPot;
    private NetworkTableEntry ArmAmp;
    private NetworkTableEntry WristAmp;
    private NetworkTableEntry IntakeAmp;

    public CustomDashboardCommand() {
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
        
        
    }
    @Override
    protected void initialize() {
        System.out.println("CustomDashboardCommand starts!");
        
    }
  
    @Override
      protected void execute(){
        System.out.println("CustomDashboardCommand RUNNING");
        System.out.println(Robot.driveSys.getTemperature());
        PdpEntry1.setDouble(Robot.driveSys.getTemperature());
        PdpEntry2.setDouble(Robot.driveSys.getCurrent());
        UltraDistance.setDouble(Robot.driveSys.getUltrasonic());
        EncoderEntry.setDouble(Robot.driveSys.getEncoderPosRight());
        ClutchCompressor.setBoolean(Robot.climbSys.getClutchSolenoid());
        BoostCompressor.setBoolean(Robot.climbSys.getBoosterSolenoid());
        ArmPot.setDouble(Robot.armSys.getArmPotentiometer());
        WristPot.setDouble(Robot.armSys.getWristPotentiometer());
        ArmAmp.setDouble(Robot.armSys.getArmAmperage());
        WristAmp.setDouble(Robot.armSys.getWristAmperage());
        IntakeAmp.setDouble(Robot.intakeSys.getIntakeAmperage());

      }
       
  
    @Override
    protected boolean isFinished() {
      return false;
    }
  
    @Override
    protected void end() {
      
    }
  
    @Override
    protected void interrupted() {

    }
  }
