/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.lib.vision.Point;
import frc.lib.vision.Target;
import frc.robot.Robot;
import frc.robot.drive.DriveStraightCommand;

/* CommandGroup to ensure that robot follows line

Retrieves vertices from Target Class detected by Raspberry Pi to find midpoint between detected objects
Uses Target class to find midpoint between two largest targets
Midpoint then used to determine predictionDir

Calls in PerpendicularOnLineCommand which uses predictionDir to run until IR Sensors find the line 
or if line cannot be found

RotateToCenterCommand which direction is based off of our initial prediction, 
in short it just tells it which direction the bot needs to turn to realign facing forward

UltrasonicFrontDriverCommand which drives forward until robot is close enough to object

*/

public class AlignToHatchCommand extends CommandGroup {
  
  private boolean predictionDir;

  public AlignToHatchCommand() {

    addSequential(new Command(){  // get rotation direction prediction
      protected void initialize(){
        System.out.println("the first part started !!!!!!!!!!!!!");
        Target[] targets = Robot.visionInput.getVisionPacket();
        Point midpoint = Target.getMidpoint(targets);
        predictionDir = midpoint.x < Point.CAMERA_WIDTH/2; //true is when target is to the right
        
      }
      protected boolean isFinished() {
        return true;
      }
      protected void end(){
        System.out.println("the first part finished");
      }
    });

    addSequential(new PerpendicularOnLineCommand(0.15, 0.10, predictionDir));
    addSequential(new RotateToCenterCommand(!predictionDir, 0.15));
    //addSequential(new UltrasonicFrontDriveCommand(6, 0.3));
  }
}
