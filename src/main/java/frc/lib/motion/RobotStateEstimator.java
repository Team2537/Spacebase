package frc.lib.motion;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.lib.util.Vec2;

public class RobotStateEstimator {
    private final MotionProfile state;
    private final NetworkTable table;
    public static final String TABLE_NAME = "RobotStateEstimator", ENTRY_POS = "pos";

    public RobotStateEstimator(RobotConstraints constraints, Vec2 start, double startAngle){
        state = new MotionProfile(MotionState.fromWheels(constraints,0, start,startAngle, 0,0,0,0));
        table = NetworkTableInstance.getDefault().getTable(TABLE_NAME);
    }

    public void update(double dt, double wheelDeltaLeft, double wheelDeltaRight, double angleDelta){
        if(dt > 0){
            final double vel = (wheelDeltaLeft + wheelDeltaRight)/(2*dt);
            final double angVel = angleDelta/dt;
            state.appendControlAngular(dt, (vel - getLatestState().vel)/dt, (angVel - getLatestState().angVel)/dt);
            table.getEntry(ENTRY_POS).setValue(getLatestState().pos);
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