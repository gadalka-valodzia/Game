
package trtpo;

import java.awt.image.BufferedImage;

import static trtpo.Constants.*;

public class BlueAlien extends Alien {


    private int  shotCount;
    private int limit;

      public BlueAlien(BufferedImage[] frames, int frameLivingLimit, int x, int y) {
        super(frames, frameLivingLimit / 2, x, y);
        setMoveSpeed(BLUE_ALIEN_MOVE_SPEED);
        limit = frameLivingLimit;
        shotCount = 0;
        setScorePoint(BLUE_ALIEN_SCORE_POINTS);
       setHitPoints(BLUE_ALIEN_HIT_POINTS);
    }

    @Override
    public void shooting() {
          if(++shotCount < hitPoints) {
            startFrame = 6;
            livingLimitFrame = limit;
        }
        else
            manIsDown = true;
        repaint();
    }

    @Override
    public int getScorePoint() {
        return this.scorePoint;
    }

}
