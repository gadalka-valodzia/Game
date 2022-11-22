package trtpo;

import javax.swing.*;

public interface Creature {
    void move();
    boolean isAlive();
    void shooting();
    void update();
    int getScorePoint();
    Timer getAnimationTimer();
}
