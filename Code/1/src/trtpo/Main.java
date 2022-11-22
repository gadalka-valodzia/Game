package trtpo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            new Game().startGame();
        } catch (HeadlessException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,
                    "We are sorry about that!\nError: " + e.getMessage(),
                    "Opps!! Something went wrong!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "It seems that there is a problem on your file system!\nError: " + e.getMessage(),
                    "Opps!! Something went wrong!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
