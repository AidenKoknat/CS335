import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class Hoops extends JPanel {
    private Ball[] hoop = new Ball[4];
    private static double delay = 10;
    private double speed=0.01;
    static JButton pointsButton;
    static JButton hoopButton;
    static JSlider slider;
    private JPanel menu;
    boolean showHoop = true;
    boolean showBall = true;

    Bspline curve = new Bspline();

    private static int panelWidth=800;
    private static int panelHeight=800;

    public Hoops() {
        // Set up the hoop points with random speeds and colors
        for (int i = 0; i < 4; i++) {
            hoop[i] = new Ball(panelWidth, panelHeight,
                    (float) (speed * Math.random()),
                    (float) (speed * Math.random()),
                    new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
        }

        // Create a thread to update the animation and repaint
        Thread thread = new Thread() {
            public void run() {
                curve.resetCurve(); /// !!!
                new Thread(this).start();
                while (true) {
                    // Ask the ball to move itself and then repaint
                    for (int i = 0; i < 4; i++) {
                        hoop[i].moveBall();
                        curve.addPoint(hoop[i].getX(), hoop[i].getY()); // !!!
                    }
                    repaint(); // !!!
                    try {
                        Thread.sleep((int)delay);
                    } catch (InterruptedException ex) { }
                }
            }
        };
        thread.start();

        hoopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showHoop = !showHoop;}
        });
        pointsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showBall = !showBall;}
        });
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                speed = slider.getValue();
                Hoops.delay = speed;
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the ball
        for (int i = 0; i < 4; i++) {
            if (showBall == true) {
                hoop[i].paintBall(g);
            }
        }
        if (showHoop == true) {
            curve.paintCurve(g);
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("Bouncing Ball");
        JPanel menu = new JPanel();
        pointsButton = new JButton("Display Points");
        hoopButton = new JButton("Display Hoop");
        JLabel label = new JLabel("  Speed");
        slider = new JSlider();
        menu.add(pointsButton);
        menu.add(hoopButton);
        menu.add(label);
        menu.add(slider);
        frame.add(menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panelWidth, panelHeight);
        frame.setContentPane(new Hoops());
        frame.add(menu);
        frame.setVisible(true);
    }
}

