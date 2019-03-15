package frc.robot.drive;
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
    private NetworkTableEntry Compressor;

    public CustomDashboardCommand() {
        requires(Robot.driveSys);
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("database");
        PdpEntry1 = table.getEntry("Temperature");
        PdpEntry2 = table.getEntry("Current");
        UltraDistance = table.getEntry("Ultra");
        EncoderEntry = table.getEntry("Encoder");
        Compressor = table.getEntry("Compressor");
        
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
        //EncoderEntry.setDouble(Robot.driveSys.getEncoderPosRight());
        //Compressor.setBoolean(Robot.driveSys.getClutchSolenoid());


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
