package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI extends Thread {


    public void run() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(500,500);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);

        Button createAcc = new Button();
        Button login = new Button();


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GUI());
    }
}
