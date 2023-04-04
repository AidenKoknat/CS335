import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class cardshuffle extends JFrame implements ActionListener {

    // Array of icons
    private ImageIcon pics[];
    private static JButton cards[];
    private static JButton shuffleButton;
    private static JButton resetButton;
    private static JButton quitButton;

    // Array of icon file names, hardcoded and initialized
    private String iconNames[] = {
            "ace_of_clubs_icon.png",
            "2_of_clubs_icon.png",
            "3_of_clubs_icon.png",
            "4_of_clubs_icon.png",
            "5_of_clubs_icon.png",
            "6_of_clubs_icon.png",
            "7_of_clubs_icon.png",
            "8_of_clubs_icon.png",
            "9_of_clubs_icon.png",
            "10_of_clubs_icon.png",
            "jack_of_clubs_icon.png",
            "queen_of_clubs_icon.png",
            "king_of_clubs_icon.png",
            "ace_of_diamonds_icon.png",
            "2_of_diamonds_icon.png",
            "3_of_diamonds_icon.png",
            "4_of_diamonds_icon.png",
            "5_of_diamonds_icon.png",
            "6_of_diamonds_icon.png",
            "7_of_diamonds_icon.png",
            "8_of_diamonds_icon.png",
            "9_of_diamonds_icon.png",
            "10_of_diamonds_icon.png",
            "jack_of_diamonds_icon.png",
            "queen_of_diamonds_icon.png",
            "king_of_diamonds_icon.png",
            "ace_of_hearts_icon.png",
            "2_of_hearts_icon.png",
            "3_of_hearts_icon.png",
            "4_of_hearts_icon.png",
            "5_of_hearts_icon.png",
            "6_of_hearts_icon.png",
            "7_of_hearts_icon.png",
            "8_of_hearts_icon.png",
            "9_of_hearts_icon.png",
            "10_of_hearts_icon.png",
            "jack_of_hearts_icon.png",
            "queen_of_hearts_icon.png",
            "king_of_hearts_icon.png",
            "ace_of_spades_icon.png",
            "2_of_spades_icon.png",
            "3_of_spades_icon.png",
            "4_of_spades_icon.png",
            "5_of_spades_icon.png",
            "6_of_spades_icon.png",
            "7_of_spades_icon.png",
            "8_of_spades_icon.png",
            "9_of_spades_icon.png",
            "10_of_spades_icon.png",
            "jack_of_spades_icon.png",
            "queen_of_spades_icon.png",
            "king_of_spades_icon.png",};

    // layout objects:  the container, and panels within the container
    private JPanel imageBoard;
    private JPanel clubPanel;
    private JPanel diamondPanel;
    private JPanel heartPanel;
    private JPanel spadePanel;
    private JPanel menu;
    private JButton button;
    private ImageIcon shuffledPics[];
    //private JButton shuffledCards[];


    private void shuffleTime() {
        ImageIcon randomPics[] = pics.clone();

        for (int i = 52; i >= 0; i--) {
            int randomNum = (int)(Math.floor(Math.random() * i));
            //shuffledPics[i] = new ImageIcon(randomPics[randomNum]);
            //setSize(shuffledPics[i].getIconWidth()+50, shuffledPics[i].getIconHeight()+50);
            //cards[i] = new JButton(shuffledPics[i]);

            for (int j = randomNum; j < 51; j++) {
                ImageIcon temp = randomPics[j];
                randomPics[j] = randomPics[j+1];
                randomPics[j+1] = temp;
            }

        }
        for (int i = 0; i < 52; i++) {
            cards[i].setIcon(randomPics[i]);
        }
        repaint();
    }

    private void resetTime() {
        for (int i = 0; i < 52; i++) {
            cards[i].setIcon(pics[i]);
        }
        repaint();
    }


    public cardshuffle() {

        // Allocate a panels to hold a button

        JFrame frame = new JFrame("cards"); // window
        JPanel imageBoard = new JPanel();        //used to hold the cards
        JPanel clubPanel = new JPanel();
        JPanel diamondPanel = new JPanel();
        JPanel heartPanel = new JPanel();
        JPanel spadePanel = new JPanel();
        JPanel menu = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stops running when you close
        cards = new JButton[52];
        pics = new ImageIcon[52];

        for (int i = 0; i < 52; i++) {
            pics[i] = new ImageIcon(iconNames[i]);
            setSize(pics[i].getIconWidth()+50, pics[i].getIconHeight()+50);
            cards[i] = new JButton(pics[i]);
            cards[i].setIcon(pics[i]);
            if ((i / 13) < 1) {
                clubPanel.add(cards[i]);
            }
            if ((i / 13) < 2 && (i/13) >= 1) {
                diamondPanel.add(cards[i]);
            }
            if ((i / 13) < 3 && (i/13) >= 2) {
                heartPanel.add(cards[i]);
            }
            if ((i / 13) < 4 && (i/13) >= 3) {
                spadePanel.add(cards[i]);
            }
        }

        shuffleButton = new JButton("Shuffle Cards");
        shuffleButton.addActionListener(this);

        resetButton = new JButton("Reset Cards");
        resetButton.addActionListener(this);

        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        imageBoard.add(clubPanel);
        imageBoard.add(diamondPanel);
        imageBoard.add(heartPanel);
        imageBoard.add(spadePanel);

        menu.add(shuffleButton);
        menu.add(resetButton);
        menu.add(quitButton);
        imageBoard.add(menu);

        frame.add(imageBoard);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) { // Attaches button to event
        Object userOption = e.getSource();
        if (userOption == shuffleButton) {
            shuffleTime();
        }
        else if (userOption == resetButton) {
            resetTime();
        }
        else if (userOption == quitButton) {
            System.exit(0);
        }
        System.out.println("event");
    }
    public static void main(String[] args) {
        cardshuffle c = new cardshuffle();
    }
}