package trtpo;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import static trtpo.FilePaths.ABOUT_FILE;
import static trtpo.FilePaths.SCORE_FILE;

public class Game extends JFrame implements ActionListener {
    public final static int WIDTH = 900;

    public final static int HEIGHT = 534;

    public final static int REFRESH_TIME = 100;
    public static Cursor CURSOR_LOCKED;
    public static Cursor CURSOR_UNLOCKED;
    protected static Cursor CURSOR_DEFAULT;

    public static int highScore = 0;


    private JTextField txtHighScore;

    public final String scoreFile = new File(getClass()
            .getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath())
            .getParent() +
            File.separator + SCORE_FILE;
    public final String aboutFile = ABOUT_FILE;

    private Font defaultFont;
    private JPanel menuPanel, highScorePanel, aboutPanel;
    private JButton playButton, highScoreButton, aboutButton, backButton1, backButton2, quitButton;//инициализацию сделать в конструкторе!!!!

    private static GameState gameState;


    public Game() throws HeadlessException, IOException {
        this("SHOOTER GAME");
    }

    public Game(String title) throws HeadlessException, IOException {
        super(title);

        initCursors();
        initButtons();
        loadHighScore();

        defaultFont = new Font(Font.SERIF, Font.BOLD, 24);

        setMenuPanel();
        setHighScorePanel();
        setAboutPanel();

        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void initButtons() {
        playButton = createButton(FilePaths.playButtons);

        highScoreButton = createButton(FilePaths.highScoreButtons);

        aboutButton = createButton(FilePaths.aboutButtons);

        quitButton = createButton(FilePaths.quitButtons);

        backButton1 = createButton(FilePaths.backButtons);
        backButton2 = createButton(FilePaths.backButtons);
    }

    public void initCursors() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        Image lockedImage = toolkit.getImage(getClass().getResource(FilePaths.CURSOR_LOCKED));
        Image unlockedImage = toolkit.getImage(getClass().getResource(FilePaths.CURSOR_UNLOCKED));
        Image menuImage = toolkit.getImage(getClass().getResource(FilePaths.CURSOR_DEFAULT));

        CURSOR_LOCKED = toolkit.createCustomCursor(lockedImage, new Point(20, 20), "cursorLocked");
        CURSOR_UNLOCKED = toolkit.createCustomCursor(unlockedImage, new Point(20, 20), "cursorUnlocked");
        CURSOR_DEFAULT = toolkit.createCustomCursor(menuImage, new Point(16, 16), "cursorDefault");
    }

    public void loadHighScore() throws IOException {
        File file = new File(scoreFile);


        if (!file.canRead()) {
            setHighScore(0);
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String score = reader.readLine();
            setHighScore((score != null) ? Integer.parseInt(score) : 0);
            reader.close();
        }
    }

    public static void setState(GameState state) {
        Game.gameState = state;
    }

    public static void setHighScore(int score) {
        if (score < 0)
            throw new IllegalArgumentException("High Score CANNOT BE a negative number!");
        if (score > highScore)
            highScore = score;
    }

    public static int getHighScore() {
        return highScore;
    }

    public void saveHighScore() throws IOException {
        File newScoreFile = new File(scoreFile);
        BufferedWriter writer = new BufferedWriter(new FileWriter(newScoreFile));
        writer.write(Integer.toString(getHighScore()));
        writer.close();
    }

    public void setMenuPanel() {
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        menuPanel.setBounds(0, 0, WIDTH, HEIGHT);
        menuPanel.setSize(Game.WIDTH, Game.HEIGHT);
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setCursor(Game.CURSOR_DEFAULT);
        Dimension dim = Toolkit.getDefaultToolkit()
                               .getScreenSize();
        this.setLocation(dim.width / 4, dim.height / 4);
        JPanel innerPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        innerPanel.setPreferredSize(new Dimension(310, 440));
        innerPanel.setBackground(Color.WHITE);


        innerPanel.add(playButton);
        innerPanel.add(highScoreButton);
        innerPanel.add(aboutButton);
        innerPanel.add(quitButton);

        menuPanel.add(innerPanel);

    }

    public void setHighScorePanel() {
        highScorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        highScorePanel.setSize(Game.WIDTH, Game.HEIGHT);
        highScorePanel.setBackground(Color.WHITE);
        highScorePanel.setCursor(Game.CURSOR_DEFAULT);

        JPanel innerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        innerPanel.setPreferredSize(new Dimension(800, 220));
        innerPanel.setBackground(Color.WHITE);

        JLabel lblMessage = new JLabel("High Score", JLabel.CENTER);
        lblMessage.setForeground(Color.BLACK);
        lblMessage.setFont(defaultFont);
        lblMessage.setHorizontalTextPosition(JLabel.CENTER);

        txtHighScore = new JTextField(Integer.toString(highScore), 6);
        txtHighScore.setFont(defaultFont);
        txtHighScore.setHorizontalAlignment(JTextField.CENTER);
        txtHighScore.setEditable(false);

        innerPanel.add(lblMessage, BorderLayout.NORTH);
        innerPanel.add(txtHighScore, BorderLayout.CENTER);

        highScorePanel.add(innerPanel);
        highScorePanel.add(backButton1);

    }

    public void setAboutPanel() throws IOException {//починить тут
        aboutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        aboutPanel.setSize(Game.WIDTH, Game.HEIGHT);
        aboutPanel.setBackground(Color.WHITE);
        aboutPanel.setCursor(Game.CURSOR_DEFAULT);

        String text = "";
        System.out.println(aboutFile);
        System.out.println(ABOUT_FILE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(aboutFile)));
        String line = " ";
        while (line != null) {
            text = text + line + "\n";
            line = reader.readLine();
        }
        reader.close();
        JTextPane txtPane = new JTextPane();
        txtPane.setText(text);
        txtPane.setEditable(false);

        StyledDocument doc = txtPane.getStyledDocument();

        SimpleAttributeSet body = new SimpleAttributeSet();
        SimpleAttributeSet header = new SimpleAttributeSet();

        StyleConstants.setAlignment(body, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(body, 16);

        StyleConstants.setFontSize(header, 24);
        StyleConstants.setBold(header, true);
        StyleConstants.setAlignment(header, StyleConstants.ALIGN_CENTER);

        doc.setParagraphAttributes(7, doc.getLength(), body, false);
        doc.setParagraphAttributes(0, 7, header, false);

        JScrollPane scrollPane = new JScrollPane(txtPane);
        scrollPane.setPreferredSize(new Dimension(850, HEIGHT - 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        aboutPanel.add(scrollPane);
        aboutPanel.add(backButton2);

    }

    private JButton createButton(String[] iconFiles) {
        ImageIcon[] icon = new ImageIcon[iconFiles.length];
        for (int i = 0; i < iconFiles.length; ++i)
            icon[i] = new ImageIcon(getClass().getResource(iconFiles[i]));

        JButton button = new JButton(icon[0]);
        button.setBorderPainted(false);
        button.setFocusable(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setRolloverIcon(icon[1]);
        button.setPressedIcon(icon[2]);
        button.setContentAreaFilled(false);
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()
             .equals(playButton))
            setState(GameState.NEW);

        else if (e.getSource()
                  .equals(highScoreButton))
            setState(GameState.HIGHSCORE);

        else if (e.getSource()
                  .equals(backButton1) || e.getSource()
                                           .equals(backButton2)) {
            remove(((JButton) e.getSource()).getParent());
            repaint();
            add(menuPanel);
            pack();
            setState(GameState.WAIT);
        } else if (e.getSource()
                    .equals(quitButton))
            setState(GameState.QUIT);

        else if (e.getSource()
                  .equals(aboutButton))
            setState(GameState.CREDITS);
    }


    public void startGame() {

        Thread t = new Thread(() -> {
            setState(GameState.WAIT);
            add(menuPanel);
            GameBoard board = null;

            while (!gameState.equals(GameState.QUIT)) {
                switch (gameState) {

                    case WAIT:
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;

                    case NEW:
                        remove(menuPanel);
                        repaint();

                        try {
                            board = new GameBoard();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        add(board);
                        pack();
                        board.gameLoop();
                        setState(GameState.WAIT);
                        break;

                    case CONTINUE:

                        try {
                            saveHighScore();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        remove(board);
                        repaint();

                        try {
                            board = new GameBoard();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        add(board);
                        repaint();
                        pack();
                        board.gameLoop();
                        setState(GameState.WAIT);
                        break;

                    case OVER:

                        try {
                            saveHighScore();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        remove(board);
                        repaint();
                        add(menuPanel);
                        repaint();
                        pack();
                        setState(GameState.WAIT);
                        break;
                    case HIGHSCORE:
                        txtHighScore.setText(Integer.toString(getHighScore()));
                        remove(menuPanel);
                        repaint();
                        add(highScorePanel);
                        repaint();
                        pack();
                        setState(GameState.WAIT);
                        break;

                    case CREDITS:
                        remove(menuPanel);
                        repaint();
                        add(aboutPanel);
                        repaint();
                        pack();
                        setState(GameState.WAIT);
                        break;
                    case QUIT:
                        break;
                }
            }
            dispose();
            System.exit(0);
        });
        t.start();
    }
}
