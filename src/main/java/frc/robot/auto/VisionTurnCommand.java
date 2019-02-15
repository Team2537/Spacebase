package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
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

//CAM dimensions: 640 width, 480 height

    public static final int LOWERTHRESHOLD = 250; 
    public static final int HIGHERTHRESHOLD = 390;
    public static final double TURNSPEED = 0.2;
    private Point MidPoint;
    private Target[] targets;
    private double half = 320; //midpoint of screen

    @Override
    protected void initialize(){
        requires(Robot.driveSys);        
    }

    @Override
    protected void execute(){
        targets = Robot.visionInput.getVisionPacket();
        MidPoint = Target.getMidpoint(targets);
        
        System.out.println("MIDPOINT: "+ MidPoint);
        
        if(MidPoint.x > HIGHERTHRESHOLD || MidPoint.x < LOWERTHRESHOLD){
           // Robot.driveSys.setMotorsLeft(TURNSPEED*(Math.signum(320-MidPoint.x)));
           if(MidPoint.x > 100 && MidPoint.x < 520) {
               System.out.println("INNER THRESHOLD!!");
                //slows down proportionally to distance from midpoint
                if(MidPoint.x < half) { // if midpoint is too far left
                    Robot.driveSys.setMotorsLeft(TURNSPEED*((half-MidPoint.x)/half)); //320 - midpoint = distance from middle of screen; 320 is half of full range [0-640)]
                    Robot.driveSys.setMotorsRight(TURNSPEED*((half-MidPoint.x)/half)); 
                }

                //slows down proportionally to distance from midpoint
                if(MidPoint.x > half) { // if midpoint is too far right
                    Robot.driveSys.setMotorsLeft(-TURNSPEED*((MidPoint.x-half)/half));
                    Robot.driveSys.setMotorsRight(-TURNSPEED*((MidPoint.x-half)/half)); 
                }

                //Robot.driveSys.setMotorsRight(TURNSPEED*Math.signum(320-MidPoint.x));
            }
            
            else{
                Robot.driveSys.setMotorsLeft(TURNSPEED);
                Robot.driveSys.setMotorsRight(TURNSPEED);
                
            }
        }
        else{
            Robot.driveSys.setMotors(0, 0);
        }
    }
    

    public boolean isFinished(){
        //return (MidPoint.x < HIGHERTHRESHOLD & MidPoint.x > LOWERTHRESHOLD);
        return false;
    }

    public void end(){

    }


}