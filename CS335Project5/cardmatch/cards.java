import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class cards {
    public ImageIcon front;
    public ImageIcon back;
    public int rank;
    public JButton button;
    boolean matched;

    // constructor
    public cards(ImageIcon front, int rank) {
        this.front = front;
        back = new ImageIcon("back_of_card_icon.png");
        this.rank = rank;
        matched = false;
        button = new JButton();
        button.setIcon(back);
    }
    public void flipTime() {
        if (button.getIcon() == front) {
            button.setIcon(back);
        }
        else {
            button.setIcon(front);
        }
    }
}
