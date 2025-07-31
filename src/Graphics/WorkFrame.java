package Graphics;

import Simulation.Handlers.DrawFunc;
import Simulation.SimulationInitializer;
import Simulation.SimulationRunner;
import Simulation.SimulationState;
import Structures.Coordinate;
import Structures.MaterialPoint;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.function.Consumer;

import Model.Models;

import static Model.Models.*;

public class WorkFrame<Model extends Models> extends JFrame {
    private final JPanel panelSet;
    private final JPanel panelGet;
    private final SimulationRunner simulationRunner;
    private final Model model;
    private Vector<MaterialPoint> materialPoints;

    public WorkFrame(SimulationRunner simulationRunner, Model model) {
        this.simulationRunner = simulationRunner;
        this.model = model;
        setLayout(new BorderLayout());
        setTitle("Particle Modeling");

        panelSet = createControlPanel();
        panelGet = createSimulationPanel();

        add(panelSet, BorderLayout.WEST);
        add(panelGet, BorderLayout.CENTER);

        setSize(new Dimension(1000, 700));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // Width и Height
        JComponent[] widthControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "X", model.getWeight(), model::setWeight);
        controlPanel.add(Box.createVerticalStrut(10));
        JComponent[] heightControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "Y", model.getHeight(), model::setHeight);

        // Прокрутка
        JScrollPane scrollPane = new JScrollPane(controlPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // Кнопка Старт/Стоп
        JButton startStopButton = new JButton("Старт");

        startStopButton.addActionListener(_ -> {
            if (!simulationRunner.isRunning()) {
                int width_work = ((JSlider) widthControl[0]).getValue();
                int height_work = ((JSlider) heightControl[0]).getValue();
                startStopButton.setText("Стоп");
                toggleControls(gasPropertiesPanels, widthControl, heightControl, false);
                SimulationState state = SimulationInitializer.init(
                        width_work,
                        height_work, model);

                simulationRunner.start(state);
            } else {
                toggleControls(gasPropertiesPanels, widthControl, heightControl, true);
                startStopButton.setText("Старт");
                simulationRunner.stop();
                panelGet.repaint();
            }
        });

        // Обновление размеров
        ((JSlider) widthControl[0]).addChangeListener(e -> {
            model.setWeight(((JSlider) widthControl[0]).getValue());
            repaint();
            try {
                model.getPiston().setCoordinate(((JSlider) widthControl[0]).getValue(), Coordinate.X);
            } catch (NullPointerException _) {
            }
        });
        ((JSlider) heightControl[0]).addChangeListener(e -> {
            model.setHeight(((JSlider) heightControl[0]).getValue());
            repaint();
            try {
                model.getPiston().setCoordinate(((JSlider) heightControl[0]).getValue(), Coordinate.Y);
            } catch (NullPointerException _) {
            }
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(startStopButton, BorderLayout.SOUTH);

        return panel;
    }

    private void toggleControls(GasPropertiesPanel[] gasPropertiesPanels, JComponent[] widthControl, JComponent[] heightControl, boolean b) {
        for (GasPropertiesPanel gasPropertiesPanel : gasPropertiesPanels) {
            gasPropertiesPanel.setEnabledAll(b);
        }
        widthControl[0].setEnabled(b);
        heightControl[0].setEnabled(b);
        widthControl[1].setEnabled(b);
        heightControl[1].setEnabled(b);
    }


    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
        slider.addChangeListener(e -> {
            int val = slider.getValue();
            spinner.setValue(Math.max(min, Math.min(max, val)));
            consumer.accept(slider.getValue());
        });

        spinner.addChangeListener(e -> {
            int val = (Integer) spinner.getValue();
            slider.setValue(Math.max(min, Math.min(max, val)));
            consumer.accept(slider.getValue());
        });

        panel.add(slider);
        parent.add(panel);
        return new JComponent[]{slider, spinner};
    }

}