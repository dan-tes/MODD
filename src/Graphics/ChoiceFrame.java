package Graphics;

import javax.swing.*;
import java.awt.*;
import Model.Models;

class ModelPanel<Model extends Models> extends JPanel {
    ModelPanel(Model model, JFrame frame) {
        setLayout(new BorderLayout());
        JButton run_button = new JButton("Начать");
        run_button.addActionListener(_ -> {
            model.run();
            frame.dispose();
        });
        JLabel description = new JLabel(model.getDescription());
        add(description, BorderLayout.NORTH);
        add(run_button, BorderLayout.SOUTH);
    }
}

public class ChoiceFrame<Model extends Models> extends JFrame {
    public ChoiceFrame(Model[] models) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        for (Model m : models) {
            ModelPanel<Model> mPanel = new ModelPanel(m, this);
            mPanel.setBackground(Color.GREEN);
            add(mPanel, c);
            c.gridx+=2;
        }
        setSize(new Dimension(1000, 700));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}


