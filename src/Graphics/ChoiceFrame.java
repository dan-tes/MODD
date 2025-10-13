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
    public static String description = "Выбор модели";
    public ChoiceFrame(Model[] models) {
        setUndecorated(true);
        setLayout(new BorderLayout());
        add(new WindowHeader(description, this), BorderLayout.NORTH);
        // Добавляем кастомный заголовок окна

        // Основная панель с моделями
        JPanel modelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(5, 5, 5, 5);

        for (Model m : models) {
            ModelPanel<Model> mPanel = new ModelPanel(m, this);
            modelsPanel.add(mPanel, c);
            c.gridx += 1;
            if (c.gridx == 4){
                c.gridx = 0;
                c.gridy += 1;
            }
        }

        add(modelsPanel, BorderLayout.CENTER);
        setSize(new Dimension(1000, 700));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
