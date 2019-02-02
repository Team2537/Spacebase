/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.drive.DriveStraightCommand;

public class AlignToHatchCommand extends CommandGroup {
  
  private boolean predictionDir;

  public AlignToHatchCommand() {

    addSequential(new Command(){  // get rotation direction prediction
      protected void initialize(){
        predictionDir = true; // TODO: implement
      }
      protected boolean isFinished() {
        return true;
      }
    });

    addSequential(new PerpendicularOnLineCommand(0.3, 0.15, predictionDir));
    addSequential(new RotateToCenterCommand(!predictionDir));
    /* addSequential(new VisionRotateCommand()); */
    addSequential(new UltrasonicFrontDriveCommand(6, 0.3));
  }
}
