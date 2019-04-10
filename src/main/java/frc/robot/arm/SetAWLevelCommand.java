package frc.robot.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.Robot;
import frc.robot.arm.ArmWristSetpoints.ArmSetpoint;

public class SetAWLevelCommand extends CommandGroup {

    private int index;
    private AWLevelMode mode;
    private SetArmSetpointCommand armCommand;
    private SetWristSetpointCommand wristCommand;

    public SetAWLevelCommand(AWLevelMode mode){
        this.mode = mode;
        this.armCommand = new SetArmSetpointCommand();
        this.wristCommand = new SetWristSetpointCommand();

        addParallel(armCommand);
        addSequential(wristCommand);
    }

    public SetAWLevelCommand(int levelIndex){
        this(AWLevelMode.DEFAULT);
        this.index = levelIndex;
    }

    @Override
    protected void initialize(){
        if(mode == AWLevelMode.LOWEST){
            index = 0;
        } else if(mode == AWLevelMode.HIGHEST){
            index = Robot.awSetpoints.getHighestLevelIndex();
        } else if(mode == AWLevelMode.INDEX1){ //TEMPORARY
            index = 1;
        } else if(mode == AWLevelMode.INDEX2){
            index = 2;
        } else if(mode == AWLevelMode.INDEX3){
            index = 3;
        }
        
        Robot.awSetpoints.setLevelIndex(index);
        final ArmSetpoint currentSetpoint = Robot.awSetpoints.getCurrentLevel();
        armCommand.updateSetpoint(currentSetpoint.arm);
        wristCommand.updateSetpoint(currentSetpoint.wrist);
    }

    public static enum AWLevelMode {
        DEFAULT, LOWEST, HIGHEST, INDEX1, INDEX2, INDEX3;
    }

}