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

public class AlignToHatchCommand extends CommandGroup {
  
  private boolean predictionDir;

  public AlignToHatchCommand() {

    addSequential(new Command(){  // get rotation direction prediction
      protected void initialize(){
        Target[] targets = Robot.visionInput.getVisionPacket();
        Point midpoint = Target.getMidpoint(targets);
        predictionDir = midpoint.x < Point.CAMERA_WIDTH/2;
      }
      protected boolean isFinished() {
        return true;
      }
    });

    addSequential(new PerpendicularOnLineCommand(0.3, 0.15, predictionDir));
    addSequential(new RotateToCenterCommand(!predictionDir, 0.15));
    addSequential(new UltrasonicFrontDriveCommand(6, 0.3));
  }
}
