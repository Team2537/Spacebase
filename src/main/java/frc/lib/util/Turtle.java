package frc.lib.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import frc.lib.motion.DriveSpecs;
import frc.lib.motion.MotionProfile;
import frc.lib.motion.MotionState;
 
public class Turtle {
    private DrawPanel panel;
    public Turtle(int width, int height, double scale, Vec2 center) {  
        JFrame f = new JFrame("Turtle");    
        panel = new DrawPanel(width,height,scale,center);
        f.add(panel);
        f.pack();    
        f.setVisible(true);
    }

    public Turtle(int width, int height, double scale){
        this(width, height, scale, new Vec2(0,0));
    }

    public void addMouseAdapter(MouseAdapter listener){
        panel.addMouseListener(listener);
        panel.addMouseMotionListener(listener);
    }

    public void addPoints(Vec2[] points, Color color){
        panel.addPoints(points, color);
    }

    public void addPoints(List<Vec2> points, Color color){
        Vec2[] arr = new Vec2[points.size()];
        for(int i = 0; i < arr.length; i++){
            arr[i] = points.get(i);
        }
        addPoints(arr,color);
    }

    public void appendPoints(Vec2[] points){
        panel.appendPoints(points);
    }

    public void appendPoints(Vec2[] points, int index){
        panel.appendPoints(points, index);
    }

    public void clearPoints(){
        panel.clearPoints();
    }

    public void clearPoints(Color color){
        panel.clearPoints(color);
    }

    public void animateProfile(MotionProfile profile){
        panel.animateProfile(profile);
    }

    public void drawState(DriveSpecs drive, MotionState state){
        panel.drawState(drive, state);
    }

    public Vec2 toPanelSpace(Vec2 v){
        return panel.toPanelSpace(v);
    }

    public Vec2 toWorldSpace(Vec2 v){
        return panel.toWorldSpace(v);
    }

    private static class DrawPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private List<List<Vec2>> pointsList;
        private List<Color> colors;

        private MotionProfile profile;
        private MotionState state;
        private DriveSpecs drive;
        private long startTime;

        public final int width, height;
        public final double scale;
        public final Vec2 center;

        public DrawPanel(int width, int height, double scale, Vec2 center){
            setPreferredSize(new Dimension(width,height));
            this.width = width;
            this.height = height;
            this.scale = scale;
            this.center = center;
            this.pointsList = new ArrayList<>();
            this.colors = new ArrayList<>();

            Thread t = new Thread(){
                public void run() { while(true) repaint(); }
            };
            startTime = System.currentTimeMillis();
            t.start();
        }
        
        public void addPoints(Vec2[] points, Color color){
            pointsList.add(new ArrayList<>(Arrays.asList(points)));;
            colors.add(color);
        }

        public void appendPoints(Vec2[] points, int index){
            pointsList.get(index).addAll(Arrays.asList(points));
        }

        public void appendPoints(Vec2[] points){
            appendPoints(points, pointsList.size()-1);
        }

        public void clearPoints(){
            pointsList.clear();
            colors.clear();
        }

        public void clearPoints(Color color){
            for(int i = colors.size() - 1; i >= 0; i--){
                if(colors.get(i).equals(color)){
                    colors.remove(i);
                    pointsList.remove(i);
                }
            }
        }

        public void animateProfile(MotionProfile profile){
            this.profile = profile;
        }

        public void drawState(DriveSpecs drive, MotionState state){
            this.state = state;
            this.drive = drive;
        }

        public Vec2 toPanelSpace(Vec2 v){
            v = v.diff(center);
            return new Vec2(v.x*scale, -v.y*scale).add(new Vec2(width/2, height/2));
        }

        public Vec2 toPanelSpace(Vec2 v, MotionState center){
            return toPanelSpace(v.rotateBy(center.pose.ang).add(center.pose.vec));
        }

        public Vec2 toWorldSpace(Vec2 v){
            v = v.diff(new Vec2(width/2, height/2));
            return new Vec2(v.x/scale, -v.y/scale).add(center);
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);

            Vec2 mid = toPanelSpace(new Vec2(0,0));
            g2.setColor(Color.black);
            g2.drawLine(0, (int)mid.y, width, (int)mid.y);
            g.drawLine((int)mid.x, 0, (int)mid.x, height);
            
            g2.setStroke(new BasicStroke(3));
            for(int i = 0; i < pointsList.size(); i++){
                g.setColor(colors.get(i));
                List<Vec2> points = pointsList.get(i);
                
                if(points.size() == 0) continue;
                Vec2 prev = toPanelSpace(points.get(0)), cur;
                for(int j = 1; j < points.size(); j++){
                    cur = toPanelSpace(points.get(j));
                    g.drawLine((int)prev.x, (int)prev.y, (int)cur.x, (int)cur.y);
                    prev = cur;
                }
            }

            if(profile != null){
                final long currentTime = System.currentTimeMillis();
                double dt = (currentTime - startTime)/1000.0;
                if(dt >= profile.dt()){
                    dt = 0;
                    startTime = currentTime;
                }
                final MotionState curState = profile.getState(profile.startTime()+dt);
                paintMotionState(g, g2, profile.getDriveSpecs(), curState);
            }
            if(state != null){
                paintMotionState(g, g2, drive, state);
            }
        }

        private void paintMotionState(Graphics g, Graphics2D g2, DriveSpecs drive, MotionState state){
            final int r = 7;
            final Vec2 center = toPanelSpace(state.pose.vec);

            Vec2 left = new Vec2(0, drive.wheelAxleLength/2), right = new Vec2(0, -drive.wheelAxleLength/2);
            Vec2 leftVel = new Vec2(state.velWheels.left/4, left.y), rightVel = new Vec2(state.velWheels.right/4, right.y);

            Vec2 arrow = toPanelSpace(new Vec2(4,0), state);
            left = toPanelSpace(left, state);
            right = toPanelSpace(right, state);
            leftVel = toPanelSpace(leftVel, state);
            rightVel = toPanelSpace(rightVel, state);
            
            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(5));
            g.fillArc((int)(center.x-r), (int)(center.y-r), 2*r, 2*r, 0, 360);
            g.drawLine((int)left.x, (int)left.y, (int)right.x, (int)right.y);

            g2.setStroke(new BasicStroke(3));
            g2.setColor(state.velWheels.left > 0 ? Color.green : Color.red);
            g.drawLine((int)left.x, (int)left.y, (int)leftVel.x, (int)leftVel.y);
            g2.setColor(state.velWheels.right > 0 ? Color.green : Color.red);
            g.drawLine((int)right.x, (int)right.y, (int)rightVel.x, (int)rightVel.y);
            g2.setColor(Color.blue);
            g.drawLine((int)center.x, (int)center.y, (int)arrow.x, (int)arrow.y);
        }
    }

}  