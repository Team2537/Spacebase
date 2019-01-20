package frc.lib.vision;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionTestCommand extends Command {

	public VisionTestCommand(){
		requires(VisionInput.getInstance());
	}

	@Override
	protected void execute(){
		Target[] targets = VisionInput.getInstance().getVisionPacket();
		
		String targetsString = "";
		for(Target target : targets){
			Point[] bb = target.getBoundingBox();
			targetsString += bb[0].x+","+bb[0].y+"|"+bb[1].x+","+bb[1].y+" ";
		}
		SmartDashboard.putString("targets", targetsString);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
