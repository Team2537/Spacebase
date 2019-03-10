/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.cameras;



import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * Add your docs here.
 */
public class Cameras /*extends Thread*/{
    private UsbCamera cam0;
    private VideoSink server;
    //private boolean camBoolean;
    //private long lastSwitched;


    public Cameras(){
        cam0 = CameraServer.getInstance().startAutomaticCapture(0);
        server = CameraServer.getInstance().getServer();
        server.setSource(cam0);
        cam0.setResolution(160, 120);

        //camBoolean = false;
        //lastSwitched = 0;

    }

    /*
    @Override
    public void run(){
        
        while(true){
            if(Robot.input.cameraButton.get() && System.currentTimeMillis() > lastSwitched + 200){
                if(camBoolean){
                    server.setSource(cam1);
                    camBoolean = !camBoolean;
                    lastSwitched = System.currentTimeMillis();
				
                } else if(!camBoolean){
                    server.setSource(cam0);
                    camBoolean = !camBoolean;
                    lastSwitched = System.currentTimeMillis();
                }
            }
        }
        
    }*/

}
