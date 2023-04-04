import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import Jama.*;

public class Jmorph_phase2 extends JFrame
{
    private double alpha=0;
    private float handlesAlpha = 1;
    private BufferedImage startImage;
    private BufferedImage endImage;
    private BufferedImage tempImage;
    int t = 0;
    JPanel menu = new JPanel();
    JLabel box1Name = new JLabel("   Before^       (Please be gentle and move the boxes slowly! She gets nervous!)          ");
    JLabel box2Name = new JLabel("After^                                                                                             ");
    JLabel box3Name = new JLabel("Animation^            ");
    JButton previewButton = new JButton("Animate");
    JButton backButton = new JButton("Back");
    JLabel animateLabel = new JLabel("Frame amount:");
    JLabel DimensionLabel = new JLabel("Square amount (s*s):");
    JSpinner numFrames = new JSpinner(new SpinnerNumberModel(30, 1, 60, 1));
    JSpinner numDimensions = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
    int frames = 30;
    int selected;
    AnimCurve curve = new AnimCurve();
    JButton reset = new JButton("Reset"),
            quit  = new JButton("Quit");

    /* starting points, stored for reset */
    public static int dimension = 5;
    public static int squares = (dimension + 2) * (dimension + 2);
    int xs[] = new int[squares],
            ys[] = new int[squares],
            xs2[] = new int[squares],
            ys2[] = new int[squares],
            xs3[] = new int[squares],
            ys3[] = new int[squares];
    int box2spacing = 450;
    int box3spacing = 900;

    // Image Stuff
    //private MyImageProc.MyImageObj view;

    private void buildMenus () {
        final JFileChooser fc = new JFileChooser(".");
        JMenuBar bar = new JMenuBar();
        this.setJMenuBar (bar);
        JMenu fileMenu = new JMenu ("File");
        JMenuItem fileopen = new JMenuItem ("Open");
        JMenuItem fileexit = new JMenuItem ("Exit");
        fileopen.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        int returnVal = fc.showOpenDialog(Jmorph_phase2.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            try {
                                startImage = ImageIO.read(file);
                            } catch (IOException e1){};
                            //view.setImage(startImage);
                            //view.showImage();
                        }
                    }
                }
        );
        fileexit.addActionListener(
                new ActionListener () {
                    public void actionPerformed (ActionEvent e) {
                        System.exit(0);
                    }
                }
        );
        fileMenu.add(fileopen);
        fileMenu.add(fileexit);
        bar.add(fileMenu);
    }
    // End Image Stuff

    public Jmorph_phase2()
    {
        super("Jmorph_phase2");

        dimension = 5;

        try {
            startImage = ImageIO.read(new File("dog1.jpg"));
            endImage = ImageIO.read(new File("dog2.jpg"));
            tempImage = new BufferedImage(startImage.getWidth(), startImage.getHeight(), startImage.getType());
            tempImage = startImage; // might break ?
        }
        catch(IOException e) {
            System.err.print("Image read exception");
        }

        /* set starting point values */
        int counter = 0;
        for (int i = 1; i < dimension + 3; i++) {
            for (int j = 1; j < dimension + 3; j++) {
                xs[counter] = j * (500 / (dimension + 3));
                ys[counter] = i * (500 / (dimension + 3));
                xs2[counter] = j * (500 / (dimension + 3)) + box2spacing;
                ys2[counter] = i * (500 / (dimension + 3));
                xs3[counter] = j * (500 / (dimension + 3)) + box3spacing;
                ys3[counter] = i * (500 / (dimension + 3));
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
        controls.add(quit);
        controls.add(reset);
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
                alpha = 0;
                handlesAlpha = 1;
                for (int i = 0; i < squares; i++) {
                    curve.xs3[i] = curve.xs[i] + box3spacing;

                }
                curve.reset(curve.xs, curve.ys, curve.xs2, curve.ys2, curve.xs3, curve.ys); // Don't Delete
            }
        });

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
           Triangle[] StartTriangles = new Triangle[2*(dimension+1)*(dimension+1)];
           Triangle[] EndTriangles = new Triangle[2*(dimension+1)*(dimension+1)];
           int numStartTriangles = 0;
           int numEndTriangles = 0;
            Timer timer = new Timer(10, new ActionListener() {
                int t = 0;
                public void actionPerformed(ActionEvent e) {
                    handlesAlpha = 0;
                    if (t < frames) {
                        t++;

                        for (int i = 0; i < squares; i++) {
                            // Making da before triangles
                            if ((i < (squares - dimension - 3)) && (i % (dimension + 1) != 0) && (numStartTriangles < StartTriangles.length)) {
                                StartTriangles[numStartTriangles] = new Triangle(xs[i] + box3spacing, ys[i], xs[i + 1] + box3spacing, ys[i + 1], xs[i + dimension + 2] + box3spacing, ys[i + dimension + 2]);
                                numStartTriangles++;
                                StartTriangles[numStartTriangles] = new Triangle(xs[i+1] + (box3spacing - box2spacing), ys[i+1], xs[i + dimension + 2] + (box3spacing - box2spacing), ys[i + dimension + 2], xs[i + dimension + 3] + (box3spacing - box2spacing), ys[i + dimension + 3]);
                                numStartTriangles++;
                            }
                            // Done with triangles
                            curve.xs3[i] = curve.xs3[i] + (int) (t * ((float) ((curve.xs2[i] + box3spacing - box2spacing) - curve.xs3[i])) / frames);
                            curve.ys3[i] = curve.ys3[i] + (int) (t * ((float) (curve.ys2[i] - curve.ys3[i])) / frames);

                            // Making da end triangles
                            if ((i < squares - dimension - 4) && (i % (dimension+1) != 0) && (numEndTriangles < EndTriangles.length)) {
                                EndTriangles[numEndTriangles] = new Triangle(xs2[i] + box3spacing, ys2[i], xs2[i + 1] + box3spacing, ys2[i + 1], xs2[i + dimension + 2] + box3spacing, ys2[i + dimension + 2]);
                                numEndTriangles++;
                                EndTriangles[numEndTriangles] = new Triangle(xs2[i+1] + (box3spacing - box2spacing), ys2[i+1], xs2[i + dimension + 2] + (box3spacing - box2spacing), ys2[i + dimension + 2], xs2[i + dimension + 3] + (box3spacing - box2spacing), ys2[i + dimension + 3]);
                                numEndTriangles++;
                            }

                            // Done with triangles

                        }
                        System.out.println(numStartTriangles);
                        System.out.println(numEndTriangles);

                        for (int j = 0; j < 66; j++) {
                            //System.out.println(j);
                            warpTriangle(startImage, tempImage, StartTriangles[j], EndTriangles[j]);
                            //curve.repaint();
                        }


                        alpha = alpha + ((float)1 / frames);
                        //curve.reset(curve.xs, curve.ys, curve.xs2, curve.ys2, curve.xs3, curve.ys3);
                        repaint();
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

        controls.add(animateLabel);
        controls.add(numFrames);
        controls.add(DimensionLabel);
        controls.add(numDimensions);
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
        numDimensions.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        JSpinner spin = (JSpinner) e.getSource();
                        try {
                            spin.commitEdit();
                        }
                        catch (java.text.ParseException err) {}
                        dimension = (int) spin.getValue();
                        squares = (dimension + 2) * (dimension + 2);
                        curve = new AnimCurve();
                        int xs[] = new int[squares],
                                ys[] = new int[squares],
                                xs2[] = new int[squares],
                                ys2[] = new int[squares],
                                xs3[] = new int[squares],
                                ys3[] = new int[squares];
                        int counter = 0;
                        for (int i = 1; i < dimension + 3; i++) {
                            for (int j = 1; j < dimension + 3; j++) {
                                xs[counter] = j * (500 / (dimension + 3));
                                ys[counter] = i * (500 / (dimension + 3));
                                xs2[counter] = j * (500 / (dimension + 3)) + box2spacing;
                                ys2[counter] = i * (500 / (dimension + 3));
                                xs3[counter] = j * (500 / (dimension + 3)) + box3spacing;
                                ys3[counter] = i * (500 / (dimension + 3));
                                counter++;
                            }
                        }
                        curve.reset(curve.xs, curve.ys, curve.xs2, curve.ys2, curve.xs3, curve.ys3);
                        curve.repaint();
                        repaint();
                    }
                }
        );
    }


    /* the animatable B-spline */
    public class AnimCurve extends JComponent implements Runnable
    {

        // REDEFINE THESE PLS!!!!
        //int dimension = 6;
        //int squares = (dimension + 2) * (dimension + 2);
        /* points of control structure */
        int MAXSQUARES = (22 * 22);
        int xs[] = new int[MAXSQUARES],
                ys[] = new int[MAXSQUARES],
                xs2[] = new int[MAXSQUARES],
                ys2[] = new int[MAXSQUARES],
                xs3[] = new int[MAXSQUARES],
                ys3[] = new int[MAXSQUARES],

        /* starting point of dragged handle */
        startx,
                starty;

        /* draggable point handles */
        Rectangle handles[] = new Rectangle[squares];
        Rectangle handles2[] = new Rectangle[squares];
        Rectangle handles3[] = new Rectangle[squares];

        /* which handle is dragged */
        int drag = -1;

        /* are we animating the curve? */

        Bspline curve = new Bspline(); // !!!

        public AnimCurve()
        {
            setPreferredSize(new Dimension(2000, 450));
            for (int i = 0; i < squares; i++) {
                handles[i] = new Rectangle();
                handles2[i] = new Rectangle();
                handles3[i] = new Rectangle();
            }
        }

        public void paint(Graphics g)
        {

            // Image data
            ((Graphics2D)g).drawImage(startImage, 50, 50, this);
            ((Graphics2D)g).drawImage(endImage, box2spacing + 50, 50, this);
            //((Graphics2D)g).drawImage(startImage, box3spacing + 50, 50, this);
            ((Graphics2D)g).drawImage(tempImage, box3spacing + 50, 50, this);

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
            ((Graphics2D)g).setComposite(ac);
            ((Graphics2D)g).drawImage(endImage, box3spacing + 50, 50, this);
            // End Image data

            AlphaComposite ac2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) handlesAlpha);
            ((Graphics2D)g).setComposite(ac2);

            for (int i = 0; i < squares; i++) {
                // MAKES THE LINES!!!!!!!!!!!!!!

                if ((i < dimension + 2) || (i > (squares - (dimension + 3))) || ((i % (dimension + 2)) == 0) || ((i % (dimension + 2)) == (dimension + 1))) {
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
                if (i % (dimension + 2) != (dimension + 1)) {
                    g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
                    g.drawLine(xs2[i], ys2[i], xs2[i + 1], ys2[i + 1]);
                    g.drawLine(xs3[i], ys3[i], xs3[i + 1], ys3[i + 1]);
                    if (i < (squares - (dimension + 2))) {
                        g.drawLine(xs[i+(dimension + 2)], ys[i+(dimension + 2)], xs[i+1], ys[i+1]);
                        g.drawLine(xs2[i+(dimension + 2)], ys2[i+(dimension + 2)], xs2[i+1], ys2[i+1]);
                        g.drawLine(xs3[i+(dimension + 2)], ys3[i+(dimension + 2)], xs3[i+1], ys3[i+1]);
                    }
                }
                if (i < (squares - (dimension + 2))) {
                    g.drawLine(xs[i], ys[i], xs[i + (dimension + 2)], ys[i+(dimension + 2)]);
                    g.drawLine(xs2[i], ys2[i], xs2[i + (dimension + 2)], ys2[i+(dimension + 2)]);
                    g.drawLine(xs3[i], ys3[i], xs3[i + (dimension + 2)], ys3[i+(dimension + 2)]);
                }
            }
        }

        public void animateRectangles(int xs[], int ys[], int xs2[], int ys2[], int xs3[], int ys3[])
        {
            for (int i = 0; i < squares; i++) {
                handles[i].setRect(xs[i]-5, ys[i]-5, 10, 10);
                handles2[i].setRect(xs2[i]-5, ys2[i]-5, 10, 10);
                handles3[i].setRect(xs3[i]-5, ys3[i]-5, 10, 10);
            }
            repaint(); // !!!
        }

        /* reset points */
        public void reset(int xs[], int ys[], int xs2[], int ys2[], int xs3[], int ys3[])
        {
            for (int i = 0; i < squares; i++) {
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
            for (int i = dimension + 2; i < squares - (dimension + 2); i++) {
                if (handles[i].contains(x, y) && ((i % (dimension + 2)) != 0) && ((i % (dimension + 2)) != (dimension + 1))) {
                    drag = i;
                    startx = xs[i];
                    starty = ys[i];
                    selected = i;
                    return;
                }
                if (handles2[i].contains(x, y) && ((i % (dimension + 2)) != 0) && ((i % (dimension + 2)) != (dimension + 1))) {
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

    public static void warpTriangle(BufferedImage src, BufferedImage dest, Triangle S, Triangle D) {

        // Solves the two 3x3 systems of equations for the affine
        // mapping of the source triangle onto the destination triangle
        // defined by the coordinates of the two triangles.

        // The 3x3 matrix A is the same for both systems.
        // In one system the b vector is made from x coords
        // of the destination triangle (named BdestX here) and in the
        // other system the b vector is set to y coords of the
        // destination triangle (BdestY)
        // The first system gives the first row of the affine warp
        // The second system gives the second row.

        // The JAMA matrix library expects 2D arrays in its matrix
        // constructor, even for column vectors (only have 1 column)
        // and row vectors (only have 1 row).
        // So the column vectors BdestX and BdestY are actually 2D arrays
        // with 3 rows and 1 column.
        // Also, just like arrays, the JAMA matrix object indexes its
        // rows and columns starting with 0.
        double [][] Aarray = new double [3][3];
        double [][] BdestX = new double [3][1];
        double [][] BdestY = new double [3][1];
        for( int i= 0; i<3; ++i){
            Aarray[i][0] = S.getX(i);
            Aarray[i][1] = S.getY(i);
            Aarray[i][2] = 1.0;
            BdestX[i][0] = D.getX(i);
            BdestY[i][0] = D.getY(i);
        }

        // The matrix "A", which is the same for both systems to be solved,
        // is a 3x3 matrix.
        // The matrix "bx" is a 3x1 "column vector" of x values from the
        // destination triangle.
        // The matrix "by" is a 3x1 "column vector" of y values from the
        // destination triangle.
        // Now create matrix objects from the arrays of doubles in order to
        // use the JAMA systems-of-linear-equations solver via the "solve()"
        // method.
        Matrix A = new Matrix(Aarray);
        Matrix bx = new Matrix(BdestX);
        Matrix by = new Matrix(BdestY);

        // Matrices are ready, now solve using the "solve" method
        // The resulting solution matrices, column vectors from the solver,
        // are the values making up the first row and then the second row
        // of the affine transformation.
        // Yes, the result comes out of the solver as a column vector.
        // But each vector is a row of the affine transformation matrix we
        // seek (mapping the start triangle onto the destination triangle)
        Matrix affineRow1 = A.solve(bx);
        Matrix affineRow2 = A.solve(by);

        // Now that we have solved for the correct affine transformation
        // mapping the source triangle to the destination triangle, we
        // instantiate the Java affine transform object with the solved
        // values in the solution vectors.
        // Again, JAMA represents the solution vector as a 2D matrix
        // even though we know it is a column vector, so the column index
        // is always 0.
        // Note the order of parameters expected in the AffineTransform
        // constructor:  it wants column major order (usually one
        // expects row major...)
        // The order matters, if parameters are transposed the transformation
        // will not be correct.
        AffineTransform af = new
                AffineTransform(affineRow1.get(0,0), affineRow2.get(0,0),
                affineRow1.get(1,0), affineRow2.get(1,0),
                affineRow1.get(2,0), affineRow2.get(2,0));

        // Get the graphics context for the destination image
        // This destination image is the output image, produced by
        // rendering from the source to the destination
        Graphics2D g2 = dest.createGraphics();

        // Set the aliasing and interpolation settings
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // Create the clip region in the destination image as a "path.
        // This region is the destination triangle, D
        GeneralPath destPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        destPath.moveTo((float)D.getX(0), (float)D.getY(0));
        destPath.lineTo((float)D.getX(1), (float)D.getY(1));
        destPath.lineTo((float)D.getX(2), (float)D.getY(2) );
        destPath.lineTo((float)D.getX(0), (float)D.getY(0) );

        // Apply the clip region so that any pixels that fall outside
        // this region will be clipped
        g2.clip(destPath);
        // Apply the affine transform, which will map the pixels in
        // the source triangle onto the destination image
        // the destination
        g2.setTransform(af);
        // Map the pixels from the source image into the destination
        // according to the destination image's graphics context
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
    }

    public static void main(String argv[])
    {
        new Jmorph_phase2();
    }
}
