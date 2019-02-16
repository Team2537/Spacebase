package frc.lib.motion;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.lib.util.Turtle;
import frc.lib.util.Vec2;

import java.awt.Color;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;

public class RobotStateViewer {
    public static final int TEAM_NUMBER = 2537;
    public static void main(String[] args) throws InterruptedException {
        new RobotStateViewer().run();
    }

    private void run() throws InterruptedException {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable(RobotStateEstimator.TABLE_NAME);
        inst.startClientTeam(TEAM_NUMBER); 
        inst.startDSClient();  // this gets the robot IP from the DS

        Turtle turtle = new Turtle(800, 800, 12);
        turtle.addPoints(new Vec2[]{}, new Color(100,210,60)); // green
        
        table.addEntryListener(RobotStateEstimator.ENTRY_STATE, (table_, key, entry, value, flags) -> {
            MotionState state = MotionState.decode(value.getDoubleArray());
            turtle.drawState(state);
            turtle.appendPoints(new Vec2[]{state.pos});
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

}