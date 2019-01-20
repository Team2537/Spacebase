package frc.lib.util;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;
 
public class Turtle {
    private DrawPanel panel;
    public Turtle(int width, int height, double scale, Vec2 center) {  
        JFrame f = new JFrame("Turtle");    
        panel = new DrawPanel(width,height,scale,center);
        f.add(panel);
        f.pack();    
        f.setVisible(true);
    }

    public void addPoints(Vec2[] points, Color color){
        panel.addPoints(points, color);
    }

    public void clearPoints(){
        panel.clearPoints();
    }

    private static class DrawPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private List<Vec2[]> pointsList;
        private List<Color> colors;
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
        }

        public void addPoints(Vec2[] points, Color color){
            pointsList.add(points);
            colors.add(color);
        }

        public void clearPoints(){
            pointsList.clear();
            colors.clear();
        }

        public Vec2 toPanelSpace(Vec2 v){
            Vec2 off = v.diff(center);
            return new Vec2(off.x*scale, -off.y*scale).add(new Vec2(width/2, height/2));
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
                Vec2[] points = pointsList.get(i);
                for(int j = 0; j < points.length - 1; j++){
                    Vec2 a = toPanelSpace(points[j]), b = toPanelSpace(points[j+1]);
                    g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
                }
            }
        }
    }
}  