
package trtpo;

import java.awt.image.BufferedImage;

import static trtpo.Constants.*;

public class GreenAlien extends Alien {

    public GreenAlien(BufferedImage[] frames, int frameLivingLimit, int x, int y) {
        super(frames, frameLivingLimit, x, y);
        setMoveSpeed(GREEN_ALIEN_MOVE_SPEED);
        setScorePoint(GREEN_ALIEN_SCORE_POINTS);
        setHitPoints(GREEN_ALIEN_HIT_POINTS);
    }


    @Override
    public void shooting() {
        manIsDown = true;
    }

}
