import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class tiles {
    public ImageIcon front;
    public ImageIcon back;
    public int number;
    public JButton button;
    boolean clicked;
    boolean bomb;
    boolean chain;
    boolean flag;

    private int scale = 40;

    // constructor
    public tiles() {
        back = new ImageIcon("unclicked_tile.png");
        number = 0; // uninitialized
        bomb = false;
        chain = false;
        flag = false;
        front = new ImageIcon("blank.png");

        clicked = false;
        button = new JButton();
        button.setPreferredSize(new Dimension(40, 40));
        button.setMargin(new Insets(0,0,0,0));
        button.setIcon(back);
    }
    public void assignNumber() {
        if (bomb == true) {
            front = new ImageIcon("bomb.png");
        }
        else {
            if (number == 1) {
                front = new ImageIcon("one.png");
            }
            if (number == 2) {
                front = new ImageIcon("2.png");
            }
            if (number == 3) {
                front = new ImageIcon("3.png");
            }
            if (number == 4) {
                front = new ImageIcon("4.png");
            }
            if (number == 5) {
                front = new ImageIcon("5.png");
            }
            if (number == 6) {
                front = new ImageIcon("6.png");
            }
            if (number == 7) {
                front = new ImageIcon("7.png");
            }
            if (number == 8) {
                front = new ImageIcon("8.png");
            }
        }
        Image scaleFrontImage = front.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        Image scaleBackImage = back.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        front = new ImageIcon(scaleFrontImage);
        back = new ImageIcon(scaleBackImage);

    }

    public void flag() {
        if (flag == true) {
            flag = false;
            back = new ImageIcon("unclicked_tile.png");
            button.setIcon(back);
        }
        else {
            flag = true;
            back = new ImageIcon("flag.png");
            button.setIcon(back);
        }
    }

    public void onClick() {
        if (clicked == false) {
            button.setIcon(front);
        }
        clicked = true;
    }
    public void resetTile() {
        if (clicked = true) {
            button.setIcon(back);
            clicked = false;
            chain = false;
        }
    }
}

