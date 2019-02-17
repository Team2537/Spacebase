package frc.lib.motion;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class RobotStateEstimator {
    private final MotionProfile state;
    private final NetworkTable table;
    public static final String TABLE_NAME = "RobotStateEstimator";
    public static final String ENTRY_STATE = "state", ENTRY_SPECS = "specs";

    public RobotStateEstimator(DriveSpecs drive, Pose2d start){
        state = new MotionProfile(drive, new MotionState(start));
        table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
        table.getEntry(ENTRY_SPECS).setDoubleArray(drive.encode());
    }

    public void update(double dt, double wheelDeltaLeft, double wheelDeltaRight, double angleDelta){
        if(dt > 0){
            final double wheelDelta = (wheelDeltaLeft + wheelDeltaRight)/2;
            state.appendDeltas(dt, new ChassisState(wheelDelta, angleDelta));
            table.getEntry(ENTRY_STATE).setDoubleArray(getLatestState().encode());
        }
    }

    public MotionState getStartState(){
        return state.startState();
    }

    public MotionState getLatestState(){
        return state.endState();
    }

    public MotionState getState(double t){
        return state.getState(t);
    }
}