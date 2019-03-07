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

    public CustomDashboardCommand() {
        requires(Robot.driveSys);
        
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("database");
        PdpEntry1 = table.getEntry("Temperature");
        PdpEntry2 = table.getEntry("Current");
        UltraDistance = table.getEntry("Ultra");


        
    }
    @Override
    protected void initialize() {
        System.out.print("CustomDashboardCommand starts!");
    }
  
    @Override
      protected void execute(){
        PdpEntry1.setDouble(Robot.driveSys.getTemperature());
        PdpEntry2.setDouble(Robot.driveSys.getCurrent());
        UltraDistance.setDouble(Robot.driveSys.getUltrasonic());


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
