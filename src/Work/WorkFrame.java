package Work;

import Simulation.SimulationInitializer;
import Simulation.SimulationState;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static Work.Models.radius;
import static Work.Models.xOffset;
import static Work.Models.yOffset;

public class WorkFrame<Model extends Models> extends JFrame {
    private final JPanel panelSet;
    private final JPanel panelGet;
    private final SimulationRunner simulationRunner;
    private final Model model;
    private int width = 500;
    private int height = 500;

    int border = 15;
    private boolean showParticles;
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

        for (PointsParameters pp: this.model.points_parameters){
        GasPropertiesPanel gasPropertiesPanel = new GasPropertiesPanel(pp);
        gasPropertiesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gasPropertiesPanel.setMaximumSize(new Dimension(300, gasPropertiesPanel.getPreferredSize().height));
        controlPanel.add(gasPropertiesPanel);

        controlPanel.add(Box.createVerticalStrut(10));}

        // Width и Height
        JComponent[] widthControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "X", 500);
        controlPanel.add(Box.createVerticalStrut(10));
        JComponent[] heightControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "Y", 500);

        // Прокрутка
        JScrollPane scrollPane = new JScrollPane(controlPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // Кнопка Старт/Стоп
        JButton startStopButton = new JButton("Старт");
        AtomicBoolean running = new AtomicBoolean(false);

        startStopButton.addActionListener(e -> {
            if (!running.get()) {
//                int molarMass = ((JSlider) massControl[0]).getValue();
//                int temperature = ((JSlider) tempControl[0]).getValue();
//                int particleCount = ((JSlider) countControl[0]).getValue();
//                int width_work = ((JSlider) widthControl[0]).getValue();
//                int height_work = ((JSlider) heightControl[0]).getValue();
//
//                if (molarMass >= 1 && particleCount >= 1) {
//                    startStopButton.setText("Стоп");
//                    running.set(true);
//                    toggleControls(massControl, tempControl, countControl, widthControl, heightControl, comboBox, false);
//                    showParticles = true;
//                    SimulationState state = SimulationInitializer.init(
//                            width_work,   // ширина окна
//                            height_work,
//                            new PointsParameters[]{new PointsParameters(molarMass,    // molar mass (например, азот)
//                                    temperature,    // температура (например, 25°C)
//                                    particleCount, Color.GREEN)});
//
//                    simulationRunner.start(state);
            } else {
                running.set(false);
                showParticles = false;
                startStopButton.setText("Старт");
                simulationRunner.stop();
                panelGet.repaint();
            }
        });

        // Обновление размеров
        ((JSlider) widthControl[0]).addChangeListener(e -> {
            width = ((JSlider) widthControl[0]).getValue();
            repaint();
        });

        ((JSlider) heightControl[0]).addChangeListener(e -> {
            height = ((JSlider) heightControl[0]).getValue();
            repaint();
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(startStopButton, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(xOffset, yOffset, width + radius * 2, height + radius * 2);
                g.setColor(Color.BLACK);
                if (showParticles && materialPoints != null) {
                    for (MaterialPoint point : materialPoints) {
                        g.setColor(point.getColor());
                        g.fillOval(point.getX() + xOffset, point.getY() + yOffset, radius * 2, radius * 2);
                    }
                }
                g.setColor(Color.BLACK);
            }
        };
        panel.setPreferredSize(new Dimension(700, 700));
        return panel;
    }

    public void updateParticles(Vector<MaterialPoint> points) {
        this.materialPoints = points;
        panelGet.repaint();
    }

    private void drawFrame(Graphics g) {
        g.fillRect(xOffset - border, yOffset - border, width + border + radius * 2, border);
        g.fillRect(xOffset - border, yOffset - border, border, height + border + radius * 2);
        g.fillRect(xOffset + width + radius * 2, yOffset - border, border, height + border * 2 + radius * 2);
        g.fillRect(xOffset - border, yOffset + height + radius * 2, width + border + radius * 2, border);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, xOffset - border, 2000);
        g.fillRect(xOffset - border, 0, 2000, yOffset - border);
        g.fillRect(xOffset - border, height + yOffset + radius * 2 + border, 1000, 1000);
        g.fillRect(xOffset + width + radius * 2 + border, yOffset - border, 1000, 1000);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 10, 5000);
    }

    private JComponent[] createSliderSpinner(JPanel parent, int min, int max, int major, int minor, String labelText, int defaultValue) {
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
        });

        spinner.addChangeListener(e -> {
            int val = (Integer) spinner.getValue();
            slider.setValue(Math.max(min, Math.min(max, val)));
        });

        panel.add(slider);
        parent.add(panel);
        return new JComponent[]{slider, spinner};
    }

}