package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib.vision.Point;
import frc.lib.vision.Target;
import frc.lib.vision.VisionInput;
import frc.robot.Robot;

public class DriveTurnCommand extends Command{

// pi CAM should be at least 8 inches above ground! hatch is 31.5 inches off ground
//WRITE RANGE- 2560 x 1920 4:3; 17 feet plus; 204 inches+ 

//THRESHOLD:
    //lower or higher than certain pt can adjust wheels
    //300 lower threshold, 340 higher threshold

//CAM dimensions: 640 width, 480 height

    public static final int LOWERTHRESHOLD = 300;
    public static final int HIGHERTHRESHOLD = 340;
    public static final double TURNSPEED = 0.4;
    private Point MidPoint;
    private Target[] targets;
    private double half = 320; // midpoint of screen

    @Override
    protected void initialize(){
        requires(VisionInput.getInstance());
        requires(Robot.driveSys);        
    }

    @Override
    protected void execute(){
        targets = VisionInput.getInstance().getVisionPacket();
        MidPoint = Target.getMidpoint(targets);
        
        System.out.println("MIDPOINT: "+ MidPoint);
        
        if(MidPoint.x > HIGHERTHRESHOLD || MidPoint.x < LOWERTHRESHOLD){
           // Robot.driveSys.setMotorsLeft(TURNSPEED*(Math.signum(320-MidPoint.x)));

           // if(MidPoint.x > 200 && MidPoint.x < 440) { //when within range of 220 pixels, robot slows down for accuracy
                
            //slows down proportionally to distance from midpoint
                if(MidPoint.x < half) { // if midpoint is too far left
                    Robot.driveSys.setMotorsLeft(TURNSPEED*((half-MidPoint.x)/half)); //320 - midpoint = distance from middle of screen; 320 is half of full range [0-640)]
                    Robot.driveSys.setMotorsRight(-TURNSPEED*((half-MidPoint.x)/half)); 
                }
            
                //slows down proportionally to distance from midpoint
                if(MidPoint.x > half) { // if midpoint is too far right
                    Robot.driveSys.setMotorsLeft(-TURNSPEED*((MidPoint.x-half)/half));
                    Robot.driveSys.setMotorsRight(TURNSPEED*((MidPoint.x-half)/half)); 
                }
            //}
           // else{
             //   Robot.driveSys.setMotorsLeft(TURNSPEED);
             //   Robot.driveSys.setMotorsRight(TURNSPEED);
        
            //Robot.driveSys.setMotorsLeft(TURNSPEED*z);
            //midpoint from middle of screen, smaller distance, smaller the speed
            //Robot.driveSys.setMotorsRight(TURNSPEED*Math.signum(320-MidPoint.x));
            //Robot.driveSys.setMotorsRight(TURNSPEED*z);
           
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