package frc.lib.motion;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.lib.util.Vec2;

public class RobotStateEstimator {
    private final MotionProfile state;
    private final NetworkTable table;
    public static final String TABLE_NAME = "RobotStateEstimator", ENTRY_STATE = "state";

    public RobotStateEstimator(RobotConstraints constraints, Vec2 start, double startAngle){
        state = new MotionProfile(MotionState.fromWheels(constraints,0, start,startAngle, 0,0,0,0));
        table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
    }

    public void update(double dt, double wheelDeltaLeft, double wheelDeltaRight, double angleDelta){
        if(dt > 0){
            final double wheelDelta = (wheelDeltaLeft + wheelDeltaRight)/2;
            state.appendDeltas(dt, wheelDelta, angleDelta);
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