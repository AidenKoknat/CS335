//
// COMPILE WITH cards.java !!
// EX: javac cardmatch.java cards.java
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class cardmatch extends JFrame implements ActionListener {

    // Declaration/Instantiation
    private cards[] deck = new cards[52];
    private cards[] orderedDeck = new cards[52];
    private ImageIcon pics[] = new ImageIcon[52];
    private static JButton shuffleButton;
    private static JButton resetButton;
    private static JButton quitButton;
    private static Timer timer;
    int matchesMade = 0;
    int guesses = 0;
    private static JLabel guessLabel;
    private static JLabel matchLabel;

    // layout objects:  the container, and panels within the container
    private JPanel imageBoard;
    private JPanel clubPanel;
    private JPanel diamondPanel;
    private JPanel heartPanel;
    private JPanel spadePanel;
    private JPanel menu;
    private JButton button;
    private ImageIcon shuffledPics[];

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
            "king_of_spades_icon.png",
            "back_of_card_icon.png",};

    // End Declaration/Instantiation //////////////////////////

    private void swapCards(int a, int b) {
        cards temp = deck[a];
        deck[a] = deck[b];
        deck[b] = temp;
    }

    private void shuffleTime() {
        matchesMade = 0;
        guesses = 0;
        matchLabel.setText("Number of matches: " + String.valueOf(matchesMade));
        guessLabel.setText("Number of guesses: " + String.valueOf(guesses));
        for (int i = 52; i >= 0; i--) {
            int randomNum = (int)(Math.floor(Math.random() * i));
            for (int j = randomNum; j < 51; j++) {
                swapCards(j,j+1);
            }

        }
        for (int i = 0; i < 52; i++) {
            if (deck[i].button.getIcon() == deck[i].front) {
                deck[i].flipTime();
            }
        }
        addToPanel();
        revalidate();
    }

    private void clearPanel(JPanel panel) {
        if (panel.getComponentCount() > 1) {
            panel.removeAll();
        }
        panel.revalidate();
    }

    private void addToPanel() {
        clearPanel(clubPanel);
        clearPanel(diamondPanel);
        clearPanel(heartPanel);
        clearPanel(spadePanel);
        for (int i = 0; i < 52; i++) {
            if ((i / 13) < 1) {
                clubPanel.add(deck[i].button);
            }
            if ((i / 13) < 2 && (i/13) >= 1) {
                diamondPanel.add(deck[i].button);
            }
            if ((i / 13) < 3 && (i/13) >= 2) {
                heartPanel.add(deck[i].button);
            }
            if ((i / 13) < 4 && (i/13) >= 3) {
                spadePanel.add(deck[i].button);
            }
        }
    }

    public cardmatch() {
        // Allocate a panels to hold a button
        timer = new Timer(3000, this);
        timer.setRepeats(false);

        // window
        JFrame frame = new JFrame("Matching Game");

        //used to hold the cards
        JPanel imageBoard = new JPanel();

        clubPanel = new JPanel();
        diamondPanel = new JPanel();
        heartPanel = new JPanel();
        spadePanel = new JPanel();
        menu = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stops running when you close

        // Display back of card (set up)
        for (int i = 0; i < 52; i++) {
            // Initializing Decks
            pics[i] = new ImageIcon(iconNames[i]);
            deck[i] = new cards(new ImageIcon(iconNames[i]), (i % 13) + 1);
            orderedDeck[i] = new cards(new ImageIcon(iconNames[i]), (i % 13) + 1);
            //setSize(cards[i].front.getIconWidth()+50, cards[i].front.getIconHeight()+50);
            //setSize(cards[i].back.getIconWidth()+50, cards[i].back.getIconHeight()+50);

            deck[i].button.addActionListener(this);
        }

        addToPanel();

        shuffleButton = new JButton("Shuffle/Reset Cards");
        shuffleButton.addActionListener(this);

        guessLabel = new JLabel("Number of guesses: " + String.valueOf(guesses));
        matchLabel = new JLabel("Number of matches: " + String.valueOf(matchesMade));

        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        imageBoard.add(clubPanel);
        imageBoard.add(diamondPanel);
        imageBoard.add(heartPanel);
        imageBoard.add(spadePanel);

        menu.add(matchLabel);
        menu.add(shuffleButton);
        menu.add(quitButton);
        menu.add(guessLabel);
        imageBoard.add(menu);

        frame.add(imageBoard);
        frame.pack();
        frame.setVisible(true);

    }
    int secondPickFlag = 1;
    int firstCard = 0;
    int secondCard = 0;
    @Override
    public void actionPerformed(ActionEvent e) { // Attaches button to event
        Object userOption = e.getSource();

        if (userOption == shuffleButton) {
            shuffleTime();
        }
        else if (userOption == quitButton) {
            System.exit(0);
        }
        else {
            for (int i = 0; i < 52; i++) {
                if (userOption == deck[i].button && (secondPickFlag == 1 || secondPickFlag == 2) && deck[i].button.getIcon() == deck[i].back) {
                    deck[i].flipTime();

                    if (secondPickFlag == 1) {
                        firstCard = i;
                        secondPickFlag++;
                    }
                    else if (secondPickFlag == 2) {
                        guesses++;
                        guessLabel.setText("Number of guesses: " + String.valueOf(guesses));
                        if (deck[i].rank % 13 == deck[firstCard].rank % 13) {
                            secondPickFlag = 1;
                            matchesMade++;
                            matchLabel.setText("Number of matches: " + String.valueOf(matchesMade));
                        }
                        else {
                            secondCard = i;
                            timer.start();
                            secondPickFlag = 3;
                        }
                    }
                }
            }
            if (userOption == timer) {
                deck[firstCard].flipTime();
                deck[secondCard].flipTime();
                secondPickFlag = 1;
            }
        }
    }
    public static void main(String[] args) {
        cardmatch c = new cardmatch();
    }
}