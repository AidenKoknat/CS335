import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

public class Jmorph extends JFrame
{
    int t = 0;
    JPanel menu = new JPanel();
    JLabel box1Name = new JLabel("             Before        (Please be gentle and move the boxes slowly! She gets nervous!)          ");
    JLabel box2Name = new JLabel("After                                                                                                ");
    JLabel box3Name = new JLabel("Animation                                         ");
    JButton previewButton = new JButton("Animate");
    JButton backButton = new JButton("Back");
    JSpinner numFrames = new JSpinner(new SpinnerNumberModel(30, 1, 60, 1));
    int frames = 30;
    int selected;
    AnimCurve curve = new AnimCurve();
    JButton reset = new JButton("Reset"),
            quit  = new JButton("Quit");

    /* starting points, stored for reset */
    int xs[] = new int[49],
            ys[] = new int[49],
            xs2[] = new int[49],
            ys2[] = new int[49],
            xs3[] = new int[49],
            ys3[] = new int[49];
    int box2spacing = 350;
    int box3spacing = 700;

    public Jmorph()
    {
        super("Jmorph");

        /* set starting point values */
        int counter = 0;
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 8; j++) {
                xs[counter] = j * 50;
                ys[counter] = i * 50;
                xs2[counter] = j * 50 + box2spacing;
                ys2[counter] = i * 50;
                xs3[counter] = j * 50 + box3spacing;
                ys3[counter] = i * 50;
                counter++;
            }
        }


        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        JPanel controls = new JPanel();
        menu.add(box1Name);
        menu.add(box2Name);
        menu.add(box3Name);
        controls.add(menu, BorderLayout.NORTH);
        add(controls, BorderLayout.SOUTH);

        /* reset button */
        reset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                curve.reset(xs, ys, xs2, ys2, xs3, ys3); // Don't Delete
            }
        });

        backButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 0; i < 49; i++) {
                    curve.xs3[i] = curve.xs[i] + box3spacing;

                }
                curve.reset(curve.xs, curve.ys, curve.xs2, curve.ys2, curve.xs3, curve.ys); // Don't Delete
            }
        });

        controls.add(reset);

        /* quit button */
        quit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        previewButton.addActionListener(new ActionListener()
        {
            Timer timer = new Timer(10, new ActionListener() {
                int t = 0;
                public void actionPerformed(ActionEvent e) {
                    if (t < frames) {
                        t++;
                        for (int i = 0; i < 49; i++) {
                            curve.xs3[i] = curve.xs3[i] + (int) (t * ((float) ((curve.xs2[i] + box3spacing - box2spacing) - curve.xs3[i])) / frames);
                            curve.ys3[i] = curve.ys3[i] + (int) (t * ((float) (curve.ys2[i] - curve.ys3[i])) / frames);
                        }
                        curve.reset(curve.xs, curve.ys, curve.xs2, curve.ys2, curve.xs3, curve.ys3);
                        curve.repaint();
                        //Thread.sleep(10);
                    }
                    else {
                        ((Timer)e.getSource()).stop();
                        t = 0;
                    }
                    timer.setRepeats(true);
                    timer.setDelay(10);
                }
            });

            public void actionPerformed(ActionEvent e)
            {
                timer.start();
            }
        });

        controls.add(quit);
        controls.add(numFrames);
        controls.add(previewButton);
        controls.add(backButton);

        /* set up click and drag on the curve */
        curve.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                curve.startDrag(e.getX(), e.getY());
            }
            public void mouseReleased(MouseEvent e)
            {
                curve.endDrag();
            }
        });
        curve.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e)
            {
                curve.doDrag(e.getX(), e.getY());
            }
        });
        curve.reset(xs, ys, xs2, ys2, xs3, ys3); // set the starting state
        add(curve, BorderLayout.CENTER);
        pack();
        setVisible(true);

        numFrames.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        JSpinner spin = (JSpinner) e.getSource();
                        try {
                            spin.commitEdit();
                        }
                        catch (java.text.ParseException err) {}
                        frames = (int) spin.getValue();
                    }
                }
        );
    }


    /* the animatable B-spline */
    public class AnimCurve extends JComponent implements Runnable
    {
        /* points of control structure */
        int xs[] = new int[49],
                ys[] = new int[49],
                xs2[] = new int[49],
                ys2[] = new int[49],
                xs3[] = new int[49],
                ys3[] = new int[49],

        /* starting point of dragged handle */
        startx,
                starty;

        /* draggable point handles */
        Rectangle handles[] = new Rectangle[49];
        Rectangle handles2[] = new Rectangle[49];
        Rectangle handles3[] = new Rectangle[49];

        /* which handle is dragged */
        int drag = -1;

        /* are we animating the curve? */

        Bspline curve = new Bspline(); // !!!

        public AnimCurve()
        {
            setPreferredSize(new Dimension(1200, 370));
            for (int i = 0; i < 49; i++) {
                handles[i] = new Rectangle();
                handles2[i] = new Rectangle();
                handles3[i] = new Rectangle();
            }
        }

        public void paint(Graphics g)
        {
            /* we always want the curve */
            //curve.paintCurve(g); // !!!

            for (int i = 0; i < 49; i++) {
                // MAKES THE LINES!!!!!!!!!!!!!!

                if ((i < 7) || (i > 41) || ((i % 7) == 0) || ((i % 7) == 6)) {
                    g.setColor(Color.BLUE);
                    ((Graphics2D)g).fill(handles[i]);
                    ((Graphics2D)g).fill(handles2[i]);
                    ((Graphics2D)g).fill(handles3[i]);
                }
                else {
                    if (i == selected) {
                        g.setColor(Color.RED);
                        ((Graphics2D)g).fill(handles[i]);
                        ((Graphics2D)g).fill(handles2[i]);
                        ((Graphics2D)g).fill(handles3[i]);
                    }
                    else {
                        g.setColor(Color.BLACK);
                        ((Graphics2D)g).fill(handles[i]);
                        ((Graphics2D)g).fill(handles2[i]);
                        ((Graphics2D)g).fill(handles3[i]);
                    }
                }
                g.setColor(Color.BLACK);
                if (i % 7 != 6) {
                    g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                    g.drawLine(xs2[i], ys2[i], xs2[i + 1], ys2[i + 1]);
                    g.drawLine(xs3[i], ys3[i], xs3[i + 1], ys3[i + 1]);
                    if (i < 42) {
                        g.drawLine(xs[i+7], ys[i+7], xs[i+1], ys[i+1]);
                        g.drawLine(xs2[i+7], ys2[i+7], xs2[i+1], ys2[i+1]);
                        g.drawLine(xs3[i+7], ys3[i+7], xs3[i+1], ys3[i+1]);
                    }
                }
                if (i < 42) {
                    g.drawLine(xs[i], ys[i], xs[i + 7], ys[i+7]);
                    g.drawLine(xs2[i], ys2[i], xs2[i + 7], ys2[i+7]);
                    g.drawLine(xs3[i], ys3[i], xs3[i + 7], ys3[i+7]);
                }
            }
        }

        public void animateRectangles(int xs[], int ys[], int xs2[], int ys2[], int xs3[], int ys3[])
        {
            for (int i = 0; i < 49; i++) {
                handles[i].setRect(xs[i]-5, ys[i]-5, 10, 10);
                handles2[i].setRect(xs2[i]-5, ys2[i]-5, 10, 10);
                handles3[i].setRect(xs3[i]-5, ys3[i]-5, 10, 10);
            }
            repaint(); // !!!
        }

        /* reset points */
        public void reset(int xs[], int ys[], int xs2[], int ys2[], int xs3[], int ys3[])
        {
            for (int i = 0; i < 49; i++) {
                this.xs[i] = xs[i];
                this.ys[i] = ys[i];
                this.xs2[i] = xs2[i];
                this.ys2[i] = ys2[i];
                this.xs3[i] = xs3[i];
                this.ys3[i] = ys3[i];
                handles[i].setRect(xs[i]-5, ys[i]-5, 10, 10);
                handles2[i].setRect(xs2[i]-5, ys2[i]-5, 10, 10);
                handles3[i].setRect(xs3[i]-5, ys3[i]-5, 10, 10);
            }
            selected = -1;
            repaint(); // !!!
        }

        /* store starting point, which point is dragged */
        public void startDrag(int x, int y)
        {
            /* find which handle if any is trying to be dragged */
            for (int i = 7; i < 42; i++) {
                if (handles[i].contains(x, y) && ((i % 7) != 0) && ((i % 7) != 6)) {
                    drag = i;
                    startx = xs[i];
                    starty = ys[i];
                    selected = i;
                    return;
                }
                if (handles2[i].contains(x, y) && ((i % 7) != 0) && ((i % 7) != 6)) {
                    drag = i;
                    startx = xs2[i];
                    starty = ys2[i];
                    selected = i;
                    return;
                }
            }
        }

        /* move the handle and repaint */
        public void doDrag(int x, int y)
        {
            /* only if a handle is being dragged */
            if (drag > -1) {
                if (handles[drag].contains(x, y)) {
                    xs[drag] = x;
                    ys[drag] = y;
                    xs3[drag] = x + box3spacing;
                    ys3[drag] = y;
                    handles[drag].setRect(x - 5, y - 5, 10, 10);
                    handles3[drag].setRect(x - 5 + box3spacing, y - 5, 10, 10);
                }
                if (handles2[drag].contains(x, y)) {
                    xs2[drag] = x;
                    ys2[drag] = y;
                    handles2[drag].setRect(x - 5, y - 5, 10, 10);
                }
                repaint();
            }
        }

        /* start the animation */
        public void endDrag()
        {
            // WILL USE FOR ANIMATION LATER ON!
            /* only if a handle was dragged */
            /*
            if (drag > -1) {
                (new Thread(this)).start(); // !!!
            }
            */
        }

        public void run()
        {
            try {
                int j,

                        /* need to know where we end up */
                        endx = xs[drag],
                        endy = ys[drag];

                /* how much to move the dragged point each step */
                double stepx = (endx - startx) / 30.0,
                        stepy = (endy - starty) / 30.0;

				/* for the first 29 steps, increment dragged point and
				   rebuild curve, then repaint - with a delay */
                for (int i = 1; i < 30; i++) {
                    //curve.resetCurve();
                    xs[drag] = (int)(startx + i * stepx);
                    ys[drag] = (int)(starty + i * stepy);
                    for (j = 0; j < 4; j++) {
                        //curve.addPoint(xs[j], ys[j]);
                    }
                    repaint();
                    Thread.sleep(10); // CHANGED! INCREASES SPEED
                }

				/* for the last step, set to computed end point to avoid
				   any round error that might occur */
                //curve.resetCurve();
                xs[drag] = endx;
                ys[drag] = endy;
                for (j = 0; j < 4; j++) {
                    //curve.addPoint(xs[j], ys[j]);
                }

                /* make sure controls structure is redisplayed too */
                repaint();
                drag = -1; // done with that point
            } catch (Exception e) {}
        }
    }
    public static void main(String argv[])
    {
        new Jmorph();
    }
}
