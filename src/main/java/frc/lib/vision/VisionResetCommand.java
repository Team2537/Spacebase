package frc.lib.vision;

import edu.wpi.first.wpilibj.command.Command;

public class VisionResetCommand extends Command {
    public VisionResetCommand() {
    	requires(VisionInput.getInstance());
    }
    protected void initialize() {
    	VisionInput.getInstance().clearCache();
    }
    protected boolean isFinished() {
        return true;
    }
}