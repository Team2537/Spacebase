package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib.vision.Point;
import frc.lib.vision.Target;
import frc.lib.vision.VisionInput;
import frc.robot.Robot;

public class DriveTurnCommand extends Command{
//x coord
//thresohld on screen --> line
//lower or higher than certain pt can adjust wheels
//640 width, 480 height---300 lower, 340 higher threshold
    public static final int LOWERTHRESHOLD = 300;
    public static final int HIGHERTHRESHOLD = 340;
    public static final double TURNSPEED = 0.3;
    private Point MidPoint;
    private Target[] targets;

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
 
        for(int i=0;i < boundingBoxAreas.length;i++){ 
          if(boundingBoxAreas[i] > maxValue){ 
             maxValue = boundingBoxAreas[i]; 
             maxValueIndex = i;
          }
        }
        return maxValueIndex;
    }

    private static int findSecondLargestAreas(double[] boundingBoxAreas){
        double smallerMaxValue = boundingBoxAreas[0]; 
        int smallerValueIndex = 0;

        for(int i = 0;i < boundingBoxAreas.length;i++){ 
            if(boundingBoxAreas[i] == findLargestAreas(boundingBoxAreas)){
                System.out.println("bad");
            }
            else if(boundingBoxAreas[i] > smallerMaxValue){ 
             smallerMaxValue = boundingBoxAreas[i];
             smallerValueIndex = i; 
          }
        }
        return smallerValueIndex;
    }

    private static Point getMidpoint(int largeIndex, int LargishIndex, Target[] targets){
        Point box1 = new Point(0, 0);
        box1 = targets[largeIndex].getBoundingBoxCenter();
        Point box2 = new Point(0, 0);
        box2 = targets[LargishIndex].getBoundingBoxCenter();
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
            Robot.driveSys.setMotorsLeft(TURNSPEED*Math.signum(320-MidPoint.x));
            Robot.driveSys.setMotorsRight(-TURNSPEED*Math.signum(320-MidPoint.x));
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