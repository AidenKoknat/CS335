import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
public class Minesweeper extends JFrame implements ActionListener {
    public static int amountOfTiles;
    JFrame frame = new JFrame("MineSweeper");
    JPanel mainPanel = new JPanel();
    JPanel resetPanel = new JPanel();
    JPanel counterPanel = new JPanel();
    JPanel menuPanel = new JPanel();
    JPanel tilePanel = new JPanel();
    private static JLabel clicks;
    private static JLabel bombCounter;
    private static JLabel secondsPassed;
    private static Timer timer;
    public static boolean timerStart = false;
    String setting = "Beginner";
    JButton resetButton = new JButton("Reset");
    JButton flagButton = new JButton("Toggle Flags");
    JButton beginnerButton = new JButton("Beginner");
    JButton intermediateButton = new JButton("Intermediate");
    JButton customButton = new JButton("Custom");
    JButton expertButton = new JButton("Expert");
    public int guesses = 0;
    public int bombCount = 0;
    public int seconds = 0;
    public boolean gameOver = false;
    public boolean flagToggle = false;
    public static int bombs = 0;
    public static int rows = 0;
    public static int columns = 0;
    tiles[] tile = new tiles[amountOfTiles];
    int[] randBombTile = new int[bombs];

    public Minesweeper() {
        bombCount = bombs;
        timer = new Timer(1000, this);
        if (setting == "Intermediate") {
            rows = 16;
            columns = 16;
            bombs = 40;
        }
        else if (setting == "Expert") {
            rows = 24;
            columns = 20;
            bombs = 99;
        }
        else if (setting == "Beginner") {
            rows = 9;
            columns = 9;
            bombs = 10;
        }

        bombCount = bombs;

        tile = new tiles[amountOfTiles];
        getBombs(setting);
        placeTiles();

        int horizGap = 0;
        int VerticalGap = 0;
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        tilePanel.setLayout(new GridLayout(rows, columns, horizGap, VerticalGap));
        frame.add(mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(columns*100, rows*100);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public void customPopup() {
        setting = "Custom";
        JFrame parent = new JFrame("Custom Game");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        int minBombs = amountOfTiles/10;
        int maxBombs = amountOfTiles/4;

        JLabel rowLabel = new JLabel();
        rowLabel.setText("Number of rows: " + 16);
        JLabel colLabel = new JLabel();
        colLabel.setText("Number of columns: " + 16);
        JSlider rowSlider = new JSlider(9, 24);
        JSlider columnSlider = new JSlider(9, 30);
        JSlider bombSlider = new JSlider(minBombs, maxBombs);
        rowSlider.setValue(16);
        columnSlider.setValue(16);
        bombSlider.setValue((minBombs+maxBombs)/2);

        rowSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrRows = source.getValue();
                Minesweeper.rows = usrRows;
                Minesweeper.amountOfTiles = Minesweeper.rows * Minesweeper.columns;
                rowLabel.setText("Number of rows: " + rows);
                bombSlider.setMinimum(Minesweeper.amountOfTiles/10);
                bombSlider.setMaximum(Minesweeper.amountOfTiles/4);
            }
        });

        columnSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrColumns = source.getValue();
                Minesweeper.columns = usrColumns;
                colLabel.setText("Number of columns: " + columns);
                Minesweeper.amountOfTiles = Minesweeper.rows * Minesweeper.columns;
                bombSlider.setMinimum(Minesweeper.amountOfTiles/10);
                bombSlider.setMaximum(Minesweeper.amountOfTiles/4);
            }
        });

        bombSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrBombs = source.getValue();
                Minesweeper.bombs = usrBombs;
                bombCounter.setText("Number of bombs: " + Integer.toString(usrBombs));
            }
        });

        panel.add(rowLabel);
        panel.add(rowSlider);
        panel.add(colLabel);
        panel.add(columnSlider);
        panel.add(bombCounter);
        panel.add(bombSlider);
        JButton submitButton = new JButton("Create Game");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
                //setFrame();
                parent.setVisible(false);
                parent.dispose();
            }
        });
        panel.add(submitButton);
        parent.add(panel);
        parent.pack();
        parent.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) { // Attaches button to event
        Object userOption = e.getSource();
        if (userOption == timer) {
            seconds++;
            updateCounter();
        }
        if (userOption == resetButton) {
            reset();
        }
        if (userOption == beginnerButton) {
            setting = "Beginner";
            reset();
        }
        if (userOption == intermediateButton) {
            setting = "Intermediate";
            reset();
        }
        if (userOption == expertButton) {
            setting = "Expert";
            reset();
        }
        if (userOption == customButton) {
            customPopup();
        }
        if (userOption == flagButton) {
            if (flagToggle == false) {
                flagToggle = true;
            }
            else {
                flagToggle = false;
            }
        }
        else {
            for (int i = 0; i < amountOfTiles; i++) {
                //System.out.println("Click");
                if (userOption == tile[i].button && gameOver == false) {
                    if (flagToggle == true) {
                        tile[i].flag();
                        bombCount--;
                        updateCounter();
                    }
                    else {
                        if (timerStart == false) {
                            timer.start();
                            timerStart = true;
                        }
                        if (tile[i].clicked == false) {
                            guesses++;
                            updateCounter();
                            tile[i].onClick();
                            if (tile[i].bomb == true) {
                                for (int b = 0; b < amountOfTiles; b++) {
                                    if (tile[b].bomb == true) {
                                        tile[b].button.setIcon(new ImageIcon("bomb.png"));
                                    }
                                }
                                tile[i].button.setIcon(new ImageIcon("revealed_mine.png"));
                                counterPanel.remove(secondsPassed);
                                secondsPassed.setText("Game Over!");
                                mainPanel.remove(counterPanel);
                                counterPanel.remove(bombCounter);
                                counterPanel.remove(clicks);
                                counterPanel.add(secondsPassed);
                                mainPanel.add(counterPanel);
                                frame.add(mainPanel);
                                frame.pack();
                                frame.setVisible(true);
                                System.out.println("You lose!");
                                timer.stop();
                                timerStart = false;
                                gameOver = true;

                            }
                            else {
                                if (tile[i].number == 0) {
                                    ZeroChecker2(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void zeroChecker(int origin) {
        if (tile[origin].number == 0) { // Zero clear algorithm!
            tile[origin].chain = true;

            // Check Left
            for (int a = 0; a < 3; a++) {
                for (int j = origin; j >= 0; j--) {
                    if (j - 1 >= 0 && tile[j].chain == true && (j % columns) != 0) {
                        if (tile[j - 1].number == 0) {
                            tile[j - 1].onClick();
                            tile[j - 1].chain = true;
                        }
                    }
                    // Check Above
                    if (j - columns >= 0 && (tile[j - columns].number == 0) && tile[j].chain == true) {
                        tile[j - columns].chain = true;
                        tile[j - columns].onClick();
                    }
                }

                for (int i = origin; i < amountOfTiles; i++) {
                    // Check Right
                    if ((i + 1) < amountOfTiles && ((i % columns) != (columns - 1))) {
                        if (tile[i].chain == true && tile[i + 1].number == 0) {
                            tile[i + 1].onClick();
                            tile[i + 1].chain = true;
                        }
                    }
                    // Check Below
                    if (i + columns < amountOfTiles && (tile[i + columns].number == 0) && tile[i].chain == true) {
                        tile[i + columns].chain = true;
                        tile[i + columns].onClick();
                    }
                }
            }
        }
    }
    public void zeroCheckerAssist(int[] toCheck) {
        // Check top left
        tile[toCheck[0]].chain = true;
        if (toCheck[0] - (columns + 1) >= 0 && (toCheck[0] % columns) != 0) {
            if (tile[toCheck[0]-(columns + 1)].chain == false) {
                tile[toCheck[0]-(columns + 1)].onClick();
                if (tile[toCheck[0]-(columns + 1)].number == 0 && tile[toCheck[0]-(columns + 1)].chain == false) {
                    checkerMax++;
                    tile[toCheck[0]-(columns + 1)].chain = true;
                    toCheck[checkerMax] = toCheck[0] - (columns + 1);
                }
            }
        }
        // Check above
        if (toCheck[0] - columns >= 0) {
            tile[toCheck[0]-columns].onClick();
            if (tile[toCheck[0]-columns].number == 0 && tile[toCheck[0]-columns].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] - columns;
            }
        }

        // Check top right
        if ((toCheck[0] - columns + 1) > 0 && ((toCheck[0] % columns) != (columns - 1))) {
            tile[toCheck[0]- columns + 1].onClick();
            if (tile[toCheck[0]- columns + 1].number == 0 && tile[toCheck[0]- columns + 1].chain == false){
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] - columns + 1;
            }
        }

        // Check right
        if ((toCheck[0] + 1) < amountOfTiles && ((toCheck[0] % columns) != (columns - 1))) {
            tile[toCheck[0]+1].onClick();
            if (tile[toCheck[0]+1].number == 0 && tile[toCheck[0]+1].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] + 1;
            }
        }

        // Check below
        if (toCheck[0] + columns < amountOfTiles) {
            tile[toCheck[0]+columns].onClick();
            if (tile[toCheck[0]+columns].number == 0 && tile[toCheck[0]+columns].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] + columns;
            }
        }
        // Check left
        if (toCheck[0] - 1 >= 0 && (toCheck[0] % columns) != 0) {
            tile[toCheck[0]-1].onClick();
            if (tile[toCheck[0]-1].number == 0 && tile[toCheck[0]-1].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] - 1;
            }
        }
        // Check lower left
        if (toCheck[0] + columns - 1 < amountOfTiles && (toCheck[0] % columns) != 0) {
            tile[toCheck[0]+ columns - 1].onClick();
            if (tile[toCheck[0] + columns -1].number == 0 && tile[toCheck[0] + columns -1].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] + columns - 1;
            }
        }
        // Check lower right
        if ((toCheck[0] + columns + 1) < amountOfTiles && ((toCheck[0] % columns) != (columns - 1))) {
            tile[toCheck[0] + columns + 1].onClick();
            if (tile[toCheck[0] + columns + 1].number == 0 && tile[toCheck[0] + columns + 1].chain == false) {
                checkerMax++;
                toCheck[checkerMax] = toCheck[0] + columns + 1;
            }
        }

        for (int i = 0; i < checkerMax; i++) {
            toCheck[i] = toCheck[i+1];
        }
        checkerMax--;

        if (checkerMax == 0) {
            zeroCheckerAssist(toCheck);
        }
        else if (checkerMax > 0) {
            for (int i = 0; i < checkerMax; i++) {
                zeroCheckerAssist(toCheck);
            }
        }
    }

    int checkerMax = 0;
    int[] toCheck = new int[5000]; // to check later

    public void ZeroChecker2(int origin) {
        if (tile[origin].number == 0) { // Zero clear algorithm!
            tile[origin].chain = true;
            toCheck[checkerMax] = origin;
            zeroCheckerAssist(toCheck);
        }
        checkerMax = 0;
        toCheck = new int[5000];
    }

    public void reset() {
        guesses = 0;
        seconds = 0;
        timer.stop();
        timerStart = false;
        // Reset Bombs
        if (tilePanel.getComponentCount() > 1) {
            tilePanel.removeAll();
        }
        tilePanel.revalidate();
        for (int i = 0; i < bombs; i++) {
            randBombTile[i] = -1;
        }
        getBombs(setting);
        placeTiles();

        for (int i = 0; i < amountOfTiles; i++) {
            tile[i].resetTile();
        }
        gameOver = false;
    }
    public void updateCounter() {
        if (counterPanel.getComponentCount() > 1) {
            counterPanel.removeAll();
        }
        clicks = new JLabel("Number of clicks: " + String.valueOf(guesses));
        bombCounter = new JLabel("| Number of bombs: " + String.valueOf(bombCount));
        secondsPassed = new JLabel("| Seconds passed: " + String.valueOf(seconds));
        mainPanel.remove(counterPanel);
        counterPanel.add(clicks);
        counterPanel.add(bombCounter);
        counterPanel.add(secondsPassed);
        mainPanel.add(counterPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void setFrame() {
        int horizGap = 0;
        int VerticalGap = 0;
        if (counterPanel.getComponentCount() > 1) {
            counterPanel.removeAll();
        }
        if (menuPanel.getComponentCount() > 1) {
            menuPanel.removeAll();
        }
        if (mainPanel.getComponentCount() > 1) {
            mainPanel.removeAll();
        }
        bombCount = bombs;
        clicks = new JLabel("Number of clicks: " + String.valueOf(guesses));
        bombCounter = new JLabel("| Number of bombs: " + String.valueOf(bombCount));
        secondsPassed = new JLabel("| Seconds passed: " + String.valueOf(seconds));
        //titlePanel.add(title);
        //mainPanel.add(titlePanel);
        resetButton.addActionListener(this);
        resetPanel.add(resetButton);
        flagButton.addActionListener(this);
        resetPanel.add(flagButton);
        beginnerButton.addActionListener(this);
        menuPanel.add(beginnerButton);
        intermediateButton.addActionListener(this);
        menuPanel.add(intermediateButton);
        expertButton.addActionListener(this);
        menuPanel.add(expertButton);
        customButton.addActionListener(this);
        menuPanel.add(customButton);
        tilePanel.setLayout(new GridLayout(rows, columns));
        if (setting == "Intermediate") {
            tilePanel.setPreferredSize(new Dimension((rows * 35), (columns * 35)));
        }
        else if (setting == "Expert") {
            tilePanel.setPreferredSize(new Dimension((rows * 27), (columns * 35)));
        }
        else if (setting == "Beginner") {
            tilePanel.setPreferredSize(new Dimension((rows * 27), (columns * 31)));
        }
        else { // Custom
            if (rows > 20 && columns > 20) {
                tilePanel.setPreferredSize(new Dimension((rows * 42), (columns * 42)));
            }
            else {
                tilePanel.setPreferredSize(new Dimension((rows * 27), (columns * 31)));
            }
        }
        counterPanel.add(clicks);
        counterPanel.add(bombCounter);
        counterPanel.add(secondsPassed);
        mainPanel.add(menuPanel);
        mainPanel.add(resetPanel);
        mainPanel.add(tilePanel);
        mainPanel.add(counterPanel);
        tilePanel.setLayout(new GridLayout(rows, columns, horizGap, VerticalGap));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(rows*100, columns*100);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public void getBombs(String setting) {
        if (setting == "Intermediate") {
            rows = 16;
            columns = 16;
            bombs = 40;
        }
        else if (setting == "Expert") {
            rows = 24;
            columns = 20;
            bombs = 99;
        }
        else if (setting == "Beginner") { // Beginning
            rows = 9;
            columns = 9;
            bombs = 10;
        }
        else {
            System.out.println(bombs);
        }
        amountOfTiles = rows * columns;
        randBombTile = new int[2000];
        Random random = new Random();
        for (int i = 0; i < bombs; i++) {
            if (randBombTile[i] >= 0) {
                randBombTile[i] = -1;
            }
        }
        for (int i = 0; i < bombs; i++) {
            int randomNum = random.nextInt(amountOfTiles) + 1;

            for (int k = i; k >= 0; k--) {
                if (randomNum == randBombTile[k]) {
                    randomNum = random.nextInt(amountOfTiles);
                    k = i; // reset checker
                }
            }
            randBombTile[i] = randomNum;
            //System.out.println(randBombTile[i]);
        }
    }
    public void placeTiles() {
        tile = new tiles[amountOfTiles];
        for (int i = 0; i < amountOfTiles; i++) {
            tile[i] = new tiles();
            for (int b = 0; b < bombs; b++) {
                if (randBombTile[b] == i) {
                    tile[i].bomb = true;
                    //tile[i].button.setIcon(new ImageIcon("bomb.png")); // bomb
                }
            }
            tile[i].button.addActionListener(this);
            tilePanel.add(tile[i].button);
        }
        tilePanel.revalidate();
        getNumber();
        setFrame();
        repaint();
        revalidate();
    }
    public void getNumber() {
        int LeftOfBomb;
        for (int b = 0; b < bombs; b++) {
            LeftOfBomb = randBombTile[b] - 1;
            if (LeftOfBomb >= 0 && (LeftOfBomb % columns) != (columns - 1)) {
                tile[LeftOfBomb].number++;
                if (LeftOfBomb + columns < amountOfTiles) {
                    tile[LeftOfBomb + columns].number++;
                }
                if (LeftOfBomb - columns >= 0) {
                    tile[LeftOfBomb - columns].number++;
                }
            }
        }
        int RightOfBomb;
        for (int b = 0; b < bombs; b++) {
            RightOfBomb = randBombTile[b] + 1;
            if (RightOfBomb < amountOfTiles && (RightOfBomb % columns) != 0) {
                tile[RightOfBomb].number++;
                if (RightOfBomb + columns < amountOfTiles) {
                    tile[RightOfBomb + columns].number++;
                }
                if (RightOfBomb - columns >= 0) {
                    tile[RightOfBomb - columns].number++;
                }
            }
        }
        int AboveBomb;
        for (int b = 0; b < bombs; b++) {
            AboveBomb = randBombTile[b] - columns;
            if (randBombTile[b] >= columns) {
                tile[AboveBomb].number++;
            }
        }
        int BelowBomb;
        for (int b = 0; b < bombs; b++) {
            BelowBomb = randBombTile[b] + columns;
            if (BelowBomb < amountOfTiles) {
                tile[BelowBomb].number++;
            }
        }
        for (int i = 0; i < amountOfTiles; i++) {
            tile[i].assignNumber();
        }
    }
    public static void main(String s[]) {
        new Minesweeper();
    }
}
