//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MyImageProc extends JFrame {
    private BufferedImage image;
    private MyImageProc.MyImageObj view;
    private JLabel infoLabel;
    private JButton SharpenButton;
    private JButton BlurButton;
    private JButton EdgeButton;
    private JButton OriginalButton;
    private JButton CustomButton;
    private JButton rotleft;
    private JButton rotright;
    private JSlider rotateSlider;
    private JTextField[] filterfield;
    private int x;
    private int y;
    private boolean firstdrag = true;
    private JLabel ThreshLabel;
    private JLabel rotateLabel;
    private JSlider thresholdslider;
    private float[] customfiltervalues;
    private int rotation = 0;

    public MyImageProc() {
        this.buildMenus();
        this.buildComponents();
        this.buildDisplay();
    }

    private void buildMenus() {
        final JFileChooser var1 = new JFileChooser(".");
        JMenuBar var2 = new JMenuBar();
        this.setJMenuBar(var2);
        JMenu var3 = new JMenu("File");
        JMenuItem var4 = new JMenuItem("Open");
        JMenuItem var5 = new JMenuItem("Exit");
        var4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1x) {
                int var2 = var1.showOpenDialog(MyImageProc.this);
                if (var2 == 0) {
                    File var3 = var1.getSelectedFile();

                    try {
                        MyImageProc.this.image = ImageIO.read(var3);
                    } catch (IOException var5) {
                    }

                    MyImageProc.this.view.setImage(MyImageProc.this.image);
                    MyImageProc.this.view.showImage();
                }

            }
        });
        var5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                System.exit(0);
            }
        });
        var3.add(var4);
        var3.add(var5);
        var2.add(var3);
    }

    private void buildComponents() {
        this.view = new MyImageProc.MyImageObj(this.readImage("doge.jpg"));
        this.infoLabel = new JLabel("Original Image");
        this.OriginalButton = new JButton("Original");
        this.SharpenButton = new JButton("Sharpen");
        this.BlurButton = new JButton("Blur");
        this.EdgeButton = new JButton("Edges");
        this.CustomButton = new JButton("Custom");
        this.rotleft = new JButton("<<");
        this.rotright = new JButton(">>");
        this.rotateLabel = new JLabel("Rotate");
        this.rotateSlider = new JSlider(0, -360, 360, 10);
        this.rotateSlider.setMajorTickSpacing(10);
        this.rotateSlider.setPaintTicks(true);
        this.rotateSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
                MyImageProc.this.rotation = MyImageProc.this.rotateSlider.getValue();
                MyImageProc.this.view.ApplyAffine(MyImageProc.this.rotation);
                MyImageProc.this.rotateLabel.setText("Rotated Image");
                MyImageProc.this.rotateLabel.setText("Rotate Value: " + Integer.toString(MyImageProc.this.rotateSlider.getValue()));
            }
        });
        this.filterfield = new JTextField[9];
        this.customfiltervalues = new float[9];

        for(int var1 = 0; var1 < this.filterfield.length; ++var1) {
            this.filterfield[var1] = new JTextField(5);
            this.filterfield[var1].setText("0.0");
        }

        this.filterfield[4].setText("1.0");
        this.ThreshLabel = new JLabel("Threshold Value: 128");
        this.thresholdslider = new JSlider(1, 0, 255, 10);
        this.thresholdslider.setMajorTickSpacing(10);
        this.thresholdslider.setPaintTicks(true);
        this.thresholdslider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
                MyImageProc.this.view.ThresholdImage(MyImageProc.this.thresholdslider.getValue());
                MyImageProc.this.infoLabel.setText("Thresholded Image");
                MyImageProc.this.ThreshLabel.setText("Threshold Value: " + Integer.toString(MyImageProc.this.thresholdslider.getValue()));
            }
        });
        this.view.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent var1) {
                Graphics var2 = MyImageProc.this.view.getGraphics();
                var2.setColor(Color.white);
                if (MyImageProc.this.firstdrag) {
                    MyImageProc.this.x = var1.getX();
                    MyImageProc.this.y = var1.getY();
                    MyImageProc.this.firstdrag = false;
                } else {
                    MyImageProc.this.view.showImage();
                    MyImageProc.this.x = var1.getX();
                    MyImageProc.this.y = var1.getY();
                    int var3 = MyImageProc.this.view.getImage().getWidth();
                    int var4 = MyImageProc.this.view.getImage().getHeight();
                    var2.fillOval(MyImageProc.this.x - 5, MyImageProc.this.y - 5, 10, 10);
                    var2.drawLine(0, 0, MyImageProc.this.x, MyImageProc.this.y);
                    var2.drawLine(0, var4, MyImageProc.this.x, MyImageProc.this.y);
                    var2.drawLine(var3, var4, MyImageProc.this.x, MyImageProc.this.y);
                    var2.drawLine(var3, 0, MyImageProc.this.x, MyImageProc.this.y);
                }

            }
        });
        this.view.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent var1) {
                Graphics var2 = MyImageProc.this.view.getGraphics();
                MyImageProc.this.firstdrag = true;
                MyImageProc.this.x = var1.getX();
                MyImageProc.this.y = var1.getY();
                int var3 = MyImageProc.this.view.getImage().getWidth();
                int var4 = MyImageProc.this.view.getImage().getHeight();
                var2.fillOval(MyImageProc.this.x - 5, MyImageProc.this.y - 5, 10, 10);
                var2.drawLine(0, 0, MyImageProc.this.x, MyImageProc.this.y);
                var2.drawLine(0, var4, MyImageProc.this.x, MyImageProc.this.y);
                var2.drawLine(var3, var4, MyImageProc.this.x, MyImageProc.this.y);
                var2.drawLine(var3, 0, MyImageProc.this.x, MyImageProc.this.y);
            }
        });
        this.OriginalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc.this.view.showImage();
                MyImageProc.this.infoLabel.setText("Original");
            }
        });
        this.SharpenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc.this.view.SharpenImage();
                MyImageProc.this.infoLabel.setText("Sharpened");
            }
        });
        this.BlurButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc.this.view.BlurImage();
                MyImageProc.this.infoLabel.setText("Blur");
            }
        });
        this.EdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc.this.view.EdgeImage();
                MyImageProc.this.view.repaint();
                MyImageProc.this.infoLabel.setText("Edges");
            }
        });
        this.CustomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc.this.loadcustomvalues();
                MyImageProc.this.view.CustomImage();
                MyImageProc.this.infoLabel.setText("Custom");
            }
        });
        this.rotleft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc var10000 = MyImageProc.this;
                var10000.rotation -= 10;
                MyImageProc.this.view.ApplyAffine(MyImageProc.this.rotation);
                MyImageProc.this.infoLabel.setText(">>");
            }
        });
        this.rotright.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                MyImageProc var10000 = MyImageProc.this;
                var10000.rotation += 10;
                MyImageProc.this.view.ApplyAffine(MyImageProc.this.rotation);
                MyImageProc.this.infoLabel.setText("<<");
            }
        });
    }

    private void loadcustomvalues() {
        for(int var1 = 0; var1 < this.customfiltervalues.length; ++var1) {
            this.customfiltervalues[var1] = Float.parseFloat(this.filterfield[var1].getText());
        }

    }

    private void buildDisplay() {
        JPanel var1 = new JPanel();
        GridLayout var2 = new GridLayout(1, 5);
        var1.setLayout(var2);
        var1.add(this.infoLabel);
        var1.add(this.OriginalButton);
        var1.add(this.SharpenButton);
        var1.add(this.BlurButton);
        var1.add(this.EdgeButton);
        var1.add(this.CustomButton);
        var1.add(this.rotleft);
        var1.add(this.rotright);
        var1.add(this.rotateSlider);
        var1.add(this.rotateLabel);
        JPanel var3 = new JPanel();
        BorderLayout var4 = new BorderLayout(5, 5);
        var3.setLayout(var4);
        var3.add(this.ThreshLabel, "North");
        var3.add(this.thresholdslider, "Center");
        JPanel var5 = new JPanel();
        var2 = new GridLayout(3, 3);
        var5.setLayout(var2);

        for(int var6 = 0; var6 < this.filterfield.length; ++var6) {
            var5.add(this.filterfield[var6]);
        }

        Container var7 = this.getContentPane();
        var7.add(this.view, "East");
        var7.add(var1, "South");
        var7.add(var3, "West");
        var7.add(var5, "North");
    }

    public BufferedImage readImage(String var1) {
        Image var2 = Toolkit.getDefaultToolkit().getImage(var1);
        MediaTracker var3 = new MediaTracker(new Component() {
        });
        var3.addImage(var2, 0);

        try {
            var3.waitForID(0);
        } catch (InterruptedException var6) {
        }

        BufferedImage var4 = new BufferedImage(var2.getWidth(this), var2.getHeight(this), 1);
        Graphics2D var5 = var4.createGraphics();
        var5.drawImage(var2, 0, 0, this);
        return var4;
    }

    public static void main(String[] var0) {
        MyImageProc var1 = new MyImageProc();
        var1.pack();
        var1.setVisible(true);
        var1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
                System.exit(0);
            }
        });
    }

    public class MyImageObj extends JLabel {
        private BufferedImage bim = null;
        private BufferedImage filteredbim = null;
        private boolean showfiltered = false;
        private final float[] LOWPASS3x3 = new float[]{0.1F, 0.1F, 0.1F, 0.1F, 0.2F, 0.1F, 0.1F, 0.1F, 0.1F};
        private final float[] SHARPEN3x3 = new float[]{0.0F, -1.0F, 0.0F, -1.0F, 5.0F, -1.0F, 0.0F, -1.0F, 0.0F};
        private final float[] EDGE3x3 = new float[]{0.0F, -1.0F, 0.0F, -1.0F, 4.0F, -1.0F, 0.0F, -1.0F, 0.0F};

        public MyImageObj() {
        }

        public MyImageObj(BufferedImage var2) {
            this.bim = var2;
            this.filteredbim = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
            this.setPreferredSize(new Dimension(this.bim.getWidth(), this.bim.getHeight()));
            this.repaint();
        }

        public void setImage(BufferedImage var1) {
            if (var1 != null) {
                this.bim = var1;
                this.filteredbim = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
                this.setPreferredSize(new Dimension(this.bim.getWidth(), this.bim.getHeight()));
                this.showfiltered = false;
                this.repaint();
            }
        }

        public BufferedImage getImage() {
            return this.bim;
        }

        public void SharpenImage() {
            if (this.bim != null) {
                Kernel var1 = new Kernel(3, 3, this.SHARPEN3x3);
                ConvolveOp var2 = new ConvolveOp(var1, 1, (RenderingHints)null);
                BufferedImage var3 = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
                Graphics2D var4 = var3.createGraphics();
                var4.drawImage(this.bim, 0, 0, (ImageObserver)null);
                var2.filter(var3, this.filteredbim);
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void BlurImage() {
            if (this.bim != null) {
                Kernel var1 = new Kernel(3, 3, this.LOWPASS3x3);
                ConvolveOp var2 = new ConvolveOp(var1, 1, (RenderingHints)null);
                BufferedImage var3 = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
                Graphics2D var4 = var3.createGraphics();
                var4.drawImage(this.bim, 0, 0, (ImageObserver)null);
                var2.filter(var3, this.filteredbim);
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void EdgeImage() {
            if (this.bim != null) {
                Kernel var1 = new Kernel(3, 3, this.EDGE3x3);
                ConvolveOp var2 = new ConvolveOp(var1, 1, (RenderingHints)null);
                BufferedImage var3 = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
                Graphics2D var4 = var3.createGraphics();
                var4.drawImage(this.bim, 0, 0, (ImageObserver)null);
                var2.filter(var3, this.filteredbim);
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void CustomImage() {
            if (this.bim != null) {
                Kernel var1 = new Kernel(3, 3, MyImageProc.this.customfiltervalues);
                ConvolveOp var2 = new ConvolveOp(var1, 1, (RenderingHints)null);
                BufferedImage var3 = new BufferedImage(this.bim.getWidth(), this.bim.getHeight(), 1);
                Graphics2D var4 = var3.createGraphics();
                var4.drawImage(this.bim, 0, 0, (ImageObserver)null);
                var2.filter(var3, this.filteredbim);
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void ThresholdImage(int var1) {
            if (this.bim != null) {
                byte[] var3 = new byte[256];
                if (var1 < 0 || var1 > 255) {
                    var1 = 128;
                }

                int var2;
                for(var2 = 0; var2 < var1; ++var2) {
                    var3[var2] = 0;
                }

                for(int var4 = var2; var4 < 255; ++var4) {
                    var3[var4] = -1;
                }

                ByteLookupTable var6 = new ByteLookupTable(0, var3);
                LookupOp var5 = new LookupOp(var6, (RenderingHints)null);
                var5.filter(this.bim, this.filteredbim);
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void ApplyAffine(int var1) {
            float var2 = (float)((double)var1 / 180.0D * 3.141592653589793D);
            float var3 = (float)this.bim.getWidth() / 2.0F;
            float var4 = (float)this.bim.getHeight() / 2.0F;
            if (this.bim != null) {
                AffineTransform var5 = new AffineTransform(Math.cos((double)var2), Math.sin((double)var2), -Math.sin((double)var2), Math.cos((double)var2), (double)var3 - (double)var3 * Math.cos((double)var2) + (double)var4 * Math.sin((double)var2), (double)var4 - (double)var3 * Math.sin((double)var2) - (double)var4 * Math.cos((double)var2));
                Graphics2D var6 = this.filteredbim.createGraphics();
                var6.fillRect(0, 0, (int)var3 * 2, (int)var4 * 2);
                var6.setTransform(var5);
                var6.setColor(new Color(0, 0, 0));
                var6.drawImage(this.bim, 0, 0, (ImageObserver)null);
                var6.dispose();
                this.showfiltered = true;
                this.repaint();
            }
        }

        public void showImage() {
            if (this.bim != null) {
                this.showfiltered = false;
                this.repaint();
            }
        }

        public void paintComponent(Graphics var1) {
            Graphics2D var2 = (Graphics2D)var1;
            if (this.showfiltered) {
                var2.drawImage(this.filteredbim, 0, 0, this);
            } else {
                var2.drawImage(this.bim, 0, 0, this);
            }

        }
    }
}
