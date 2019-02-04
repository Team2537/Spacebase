/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auto.test;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.CSVLogger;
import frc.robot.Robot;

public class AccelerationTestCommand extends Command {

  private CSVLogger loggerLeft, loggerRight;
  private long startTime, timeElapsed;
  private final double voltageDesired = 7.2;
  private final double kV = 2.0, vMin = 1.0;

  public AccelerationTestCommand() {
    requires(Robot.driveSys);
  }

  @Override
  protected void initialize() {
    loggerLeft = new CSVLogger("Time","VoltageDesired","VoltageApplied","VoltageAccel","Velocity","Acceleration");
    loggerRight = new CSVLogger("Time","VoltageDesired","VoltageApplied","VoltageAccel","Velocity","Acceleration");
    startTime = System.currentTimeMillis();

    Robot.driveSys.setMotors(voltageDesired/12.0, voltageDesired/12.0);
  }

  @Override
  protected void execute() {
    timeElapsed = System.currentTimeMillis() - startTime;

    final double velLeft = Robot.driveSys.getEncoderVelLeft(), velRight = Robot.driveSys.getEncoderVelRight();
    final double accLeft = Robot.driveSys.getEncoderAccLeft(), accRight = Robot.driveSys.getEncoderAccRight();
    final double appLeft = Robot.driveSys.getAppliedVoltageLeft(), appRight = Robot.driveSys.getAppliedVoltageRight();
    final double vlaLeft = appLeft - (kV*velLeft + vMin), vlaRight = appRight - (kV*velRight + vMin);
    loggerLeft.appendRow(timeElapsed,voltageDesired,appLeft,vlaLeft,velLeft,accLeft);
    loggerRight.appendRow(timeElapsed,voltageDesired,appRight,vlaRight,velRight,accRight);
  }

  @Override
  protected boolean isFinished() {
    return timeElapsed >= 3000;
  }

  @Override
  protected void end() {
    Robot.driveSys.setMotors(0, 0);
    System.out.println("*------- Left Logger -------*");
    System.out.println(loggerLeft);
    System.out.println("*------- Right Logger -------*");
    System.out.println(loggerRight);
  }

  @Override
  protected void interrupted() {
    end();
  }
}
