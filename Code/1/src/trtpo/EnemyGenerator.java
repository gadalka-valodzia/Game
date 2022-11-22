package trtpo;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import static trtpo.Constants.*;
import static trtpo.FilePaths.BLUE_ALIEN;
import static trtpo.FilePaths.GREEN_ALIEN;

public class EnemyGenerator {
    private BufferedImage[] greenAlien, blueAlien;
    private Random random;


    public EnemyGenerator() throws IOException {
        random = new Random();
        greenAlien = SpriteLoader.createSprites(GREEN_ALIEN, NUMBER_OF_ROWS_OF_GREEN_ALIEN, NUMBER_OF_COLUMNS_OF_GREEN_ALIEN);
        blueAlien = SpriteLoader.createSprites(BLUE_ALIEN, NUMBER_OF_ROWS_OF_BLUE_ALIEN, NUMBER_OF_COLUMNS_OF_BLUE_ALIEN);

    }

    public Alien generateEnemy() {
        int typeOfAlien = random.nextInt(2);
        switch (typeOfAlien) {
            case 0:
                return new GreenAlien(greenAlien, GREEN_ALIEN_ALIVE_FRAMES, (random.nextInt(100) + (Game.WIDTH - 100)), random.nextInt(Game.HEIGHT - 200) + 50);
            case 1:
                return new BlueAlien(blueAlien, BLUE_ALIEN_ALIVE_FRAMES, (random.nextInt(100) + (Game.WIDTH - 100)), random.nextInt(Game.HEIGHT - 200) + 50);
            default:
                return null;
        }

    }


}
