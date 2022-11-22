package trtpo;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static trtpo.FilePaths.BACKGROUND;
import static trtpo.FilePaths.LASER_SOUND;

public class GameBoard extends JPanel {
    private EnemyGenerator generator;
    public static boolean gameOver = false;
    int score;

    private boolean isStopped;

    private AudioClip bangClip;
    private Cannon cannon;
    private JLabel lblScore;
    private JPanel headerPanel;
    private JButton restartButton, stopButton;
    private Timer mainTimer;
    private Font defaultFont;
    private JCheckBox effectOn;


    GameBoard() throws IOException {
        isStopped = false;

        generator = new EnemyGenerator();
        gameOver = false;
        score = 0;
        defaultFont = new Font(Font.SERIF, Font.BOLD, 24);

        setEffectOn();
        
        URL clipUrl = getClass().getResource(LASER_SOUND);
        if(clipUrl == null) {
            effectOn.setSelected(false);
            effectOn.setEnabled(false);
        }
        else
            bangClip = Applet.newAudioClip(clipUrl);
        
        setBounds(0, 0, Game.WIDTH, Game.HEIGHT);
        
        setLayout(null);

        setCursor(Game.CURSOR_UNLOCKED);
        setHeader();
        setCannon();
    }
    private void setEffectOn() {
        effectOn = new JCheckBox("Sound Effects    ", true);
        effectOn.setBackground(new Color(0, 0, 0, 0));
        effectOn.setFont(defaultFont);
        effectOn.setFocusable(false);
    }

    private void setHeader() {
        JLabel scoreMsg = new JLabel("Your Score: ");
        scoreMsg.setForeground(Color.BLACK);
        scoreMsg.setFont(defaultFont);

        lblScore = new JLabel(Integer.toString(score));
        lblScore.setForeground(Color.BLACK);
        lblScore.setFont(defaultFont);

        headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setBounds(0, 0, Game.WIDTH, 50);
        headerPanel.setOpaque(true);
        headerPanel.setBackground(new Color(0, 0, 0, 0));

        headerPanel.add(effectOn);
        headerPanel.add(scoreMsg);
        headerPanel.add(lblScore);

        
        BufferedImage[] buttonIcons= null;
        try {
            buttonIcons = SpriteLoader.createSprites("images/buttons/innerbuttons.png", 2, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert buttonIcons != null;
        ImageIcon[] icons = new ImageIcon[buttonIcons.length];
        for(int i = 0; i < buttonIcons.length; ++i)
            icons[i] = new ImageIcon(buttonIcons[i]);

        restartButton = createButton(icons[0], icons[1], icons[2]);
        stopButton = createButton(icons[3], icons[4], icons[5]);

        headerPanel.add(restartButton);
        headerPanel.add(stopButton);

        add(headerPanel);

        headerPanel.setVisible(true);
    }

    private void setCannon() {
        cannon = new Cannon();
        add(cannon);

        // MouseMotionListener calls Cannon's rotate function to follow cursor.
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                cannon.rotate(e.getX(), e.getY(), false);
            }
            @Override
            public void mouseDragged(MouseEvent arg0) {
            }
        });// End of addMouseMotionListener.

        // When clicked on blank area cannon will be fired with MouseListener.
        // If there is no mouselistener added in the GameBoard,
        // 		cannon be fired only when clicked on Aliens.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Cannon.setFire(true);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                Cannon.setFire(true);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                Cannon.setFire(false);
            }
        });// End of addMouseListener.
    }


    private JButton createButton(Icon icon0, Icon icon1, Icon icon2) {

        JButton button = new JButton(icon0);
        button.setPreferredSize(new Dimension(icon0.getIconWidth(), icon0.getIconHeight()));
        button.setBorderPainted(false);
        button.setFocusable(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setRolloverIcon(icon1);
        button.setPressedIcon(icon2);
        button.setContentAreaFilled(false);
        button.addActionListener(new InnerButtonListener());

        return button;
    }

    private Thread generator() {
        for(int i = 0; i < 4; ++i)
            add(generator.generateEnemy());

        return new Thread(() -> {
            int s = 1000;
            while(!gameOver && !isStopped) {
                try {
                    Thread.sleep(s);
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(null,
                            "We are sorry about that!\nError: " + e.getMessage(),
                            "Opps!! Something went wrong!", JOptionPane.ERROR_MESSAGE);
                }
                if(isStopped)
                    return;
                if(!gameOver)
                    add(generator.generateEnemy());
                if(s > Game.REFRESH_TIME + 50)
                    s -= 4;
                else
                    s = Game.REFRESH_TIME + 50;
            }
        });
    }

    void gameLoop() {
        generator().start();
        mainTimer = new Timer(Game.REFRESH_TIME, arg0 -> {
            repaint();
            if(gameOver) {
                gameOver();
                return;
            }
            for(Component e : getComponents()) {
                if(e instanceof Creature){
                    Creature i = (Creature) e;
                    if(!i.isAlive()) {
                        if(effectOn.isSelected())
                            bangClip.play();
                        score += i.getScorePoint();
                        lblScore.setText(Integer.toString(score));
                        remove(e);
                    }
                }
            }
        });
        mainTimer.start();
    }
    private void gameOver() {
        mainTimer.stop();
        for(Component c : getComponents()) {
            if(c instanceof Creature)
                ((Creature) c).getAnimationTimer().stop();
            remove(c);
        }

        int selectedOption = JOptionPane.showConfirmDialog(this,
                ("Your Score: " + score + "\nDo you want to play a new game?"),
                "GAME OVER", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        Game.setHighScore(score);

        switch (selectedOption) {
            case JOptionPane.YES_OPTION:
                Game.setState(GameState.CONTINUE);
                break;

            case JOptionPane.NO_OPTION:
                Game.setState(GameState.OVER);
                break;
        }
    }
    private class InnerButtonListener implements ActionListener {
        void gameStop() {
            isStopped = true;
            mainTimer.stop();
            for(Component c : getComponents()) {
                if(c instanceof Creature)
                    ((Creature) c).getAnimationTimer().stop();
                remove(c);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == restartButton) {
                gameStop();
                Game.setState(GameState.CONTINUE);
            }
            else if(e.getSource() == stopButton) {
                gameStop();
                Game.setState(GameState.OVER);
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(BACKGROUND)), 0, 0, null);
    }
}
