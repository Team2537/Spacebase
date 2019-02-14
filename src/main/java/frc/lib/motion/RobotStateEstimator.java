package frc.lib.motion;

import frc.lib.util.Vec2;

public class RobotStateEstimator {
    private MotionProfile state;

    public RobotStateEstimator(RobotConstraints constraints, Vec2 start, double startAngle){
        state = new MotionProfile(MotionState.fromWheels(constraints,0, start,startAngle, 0,0,0,0));
    }

    public void update(double dt, double wheelDeltaLeft, double wheelDeltaRight, double angleDelta){
        if(dt > 0){
            final double vel = (wheelDeltaLeft + wheelDeltaRight)/(2*dt);
            final double angVel = angleDelta/dt;
            state.appendControlAngular(dt, (vel - state.endState().vel)/dt, (angVel - state.endState().angVel)/dt);
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