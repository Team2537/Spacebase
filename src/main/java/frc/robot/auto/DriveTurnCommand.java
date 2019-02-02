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

    //grabs targets found in target class
    private static double[] getAreas(Target[] targets){
        double[] boundingBoxAreas = new double[targets.length];

       for(int i = 0; i < boundingBoxAreas.length; i++){
           System.out.println(boundingBoxAreas[i]);
           boundingBoxAreas[i] = targets[i].getBoundingBoxArea();   
       }
       return boundingBoxAreas;
    }

    private static int findLargestAreas(double[] boundingBoxAreas){
        double maxValue = boundingBoxAreas[0]; 
        int maxValueIndex = 0;
 
        for(int i = 0; i < boundingBoxAreas.length; i++){ 
          if(boundingBoxAreas[i] > maxValue){ 
             maxValue = boundingBoxAreas[i]; 
             maxValueIndex = i;
          }
        }
        System.out.println("MIDPOINT1 from Area: " + maxValue);
        return maxValueIndex;
    }

    private static int findSecondLargestAreas(double[] boundingBoxAreas){
        double first, second;
        int smallerValueIndex = 0;
        first = second = Double.MIN_VALUE;
        for ( int i = 0; i < boundingBoxAreas.length; i++) {
            if (boundingBoxAreas[i] > first) {
                second = first;
                first = boundingBoxAreas[i];
            }
            // if current element is between first and second, update second to store value of current variable
            else if (boundingBoxAreas[i] > second && boundingBoxAreas[i] != first){
                second = boundingBoxAreas[i];
                smallerValueIndex = i;
            }
        }
        System.out.println("MIDPOINT2 from Area2: " + second);
        return smallerValueIndex;
    }


    private static Point getMidpoint(int largeIndex, int LargishIndex, Target[] targets){
        Point box1 = new Point(0, 0);
        box1 = targets[largeIndex].getBoundingBoxCenter();
        System.out.println("MDPOINT1 from getMidpoint: " + box1);
        Point box2 = new Point(0, 0);
        box2 = targets[LargishIndex].getBoundingBoxCenter();
        System.out.println("MDPOINT2 from getMidpoint: " + box2);
        Point MidPoint = new Point((box1.x + box2.x)/2, (box1.y + box2.y)/2);
        return MidPoint;
    }



    @Override
    protected void initialize(){
        requires(VisionInput.getInstance());
        requires(Robot.driveSys);        
    }

    @Override
    protected void execute(){
        targets = VisionInput.getInstance().getVisionPacket();
        if(targets.length > 1){
            MidPoint = getMidpoint(
                findLargestAreas(getAreas(targets)),
                findSecondLargestAreas(getAreas(targets)),
                targets);
        } else {
            MidPoint = new Point(0, 0);
        }
        
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