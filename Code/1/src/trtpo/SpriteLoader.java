package trtpo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteLoader {
    public SpriteLoader() {
    }

    public static BufferedImage[] createSprites(String filePath, int row, int col) throws IOException {
        if (row <= 0 || col <= 0)
            throw new IllegalArgumentException("row and col must be positive!");

        BufferedImage[] sprites = new BufferedImage[row * col];

        BufferedImage spriteSheet = ImageIO.read(SpriteLoader.class.getResourceAsStream(filePath));

        int imageWidth = spriteSheet.getWidth() / col;
        int imageHeight = spriteSheet.getHeight() / row;

        for (int i = 0; i < row; ++i)
            for (int j = 0; j < col; ++j) {
                sprites[(i * col) + j] = spriteSheet.getSubimage(j * imageWidth, i * imageHeight, imageWidth, imageHeight);
            }
        return sprites;
    }
}
