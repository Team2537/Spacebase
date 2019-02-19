/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.test;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.arm.ArmCommand;
import frc.robot.arm.SetArmLevelCommand;
import frc.robot.climb.ClimbCommand;
import frc.robot.drive.DriveTestCommand;
import frc.robot.intake.FlywheelCommand;
import frc.robot.intake.PneumaticExtendCommand;
import frc.robot.manipulator.ArmFlywheelCommand;
import frc.robot.manipulator.ArmPneumaticCommand;

public class PreLaunchSequence extends CommandGroup {
  /**
   * Add your docs here.
   */
  public PreLaunchSequence() {
    addSequential(new DriveTestCommand(0.5), 0.5);
    addSequential(new DriveTestCommand(-0.5), 0.5);

    addSequential(new SetArmLevelCommand(7)); // TODO Set this for the high forward level
    addSequential(new ArmCommand(), 5); //TODO modify all these times
    addSequential(new SetArmLevelCommand(1)); //TODO hatch level
    addSequential(new ArmCommand(), 5);

    addParallel(new PneumaticExtendCommand());
    addParallel(new FlywheelCommand(true));
    addParallel(new ArmPneumaticCommand());
    addSequential(new ArmFlywheelCommand(true), 2);

    addParallel(new PneumaticExtendCommand());
    addParallel(new ArmFlywheelCommand(false));
    addSequential(new FlywheelCommand(false), 2);

    addSequential(new SetArmLevelCommand(0)); // TODO in frame perimeter level
    addSequential(new ArmCommand(), 5);
    
    addSequential(new ClimbCommand());
    addSequential(new DriveTestCommand(-0.3), 2);
    addSequential(new ClimbCommand());

    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
