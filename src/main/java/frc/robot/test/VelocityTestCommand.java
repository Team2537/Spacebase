/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.test;

import edu.wpi.first.wpilibj.command.Command;
import frc.lib.util.CSVLogger;
import frc.robot.Robot;

public class VelocityTestCommand extends Command {

    private CSVLogger loggerLeft, loggerRight;
    private long startTime, accumTime;
    private double voltageDesired;

    public VelocityTestCommand() {
        requires(Robot.driveSys);
    }

    @Override
    protected void initialize() {
        loggerLeft = new CSVLogger("Time", "VoltageDesired", "VoltageApplied", "Velocity", "Acceleration");
        loggerRight = new CSVLogger("Time", "VoltageDesired", "VoltageApplied", "Velocity", "Acceleration");
        voltageDesired = 0;
        startTime = System.currentTimeMillis();
        accumTime = startTime;
    }

    @Override
    protected void execute() {
        long time = System.currentTimeMillis(), timeElapsed = time - startTime;
        while (time - accumTime > 1000) {
            accumTime -= 1000;
            voltageDesired += 0.25;
            Robot.driveSys.setMotors(voltageDesired / 12.0, voltageDesired / 12.0);
        }

        final double velLeft = Robot.driveSys.getEncoderVelLeft(), velRight = Robot.driveSys.getEncoderVelRight();
        final double accLeft = Robot.driveSys.getEncoderAccLeft(), accRight = Robot.driveSys.getEncoderAccRight();
        final double appLeft = Robot.driveSys.getAppliedVoltageLeft(),
                appRight = Robot.driveSys.getAppliedVoltageRight();
        loggerLeft.appendRow(timeElapsed, voltageDesired, appLeft, velLeft, accLeft);
        loggerRight.appendRow(timeElapsed, voltageDesired, appRight, velRight, accRight);
    }

    @Override
    protected boolean isFinished() {
        return voltageDesired >= 12.0;
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