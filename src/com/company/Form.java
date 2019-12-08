package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.parseDouble;
import static java.lang.Math.*;
import static java.lang.Math.max;

public class Form extends JDialog {
    private JPanel contentPane;
    private JComboBox comboBox1;
    private JSpinner speed;
    private JSpinner valueZ;
    private JButton button1;
    private JPanel DrawPanel;
    private JSlider FoV;
    private JSpinner valueY;
    private JSpinner valueX;
    private int update = 0;
    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = 1999000000 / UPDATE_RATE;

    public Form() {
        setContentPane(contentPane);
        setModal(true);
        DrawPanel.setLayout(new BoxLayout(DrawPanel, BoxLayout.Y_AXIS));
        DrawPanel.setPreferredSize(new Dimension(400, 400));
        DrawPanel.setMinimumSize(new Dimension(400, 400));
        DrawPanel.setMaximumSize(new Dimension(400, 400));
//        SpinnerModel numbers = new SpinnerNumberModel(0, -180, 180, 2);
//        valueX.setValue(numbers);
//        valueY.setValue(numbers);
//        valueZ.setValue(numbers);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                render();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();

    }

    private ArrayList<Triangle> tris = new ArrayList<>();

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

    public void go() {

        piramide();
        if (update == -180) update = 180;
        Graphics2D g2 = (Graphics2D) DrawPanel.getGraphics();
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());

        double gamma = toRadians(update);

        double roll = toRadians(0);
        double heading = toRadians(30);
        double deltaX = toRadians(parseDouble(valueX.getValue().toString()));
        double deltaY = toRadians(parseDouble(valueY.getValue().toString()));
        double deltaZ = toRadians(parseDouble(valueZ.getValue().toString()));
        Matrix matrixX = new Matrix(new double[]{
                1, 0, 0, 0,
                0, cos(deltaX), sin(deltaX), 0,
                0, -sin(deltaX), 0, cos(deltaX),
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
        double viewportWidth = DrawPanel.getWidth();
        double viewportHeight = DrawPanel.getHeight();
        double fovAngle = toRadians(120);
        double fov = tan(fovAngle / 2) * 180;
        Matrix transform = new Matrix(null);

        if (parseDouble(speed.getValue().toString()) == 0) {

            Matrix tran = matrixY
                    // .multiply(matrixY)
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
                new BufferedImage((int) DrawPanel.getWidth(), (int) DrawPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
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

        g2.drawImage(img, 0, 0, null);

    }

    public Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }

    public void render() {
        Logic logic = new Logic(DrawPanel);
        // go();
        logic.go(parseDouble(valueX.getValue().toString()), parseDouble(valueY.getValue().toString()), parseDouble(valueZ.getValue().toString()), parseDouble(speed.getValue().toString()), "SSS", 10);
      /*  java.util.Timer timer = new java.util.Timer();

        TimerTask timerTask = new TimerTask() {
            int q = 20;
            int stop = 1;

            @Override
            public void run() {

                logic.go();
            }
        };
        timer.schedule(timerTask, 0, 50);*/

   /*     boolean running = true;
        float delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;
            //    System.out.println(update);
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta >= 1) {
                go();
                update--;
                delta--;

            }
        }*/
    }


    public static void main(String[] args) {
        Form dialog = new Form();
        dialog.setSize(500, 500);
        dialog.pack();
        dialog.setVisible(true);

        boolean running = true;
        float delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long elapsedTime = now - lastTime;
            lastTime = now;
            //    System.out.println(update);
            delta += (elapsedTime / UPDATE_INTERVAL);
            while (delta >= 1) {
                delta--;

            }
        }
        System.exit(0);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        panel1.add(comboBox1, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueX = new JSpinner();
        panel1.add(valueX, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        speed = new JSpinner();
        panel1.add(speed, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueY = new JSpinner();
        panel1.add(valueY, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        valueZ = new JSpinner();
        panel1.add(valueZ, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button1 = new JButton();
        button1.setText("Draw");
        panel1.add(button1, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        DrawPanel = new JPanel();
        DrawPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(DrawPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        FoV = new JSlider();
        contentPane.add(FoV, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
