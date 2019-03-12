package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.PID;
import frc.lib.util.Util;
import frc.lib.vision.Point;
import frc.lib.vision.Target;
import frc.robot.Robot;
//MAKE SURE DRIVER IS LEFT OF TARGET
/* Uses input recieved by Target Class from Raspberry Pi to turn wheels towards midpoint of targets
   Raspberry Pi outputs vertices of all objects it can detect
   Target Class files these vertices of each detected object into an array
   Target Class finds the center of each object
   DriveTurnCommand calls upon Target index and finds the two largest objects in the indexes and 
   then uses the center of each object to find the center between the two objects
   DriveTurnCommand sets motors in the direction that will direct robot to midpoint
   DriveTurnCommand has a tolerance range from 300-340; anywhere within that range, 
   robot will think it's in center and motors will stop

*/

public class VisionTurnCommand extends Command{

// pi CAM should be at least 8 inches above ground! hatch is 31.5 inches off ground
//WRITE RANGE - 2560 x 1920 4:3; 17 feet plus; 204 inches+ 

//THRESHOLD:
    //lower or higher than certain pt can adjust wheels
    //300 lower threshold, 340 higher threshold; tolerance

/////////PLUG THE DAMN PI INTO RX AND GROUND. RX. NOT TX. RX. RX
//RRRRRRRRRRRRRRRRRRRXXXXXXXXXXXXXXXXXXXXXX

//CAM dimensions: 640 width, 480 height

    public static final double THRESHOLD = 20;
   
    private double MaxVelocity = 0.2;
    private PID pid;
   
    private Point MidPoint;
    private Target[] targets;
    private double half = 320; //midpoint of screen

    public VisionTurnCommand(){
        requires(Robot.driveSys);  
        pid = new PID(0.025, 0.0, 0.0, THRESHOLD);
        pid.setSetpoint(half);
    }

    @Override
    protected void initialize(){
        System.out.println("VISION STARTS!!! "); 
    }

    @Override
    protected void execute(){
        targets = Robot.visionInput.getVisionPacket();
        MidPoint = Target.getMidpoint(targets);
        
        System.out.println("MIDPOINT: "+ MidPoint);
            
        pid.update(MidPoint.x);
        double percentOutput = MaxVelocity*Util.clamp(pid.getOutput(), -1, 1);
        Robot.driveSys.setMotors(-percentOutput, percentOutput);
    }
    

    public boolean isFinished(){
        return pid.withinTolerance();
    }

    public void end(){
        System.out.println("VISIONTURN is DONE!!");
        Robot.driveSys.setMotors(0, 0);
    }


}