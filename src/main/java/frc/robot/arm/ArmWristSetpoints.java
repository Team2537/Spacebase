package frc.robot.arm;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.manipulator.ManipulatorSubsystem.PlacementMode;

public class ArmWristSetpoints {

    public static final ArmSetpoint        
        SETPOINT_DEFAULT = new ArmSetpoint(0, 258, "FRAME PERIMETER"),

        //SETPOINT_INTAKE = new ArmSetpoint(486, 503, "INTAKE"),

        SETPOINT_SHIP_HATCH = new ArmSetpoint(9, 249, "SHIP HATCH"),
        SETPOINT_ROCKET_HATCH_2 = new ArmSetpoint(68, 167, "MID ROCKET HATCH"),
        SETPOINT_ROCKET_HATCH_3 = new ArmSetpoint(115, 99, "HIGH ROCKET HATCH"),

        SETPOINT_SHIP_CARGO = new ArmSetpoint(80, 103, "SHIP CARGO"),
        SETPOINT_ROCKET_CARGO_1 = new ArmSetpoint(39, 209, "LOW ROCKET CARGO"),
        SETPOINT_ROCKET_CARGO_2 = new ArmSetpoint(85, 148, "MID ROCKET CARGO"),
        SETPOINT_ROCKET_CARGO_3 = new ArmSetpoint(139, 85, "HIGH ROCKET CARGO")
    ;

    public static final ArmSetpoint[] HATCH_SETPOINTS_LEVELS = {
        SETPOINT_DEFAULT,            // frame perimeter
        SETPOINT_SHIP_HATCH,
        SETPOINT_ROCKET_HATCH_2,
        SETPOINT_ROCKET_HATCH_3,
    };

    public static final ArmSetpoint[] CARGO_SETPOINTS_LEVELS = {
        SETPOINT_DEFAULT,
        SETPOINT_ROCKET_CARGO_1,
        SETPOINT_SHIP_CARGO,
        SETPOINT_ROCKET_CARGO_2,
        SETPOINT_ROCKET_CARGO_3
    };


    private int levelIndex;

    public ArmWristSetpoints(){
        levelIndex = 0;
    }

    private ArmSetpoint[] getCurrentSetpointArray(){
        if(Robot.manipSys.getPlacementMode() == PlacementMode.HATCH){
            return HATCH_SETPOINTS_LEVELS;
        } else {
            return CARGO_SETPOINTS_LEVELS;
        }
    }

    public void setLevelIndex(int index){
        ArmSetpoint[] setpoints = getCurrentSetpointArray();

        if(index > setpoints.length-1){
            index = setpoints.length-1;
        }
        if(index < 0){
            index = 0;
        }
        levelIndex = index;

        final ArmSetpoint currentSetpoint = getCurrentLevel();
        final String name = currentSetpoint == null ? "NONE" : currentSetpoint.name;
        SmartDashboard.putString("Arm Level", name);
    }

    public void increaseLevelIndex(){
        setLevelIndex(levelIndex+1);
    }

    public void decreaseLevelIndex(){
        setLevelIndex(levelIndex-1);
    }

    public ArmSetpoint getCurrentLevel(){
        return getCurrentSetpointArray()[levelIndex];
    }

    public int getHighestLevelIndex(){
        return getCurrentSetpointArray().length - 1;
    }

    public int getCurrentLevelIndex(){
        return levelIndex;
    }


    public static class ArmSetpoint {
        public final double arm, wrist;
        public final String name;
        public ArmSetpoint(double arm, double wrist, String name) {
            this.arm = arm;
            this.wrist = wrist;
            this.name = name;
        }
        public String toString(){
            return name+" -> Arm: "+arm+", Wrist: "+wrist;
        }
    }
    
}