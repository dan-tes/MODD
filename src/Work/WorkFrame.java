package Work;

import Simulation.SimulationInitializer;
import Simulation.SimulationState;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkFrame extends JFrame {
    private final JPanel panelSet;
    private final JPanel panelGet;
    private final SimulationRunner simulationRunner;
    private int width = 500;
    private int height = 500;
    private int radius;
    private final int xOffset = 80;
    private final int yOffset = 80;
    int border = 15;
    private boolean showParticles;
    private Vector<MaterialPoint> materialPoints;

    public WorkFrame(SimulationRunner simulationRunner) {
        this.simulationRunner = simulationRunner;
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
        panel.setPreferredSize(new Dimension(300, 700));
        JPanel controlPanel = new JPanel(new FlowLayout());
        int lenElements = 60;
        Scanner scanner = new Scanner("Азот (N2)|28\nАммиак (NH3)|17\nАргон (Ar)|40\nАцетилен (C2H2)|2\nАцетон (C3H6O)|58\nН-бутан (C4H10)|58\nИзо-бутан (C4HJ0)|58\nН-бутиловый спирт (C4HJ0O)|74\nВода (H2O)|18\nВодород (H2)|2\nВоздух (сухой)|29\nН-гексан (C6HJ4)|86\nГелий (He)|4\nН-гептан (C7HJ6)|100\nДвуокись углерода (CO2)|44\nН-декан (C10H22)|142\nДифенил (C12H10)|154\nДифениловый эфир (CJ2H10O)|169\nДихлорметан (CH2Cl2)|85\nДиэтиловый эфир (C4H10O)|74\nЗакись азота (N2O)|44\nЙодистый водород (HJ)|128\nКислород (O2)|32\nКриптон (Kr)|83\nКсенон (Xe)|131\nМетан (CH4)|16\nМетиламин (CH5N)|31\nМетиловый спирт (CH4O)|32\nНеон (Ne)|20\nНитрозилхлорид (NOCl)|65\nОзон (O3)|48\nОкись азота (NO)|30\nОкись углерода (CO)|28\nН-октан (C8H18)|114\nН-пентан (C5H12)|72\nИзо-пентан (C5H12)|72\nПропан (C3H8)|44\nПропилен (C3H6)|42\nСеленовая кислота (H2Se)|81\nСернистый газ (SO2)|64\nСернистый ангидрид (SO3)|80\nСероводород (H2S)|34\nФосфористый водород (PH3)|34\nФреон 11 (CF3CI)|137\nФреон-12 (CF2CI2)|121\nФреон-13 (CFCI3)|114\nФтор (F2)|38\nФтористый кремний (SiF4)|104\nФтористый метил (CH3F)|34\nХлор (Cl2)|71\nХлористый водород (HCl)|36\nХлористый метил (CH3Cl)|50\nХлороформ (CHCl3)|119\nЦиан (C2N2)|52\nЦианистая кислота (HCN)|27\nЭтан (C2H6)|30\nЭтиламин (C2H7N)|45\nЭтилен (C2H4)|28\nЭтиловый спирт (C2H6O)|46\nХлористый этил (C2H5Cl)|65\n");
        String[] items = new String[lenElements];
        for (int i = 0; i < lenElements; i++) {
            items[i] = scanner.nextLine();
        }
        scanner.close();
        items[0] = "Свои настройки";

        JComboBox<String> comboBox = new JComboBox<>(items);
        controlPanel.add(comboBox);

        JComponent[] massControl = createSliderSpinner(controlPanel, 0, 1000, 100, 20, "Молярная масса г/моль", 100);
        JComponent[] tempControl = createSliderSpinner(controlPanel, -273, 447, 60, 10, "Температура C", 20);
        JComponent[] countControl = createSliderSpinner(controlPanel, 0, 200, 20, 10, "Количество молекул", 50);
        JComponent[] widthControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "X", 500);
        JComponent[] heightControl = createSliderSpinner(controlPanel, 5, 555, 50, 10, "Y", 500);

        AtomicBoolean manualMode = new AtomicBoolean(true);
        comboBox.addActionListener(e -> {
            String selected = comboBox.getItemAt(comboBox.getSelectedIndex());
            if (!Objects.equals(selected, "Свои настройки")) {
                int value = Integer.parseInt(selected.substring(selected.indexOf("|") + 1).trim());
                ((JSlider) massControl[0]).setValue(value);
                manualMode.set(false);
            }
        });

        ((JSlider) massControl[0]).addChangeListener(e -> {
            if (!manualMode.get()) {
                comboBox.setSelectedIndex(0);
                manualMode.set(true);
            }
        });

        ((JSpinner) massControl[1]).addChangeListener(e -> {
            if (!manualMode.get()) {
                comboBox.setSelectedIndex(0);
                manualMode.set(true);
            }
        });

        JButton startStopButton = new JButton("Старт");
        AtomicBoolean running = new AtomicBoolean(false);

        ((JSlider) widthControl[0]).addChangeListener(e -> {
            width = ((JSlider) widthControl[0]).getValue();
            repaint();
        });
        ((JSlider) heightControl[0]).addChangeListener(e -> {
            height = ((JSlider) heightControl[0]).getValue();
            repaint();
        });

        startStopButton.addActionListener(e -> {
            if (!running.get()) {
                int molarMass = ((JSlider) massControl[0]).getValue();
                int temperature = ((JSlider) tempControl[0]).getValue();
                int particleCount = ((JSlider) countControl[0]).getValue();
                int width_work = ((JSlider) widthControl[0]).getValue();
                int height_work = ((JSlider) heightControl[0]).getValue();

                if (molarMass >= 1 && particleCount >= 1) {
                    startStopButton.setText("Стоп");
                    running.set(true);
                    toggleControls(massControl, tempControl, countControl, widthControl, heightControl, comboBox, false);
                    showParticles = true;
                    radius = molarMass / 100 + 2;
                    SimulationState state = SimulationInitializer.init(
                            width_work,   // ширина окна
                            height_work,
                            new PointsParameters[]{new PointsParameters(molarMass,    // molar mass (например, азот)
                                    temperature,    // температура (например, 25°C)
                                    particleCount)});

                    simulationRunner.start(state);
                }
            } else {
                running.set(false);
                showParticles = false;
                startStopButton.setText("Старт");
                toggleControls(massControl, tempControl, countControl, widthControl, heightControl, comboBox, true);
                simulationRunner.stop();
                panelGet.repaint();
            }
        });

        panel.add(controlPanel, BorderLayout.CENTER);
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
                        g.fillOval(point.getX() + xOffset, point.getY() + yOffset, radius * 2, radius * 2);
                    }
                }
                drawFrame(g);

            }
        };
        panel.setPreferredSize(new Dimension(700, 700));
        return panel;
    }

    private void toggleControls(JComponent[] a, JComponent[] b, JComponent[] c, JComponent[] d, JComponent[] e, JComboBox<?> comboBox, boolean state) {
        comboBox.setEnabled(state);
        for (JComponent[] group : new JComponent[][]{a, b, c, d, e}) {
            for (JComponent comp : group) comp.setEnabled(state);
        }
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
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(350, 80));

        JLabel label = new JLabel(labelText);
        panel.add(label);

        JSlider slider = new JSlider(min, max, defaultValue);
        slider.setMajorTickSpacing(major);
        slider.setMinorTickSpacing(minor);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(300, 50));

        JSpinner spinner = new JSpinner();
        spinner.setValue(defaultValue);
        spinner.setPreferredSize(new Dimension(50, 25));

        slider.addChangeListener(e -> {
            int val = slider.getValue();
            spinner.setValue(Math.max(min, Math.min(max, val)));
        });

        spinner.addChangeListener(e -> {
            int val = (Integer) spinner.getValue();
            slider.setValue(Math.max(min, Math.min(max, val)));
        });

        panel.add(spinner);
        panel.add(slider);
        parent.add(panel);
        return new JComponent[]{slider, spinner};
    }

}
