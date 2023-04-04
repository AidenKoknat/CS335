import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class primeCalc extends JFrame implements ActionListener {

    static JButton button;
    static JTextField text;
    static JLabel label;

    primeCalc() {
        JFrame frame = new JFrame("Prime Number Finder");
        frame.setSize(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true); // ?

        JPanel panel = new JPanel();
        label = new JLabel("Enter a number 2 - 999,999");
        text = new JTextField(20); // Puts in box for number input.
        button = new JButton("Go!");
        button.addActionListener(this);

        panel.add(label);
        panel.add(text);
        panel.add(button);

        frame.getContentPane().add(panel);
        frame.pack();
    }
    public void actionPerformed(ActionEvent e) {
        if(!text.getText().matches("\\d+") || Integer.parseInt(text.getText()) > 999999 || Integer.parseInt(text.getText()) < 2) {
            label.setText("Input invalid, please put an integer 2 - 999,999");
        }
        else {
            int number = Integer.parseInt(text.getText());
            if (sieveHistogram2.primeChecker(number)) {
                label.setText(number + " is prime.");
            }
            else {
                label.setText(number + " is composite (not prime).");
            }
        }
    }
    public static void main(String[] args) {
        new primeCalc();
    }
}