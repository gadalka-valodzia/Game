package trtpo;

import javax.swing.*;
import java.awt.*;

import static trtpo.Constants.CANNON_HEIGHT;
import static trtpo.Constants.CANNON_WIDTH;
import static trtpo.FilePaths.*;

public class Cannon extends JPanel {

        private Image cannonImage, cannonFireImage;


        private int panelY, cannonY;
        private double angle;
        private static boolean fire;

        public Cannon() {

            panelY = (Game.HEIGHT / 2) - (CANNON_WIDTH / 2) + 50;


            setBounds(0, panelY, CANNON_WIDTH, CANNON_HEIGHT);

            setBackground(Color.WHITE);

            setFire(false);
            setImages();
        }

        private void setImages() {
            cannonImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(CANNON));
            cannonFireImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(CANNON_FIRE));

            cannonY = (CANNON_HEIGHT / 2) - (cannonImage.getHeight(null) / 2) - 15;
            angle = 0;
        }

        public static void setFire(boolean f) {
            fire = f;
        }

        public void rotate(int x, int y, boolean fire) {
            setFire(fire);
            angle = Math.atan2(y - (panelY + cannonY), x);
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;

            g2d.rotate(angle, 15, cannonY + 60);

            if(!fire)
                g2d.drawImage(cannonImage, 0, cannonY, null);
            else
                g2d.drawImage(cannonFireImage, 0, cannonY, null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(CANNON_BACKGROUND)), 0, 0, null);
        }

}
