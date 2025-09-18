import Graphics.ChoiceFrame;
import Model.*;
import Physics.WorkMain;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class mainMofPinaG {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Молекулярно кинетическая история");
        button.addActionListener(_ -> {
            new ChoiceFrame(ApplicationRunner.models);
            frame.dispose();
        });
        frame.add(button);
        JButton buttonb = new JButton("Баллистика");
        buttonb.addActionListener(_ -> {
            new WorkMain();
            frame.dispose();
        });
        frame.add(buttonb);
//        new ChoiceFrame(ApplicationRunner.models);
//        new WorkMain();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setTitle("Модулятор");
        frame.setSize(400, 100);
        frame.setVisible(true);
    }
}
