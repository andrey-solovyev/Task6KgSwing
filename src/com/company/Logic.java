package com.company;


import javafx.scene.canvas.GraphicsContext;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import static java.lang.Double.parseDouble;
import static java.lang.Math.*;

public class Logic {
    private Random random = new Random();
    private ArrayList<Triangle> tris = new ArrayList<>();
    private double heading;
    private double gamma = 0;
    private double n1, n2, n3;
    private double rotationalSpeed = 0;
    private GraphicsContext graphicsContext;
    private double FoV;
    private JPanel jPanel;
    private Graphics graphics;
    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = 1999000000 / UPDATE_RATE;


    public Logic(JPanel jPanel) {
        this.jPanel = jPanel;
        graphics = jPanel.getGraphics();
    }

    private ArrayList<Triangle> piramide() {
        ArrayList<Triangle> tris = new ArrayList<>();

        int f = 100;
        tris.add(new Triangle(new Vertex(0, 0, 0, 1),
                new Vertex(0, 1 * f, 0, 1),
                new Vertex(Math.sqrt(3) / 2 * f, 0.5 * f, 0, 1),
                Color.GREEN));
        tris.add(new Triangle(new Vertex(0, 0, 0, 1),
                new Vertex(Math.sqrt(3) / 6 * f, 0.5 * f, Math.sqrt(6) / Math.sqrt(3) * f, 1),
                new Vertex(Math.sqrt(3) / 2 * f, 0.5 * f, 0, 1),
                Color.RED));
        tris.add(new Triangle(new Vertex(Math.sqrt(3) / 2 * f, 0.5 * f, 0, 1),
                new Vertex(Math.sqrt(3) / 6 * f, 0.5 * f, Math.sqrt(6) / Math.sqrt(3) * f, 1),
                new Vertex(0, 1 * f, 0, 1),
                Color.ORANGE));
        tris.add(new Triangle(new Vertex(0, 1 * f, 0, 1),
                new Vertex(Math.sqrt(3) / 6 * f, 0.5 * f, Math.sqrt(6) / Math.sqrt(3) * f, 1),
                new Vertex(0, 0, 0, 1),
                Color.BLUE));
        this.tris = tris;
        return tris;
    }

    public void go(double x, double y, double z, double speed, String string, int FoV) {
        n1 = toRadians(x);
        n2 = toRadians(y);
        n3 = toRadians(z);
        rotationalSpeed = speed;
        this.FoV = toRadians(FoV);
        repaint();
      /*  boolean running = true;
        float delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;
            //    System.out.println(update);
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta >= 1) {
                repaint();
                delta--;

            }
        }*/
    }



    public void repaint() {
        piramide();
        // if (update == -180) update = 180;
        Graphics2D g2 = (Graphics2D) jPanel.getGraphics();
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, jPanel.getWidth(), jPanel.getHeight());

        //  double gamma = toRadians(update);

        double roll = toRadians(0);
        double heading = toRadians(30);
        double deltaX = n1;
        double deltaY = n2;
        double deltaZ = n3;
        Matrix matrixX = new Matrix(new double[]{
                1, 0, 0, 0,
                0, cos(deltaX), sin(deltaX), 0,
                0, -sin(deltaX), cos(deltaX),0,
                0, 0, 0, 1
        });
        Matrix matrixY = new Matrix(new double[]{
                cos(deltaY), 0, -sin(deltaY), 0,
                0, 1, 0, 0,
                sin(deltaY), 0, cos(deltaY), 0,
                0, 0, 0, 1
        });
        Matrix matrixZ = new Matrix(new double[]{
                cos(deltaZ), sin(deltaZ), 0, 0,
                -sin(deltaZ), cos(deltaZ), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
        Matrix updates = new Matrix(new double[]{
                cos(gamma), sin(gamma), 0, 0,
                -sin(gamma), cos(gamma), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });


        System.out.println(gamma);
   /*     Matrix arbitraryAxis = new Matrix(new double[]{
                Math.pow(n1, 2) + (1 - Math.pow(n1, 2)) * cos(gamma), n1 * n2 * (1 - cos(gamma)) + n3 * sin(gamma), n1 * n3 * (1 - cos(gamma)) - n2 * sin(gamma), 0,
                n1 * n2 * (1 - cos(gamma)) - n3 * sin(gamma), Math.pow(n2, 2) + (1 - Math.pow(n2, 2)) * cos(gamma), n2 * n3 * (1 - cos(gamma)) + n1 * sin(gamma), 0,
                n1 * n3 * (1 - cos(gamma)) + n2 * sin(gamma), n2 * n3 * (1 - cos(gamma)) - n1 * sin(gamma), Math.pow(n3, 2) + (1 - Math.pow(n3, 2)) * cos(gamma), 0,
                0, 0, 0, 1
        });*/
        Matrix rollTransform = new Matrix(new double[]{
                cos(roll), -sin(roll), 0, 0,
                sin(roll), cos(roll), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
        Matrix panOutTransform = new Matrix(new double[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, -400, 1
        });
        double viewportWidth = jPanel.getWidth();
        double viewportHeight = jPanel.getHeight();
        double fovAngle = toRadians(120);
        double fov = tan(fovAngle / 2) * 180;
        Matrix transform = new Matrix(null);

        if (rotationalSpeed == 0) {

            Matrix tran = matrixX
                    .multiply(matrixY)
                    .multiply(matrixZ)
                    .multiply(panOutTransform);
            transform = tran;
        } else {
            Matrix tran = matrixY
                    //  .multiply(matrixY)
                    .multiply(matrixZ)
                    .multiply(updates)
                    .multiply(panOutTransform);
            transform = tran;
        }

        BufferedImage img =
                new BufferedImage((int) jPanel.getWidth(), (int) jPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        // initialize array with extremely far away depths
        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }

        for (Triangle t : tris) {
            Vertex v1 = transform.transform(t.getV1());
            Vertex v2 = transform.transform(t.getV2());
            Vertex v3 = transform.transform(t.getV3());

            Vertex ab =
                    new Vertex(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ(), v2.getW() - v1.getW());
            Vertex ac =
                    new Vertex(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ(), v3.getW() - v1.getW());
            Vertex norm = new Vertex(
                    ab.getY() * ac.getZ() - ab.getZ() * ac.getY(),
                    ab.getZ() * ac.getX() - ab.getX() * ac.getZ(),
                    ab.getX() * ac.getY() - ab.getY() * ac.getX(),
                    1
            );
            double normalLength =
                    sqrt(norm.getX() * norm.getX() + norm.getY() * norm.getY() + norm.getZ() * norm.getZ());
            norm.setX(norm.getX() / normalLength);
            norm.setY(norm.getY() / normalLength);
            norm.setZ(norm.getZ() / normalLength);

            double angleCos = Math.abs(norm.getZ());

            v1.setX(v1.getX() / (-v1.getZ()) * fov);
            v1.setY(v1.getY() / (-v1.getZ()) * fov);
            v2.setX(v2.getX() / (-v2.getZ()) * fov);
            v2.setY(v2.getY() / (-v2.getZ()) * fov);
            v3.setX(v3.getX() / (-v3.getZ()) * fov);
            v3.setY(v3.getY() / (-v3.getZ()) * fov);

            v1.setX(v1.getX() + viewportWidth / 2);
            v1.setY(v1.getY() + viewportHeight / 2);
            v2.setX(v2.getX() + viewportWidth / 2);
            v2.setY(v2.getY() + viewportHeight / 2);
            v3.setX(v3.getX() + viewportWidth / 2);
            v3.setY(v3.getY() + viewportHeight / 2);

            int minX = (int) max(0, ceil(min(v1.getX(), min(v2.getX(), v3.getX()))));
            int maxX = (int) min(img.getWidth() - 1, floor(max(v1.getX(), max(v2.getX(), v3.getX()))));
            int minY = (int) max(0, ceil(min(v1.getY(), min(v2.getY(), v3.getY()))));
            int maxY = (int) min(img.getHeight() - 1, floor(max(v1.getY(), max(v2.getY(), v3.getY()))));

            double triangleArea =
                    (v1.getY() - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - v1.getX());

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 =
                            ((y - v3.getY()) * (v2.getX() - v3.getX()) + (v2.getY() - v3.getY()) * (v3.getX() - x)) / triangleArea;
                    double b2 =
                            ((y - v1.getY()) * (v3.getX() - v1.getX()) + (v3.getY() - v1.getY()) * (v1.getX() - x)) / triangleArea;
                    double b3 =
                            ((y - v2.getY()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v2.getX() - x)) / triangleArea;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                        int zIndex = y * img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, getShade(t.getColor(), angleCos).getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }

        }
/*try{
    File outputfile = new File("C:\\Users\\andru\\Pictures\\image.jpg");
    ImageIO.write(img, "jpg", outputfile);
} catch (IOException e){
    System.out.println();
}*/
        graphics.drawImage(img, 0, 0, null);
        // jPanel.repaint();
    }

    public static java.awt.Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new java.awt.Color(red, green, blue);
    }


}
