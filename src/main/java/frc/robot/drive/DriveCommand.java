/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.drive;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DriveCommand extends Command {
  private String filename;
  private Path dataPath;
  private PrintWriter writer;
  private long startTime;


  public DriveCommand() {
    requires(Robot.drivesys);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("we rollin");

    filename = "/home/lvuser/driverstats"
        + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".csv";
    dataPath = Paths.get(filename);
    if (Files.exists(dataPath)) {
          System.out.println("File " + dataPath + " already exists. It shouldn't.");
        }
    try {
          Files.createFile(dataPath);
          writer = new PrintWriter(filename);
     } catch (IOException e) {
          e.printStackTrace();
        }
        writer.println("Time (ms),Current (amps)");
        startTime = System.currentTimeMillis();
        writer.println(System.currentTimeMillis() - startTime + "," + Robot.pdp.getCurrent(12));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.drivesys.setMotors(Robot.drivesys.getLeftJoystick(), Robot.drivesys.getRightJoystick());
    System.out.println(Robot.drivesys.encoderStatus());

    writer.println(System.currentTimeMillis() - startTime + "," + Robot.pdp.getCurrent(12));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
