package Graphics;

import Simulation.Handlers.DrawFunc;
import Simulation.SimulationInitializer;
import Simulation.SimulationRunner;
import Simulation.SimulationState;
import Structures.MaterialPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.Gson;


import Model.Models;

import static Model.Models.*;

public class FrameMKT<Model extends Models> extends JFrame {
    private final JPanel panelSet;
    private final JPanel panelGet;
    private final SimulationRunner simulationRunner;
    private final Model model;
    private Vector<MaterialPoint> materialPoints;
    Vector<JComponent[]> custom_set_panels = new Vector<>();


    public FrameMKT(SimulationRunner simulationRunner, Model model) {
        setUndecorated(true);

        this.simulationRunner = simulationRunner;
        this.model = model;

        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Добавляем заголовок с названием
        mainPanel.add(new WindowHeader(model.getDescription(), this), BorderLayout.NORTH);

        // Создание и добавление основных панелей
        panelSet = createControlPanel();
        panelGet = createSimulationPanel();

        mainPanel.add(panelSet, BorderLayout.WEST);
        mainPanel.add(panelGet, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setSize(1000, 700);
        setVisible(true);
    }


    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(330, 700));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GasPropertiesPanel[] gasPropertiesPanels = new GasPropertiesPanel[this.model.getPointsParameters().length];
        for (int i = 0; i < this.model.getPointsParameters().length; i++) {
            gasPropertiesPanels[i] = new GasPropertiesPanel(this.model.getPointsParameters()[i]);
            gasPropertiesPanels[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            gasPropertiesPanels[i].setMaximumSize(new Dimension(300, gasPropertiesPanels[i].getPreferredSize().height));
            controlPanel.add(gasPropertiesPanels[i]);
            controlPanel.add(Box.createVerticalStrut(10));
        }

        // Прокрутка
        JScrollPane scrollPane = new JScrollPane(controlPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // Кнопка Старт/Стоп
        JButton startStopButton = new JButton("Старт");

        startStopButton.addActionListener(_ -> {
            if (!simulationRunner.isRunning()) {
                startStopButton.setText("Стоп");
                toggleControls(gasPropertiesPanels, false);
                SimulationState state = SimulationInitializer.init(
                        model.getWeight(),
                        model.getHeight(), model);

                simulationRunner.start(state);
            } else {
                toggleControls(gasPropertiesPanels, true);
                startStopButton.setText("Старт");
                simulationRunner.stop();
                panelGet.repaint();
            }
        });

        List<CustomSlider> customSliders = model.getCustomSliders();
        for (CustomSlider slider : customSliders) {
            custom_set_panels.add(createSliderSpinner(controlPanel,
                    slider.min(),
                    slider.max(),
                    slider.majorTick(),
                    slider.minorTick(),
                    slider.label(),
                    slider.defaultValue(),
                    slider.consumer()));
            controlPanel.add(Box.createVerticalStrut(10));
        }
        JButton statisticsButton = new JButton("Статистика");


        statisticsButton.addActionListener(_ -> {
            try {

                String json = simulationRunner.getJSON();

                // 2️⃣ Записываем JSON в файл
                File jsonFile = new File("data.json");
                try (FileWriter writer = new FileWriter(jsonFile)) {
                    writer.write(json);
                }

                // 3️⃣ Запускаем Python для построения графика
                ProcessBuilder pb = new ProcessBuilder("python", "plot_stats.py");
                pb.redirectErrorStream(true);
                Process process = pb.start();

                // 4️⃣ Читаем вывод Python (для отладки)
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("Python завершился с кодом " + exitCode);
                }

                // 5️⃣ Отображаем график в отдельном окне
                ImageIcon icon = new ImageIcon("chart.png");
                JLabel label = new JLabel(icon);
                JFrame chartFrame = new JFrame("График статистики");
                chartFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        // Удаляем файлы
                        File jsonFile = new File("data.json");
                        if (jsonFile.exists()) jsonFile.delete();

                        File chartFile = new File("chart.png");
                        if (chartFile.exists()) chartFile.delete();
                    }
                });
                chartFrame.add(label);
                chartFrame.pack();
                chartFrame.setLocationRelativeTo(null);
                chartFrame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка при построении графика: " + e.getMessage());
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(startStopButton);
        buttonPanel.add(statisticsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void toggleControls(GasPropertiesPanel[] gasPropertiesPanels, boolean b) {
        for (GasPropertiesPanel gasPropertiesPanel : gasPropertiesPanels) {
            gasPropertiesPanel.setEnabledAll(b);
        }
        for (JComponent[] components : custom_set_panels) {
            for (JComponent component : components) {
                component.setEnabled(b);
            }
        }
        repaint();
    }


    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 2000, 2000);
                for (DrawFunc func : model.getDrawFunc())
                    func.draw(g, model.getWeight(), model.getHeight());
                if (simulationRunner.isRunning() && materialPoints != null) {
                    for (MaterialPoint point : materialPoints) {
                        g.setColor(point.getColor());
                        g.fillOval(point.getX() + xOffset, point.getY() + yOffset, radius * 2, radius * 2);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(700, 700));
        return panel;
    }

    public void updateParticles(Vector<MaterialPoint> points) {
        this.materialPoints = points;
        panelGet.repaint();
    }


    private JComponent[] createSliderSpinner(JPanel parent, int min, int max, int major, int minor, String labelText, int defaultValue, Consumer<Integer> consumer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(300, 100));

        // Верхняя панель: метка + спиннер
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setMaximumSize(new Dimension(300, 30));
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        JSpinner spinner = new JSpinner();
        spinner.setValue(defaultValue);
        spinner.setPreferredSize(new Dimension(80, 25));

        topPanel.add(label);
        topPanel.add(spinner);
        panel.add(topPanel);

        // Слайдер
        JSlider slider = new JSlider(min, max, defaultValue);
        slider.setMajorTickSpacing(major);
        slider.setMinorTickSpacing(minor);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMaximumSize(new Dimension(300, 50));
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Связь слайдера и спиннера
        slider.addChangeListener(_ -> {
            int val = slider.getValue();
            spinner.setValue(Math.max(min, Math.min(max, val)));
            consumer.accept(slider.getValue());
        });

        spinner.addChangeListener(_ -> {
            int val = (Integer) spinner.getValue();
            slider.setValue(Math.max(min, Math.min(max, val)));
            consumer.accept(slider.getValue());
            repaint();
        });
        panel.add(slider);
        parent.add(panel);
        return new JComponent[]{slider, spinner};
    }

    @Override
    public void dispose() {
        super.dispose();
        simulationRunner.stop();

    }


}