package trtpo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import static trtpo.Constants.DEADLINE;

public abstract class Alien extends JPanel implements MouseListener, Creature {

    int scorePoint;
    int hitPoints;
    private int deadLine;
    boolean manIsDown;
    private int x, y, width, height;
    private int moveSpeed;
    private BufferedImage[] alien;
    int livingLimitFrame, deadLimitFrame, currentFrame, startFrame;
    private Timer animationTimer;

    public Alien(BufferedImage[] frames, int frameLivingLimit, int x, int y) {
        setFrames(frames, frameLivingLimit);
        setX(x);
        setY(y);

        width = frames[0].getWidth();
        height = frames[0].getHeight();
        manIsDown = false;
        deadLine = DEADLINE;

        setPreferredSize(new Dimension(width, height));
        setBounds(x, y, width, height);

        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));

        addMouseListener(this);

        animationTimer = new Timer(Game.REFRESH_TIME, e -> {
            if (!GameBoard.gameOver)
                update();
        });
        animationTimer.start();
    }

    public void setFrames(BufferedImage[] frames, int frameLivingLimit) {
        this.alien = frames;
        if (frameLivingLimit > 0) {
            this.livingLimitFrame = frameLivingLimit;
        }
        else {
            throw new IllegalArgumentException("frameLivingLimit CANNOT BE <=0");
        }
        this.deadLimitFrame = frames.length;
        this.startFrame = 0;
        this.currentFrame = startFrame;
    }

    public void setY(int y) {
        if (y >= 0)
            this.y = y;
        else
            throw new IllegalArgumentException("y CANNOT BE <0");
    }

    public void setX(int x) {
        if (x >= 0)
            this.x = x;
        else
            throw new IllegalArgumentException("x CANNOT BE <0");
    }

    public void setHitPoints(int hitPoints) {
        if (hitPoints >= 0)
            this.hitPoints = hitPoints;
        else
            throw new IllegalArgumentException("Value of hitPoints CANNOT BE a negative number!");
    }

    public void setMoveSpeed(int moveSpeed) {
        if (moveSpeed >= 0)
            this.moveSpeed = moveSpeed;
        else
            throw new IllegalArgumentException("moveSpeed CANNOT BE negative!");
    }

    @Override
    public void move() {
        if (!manIsDown) {
            if (x > deadLine)
                x -= moveSpeed;
            else {
                x = deadLine;
                GameBoard.gameOver = true;
                animationTimer.stop();
            }
            setLocation(x, y);
        }
    }

    @Override
    public boolean isAlive() {
        return (currentFrame < deadLimitFrame);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (isAlive())
            g2d.drawImage(alien[currentFrame], 0, 0, null);
        else
            setForeground(new Color(0, 0, 0, 0));
    }

    @Override
    public void update() {
        move();
        if ((++currentFrame == livingLimitFrame) && !manIsDown)
            currentFrame = startFrame;
        else if (manIsDown)
            currentFrame = (currentFrame < livingLimitFrame) ? livingLimitFrame : (currentFrame + 1);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (GameBoard.gameOver)
            return;
        Cannon.setFire(true);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(Game.CURSOR_LOCKED);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Game.CURSOR_UNLOCKED);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (GameBoard.gameOver)
            return;
        Cannon.setFire(true);
        shooting();
        update();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (GameBoard.gameOver)
            return;
        Cannon.setFire(false);
    }

    @Override
    public Timer getAnimationTimer() {
        return animationTimer;
    }

    @Override
    public int getScorePoint() {
        return scorePoint;
    }

    public void setScorePoint(int scorePoint) {
        this.scorePoint = scorePoint;
    }

}
