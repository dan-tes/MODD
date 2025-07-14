package Work;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class GasPropertiesPanel extends JPanel {
    private final JComboBox<String> comboBox;
    private final JSlider massSlider, tempSlider, countSlider;
    private final JSpinner massSpinner, tempSpinner, countSpinner;
    private final AtomicBoolean manualMode = new AtomicBoolean(true);

    public GasPropertiesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBackground(Color.RED);
        // Устанавливаем максимальную ширину панели

        // Сканируем вещества
        Scanner scanner = new Scanner("Азот (N2)|28\nАммиак (NH3)|17\nАргон (Ar)|40\nАцетилен (C2H2)|2\nАцетон (C3H6O)|58\nН-бутан (C4H10)|58\nИзо-бутан (C4HJ0)|58\nН-бутиловый спирт (C4HJ0O)|74\nВода (H2O)|18\nВодород (H2)|2\nВоздух (сухой)|29\nН-гексан (C6HJ4)|86\nГелий (He)|4\nН-гептан (C7HJ6)|100\nДвуокись углерода (CO2)|44\nН-декан (C10H22)|142\nДифенил (C12H10)|154\nДифениловый эфир (CJ2H10O)|169\nДихлорметан (CH2Cl2)|85\nДиэтиловый эфир (C4H10O)|74\nЗакись азота (N2O)|44\nЙодистый водород (HJ)|128\nКислород (O2)|32\nКриптон (Kr)|83\nКсенон (Xe)|131\nМетан (CH4)|16\nМетиламин (CH5N)|31\nМетиловый спирт (CH4O)|32\nНеон (Ne)|20\nНитрозилхлорид (NOCl)|65\nОзон (O3)|48\nОкись азота (NO)|30\nОкись углерода (CO)|28\nН-октан (C8H18)|114\nН-пентан (C5H12)|72\nИзо-пентан (C5H12)|72\nПропан (C3H8)|44\nПропилен (C3H6)|42\nСеленовая кислота (H2Se)|81\nСернистый газ (SO2)|64\nСернистый ангидрид (SO3)|80\nСероводород (H2S)|34\nФосфористый водород (PH3)|34\nФреон 11 (CF3CI)|137\nФреон-12 (CF2CI2)|121\nФреон-13 (CFCI3)|114\nФтор (F2)|38\nФтористый кремний (SiF4)|104\nФтористый метил (CH3F)|34\nХлор (Cl2)|71\nХлористый водород (HCl)|36\nХлористый метил (CH3Cl)|50\nХлороформ (CHCl3)|119\nЦиан (C2N2)|52\nЦианистая кислота (HCN)|27\nЭтан (C2H6)|30\nЭтиламин (C2H7N)|45\nЭтилен (C2H4)|28\nЭтиловый спирт (C2H6O)|46\nХлористый этил (C2H5Cl)|65\n");
        String[] items = new String[61];
        items[0] = "Свои настройки";
        for (int i = 1; i < 61; i++) items[i] = scanner.nextLine();
        scanner.close();

        // Настраиваем JComboBox
        comboBox = new JComboBox<>(items);
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(comboBox);
        add(Box.createVerticalStrut(10)); // Отступ между компонентами

        // Добавляем панели со слайдерами и спиннерами
        add(makeCombinedLine("Молярная масса г/моль",
                massSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1)),
                massSlider = new JSlider(0, 1000, 100)));
        add(Box.createVerticalStrut(10)); // Отступ

        add(makeCombinedLine("Температура °C",
                tempSpinner = new JSpinner(new SpinnerNumberModel(20, -273, 447, 1)),
                tempSlider = new JSlider(-273, 447, 20)));
        add(Box.createVerticalStrut(10)); // Отступ

        add(makeCombinedLine("Количество молекул",
                countSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 200, 1)),
                countSlider = new JSlider(0, 200, 50)));

        // Обработчик для JComboBox
        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            if (!Objects.equals(selected, "Свои настройки")) {
                int value = Integer.parseInt(selected.substring(selected.indexOf("|") + 1).trim());
                massSlider.setValue(value);
                manualMode.set(false);
            }
        });

        bindSliderAndSpinner(massSlider, massSpinner);
        bindSliderAndSpinner(tempSlider, tempSpinner);
        bindSliderAndSpinner(countSlider, countSpinner);
    }

    private JPanel makeCombinedLine(String label, JSpinner spinner, JSlider slider) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(300, 80)); // Фиксированная ширина и высота для панели

        // Панель для метки и спиннера
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        spinner.setPreferredSize(new Dimension(80, 25)); // Фиксированный размер спиннера
        top.add(lbl);
        top.add(spinner);
        panel.add(top);

        // Настройка слайдера
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing((slider.getMaximum() - slider.getMinimum()) / 5);
        slider.setPreferredSize(new Dimension(300, 40)); // Уменьшенная ширина слайдера
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(slider);

        return panel;
    }

    private void bindSliderAndSpinner(JSlider slider, JSpinner spinner) {
        slider.addChangeListener(e -> {
            if (!manualMode.get()) {
                manualMode.set(true);
            }
            spinner.setValue(slider.getValue());
        });

        spinner.addChangeListener(e -> {
            if (!manualMode.get()) {
                manualMode.set(true);
            }
            slider.setValue((Integer) spinner.getValue());
        });
    }

    public int getMolarMass() {
        return massSlider.getValue();
    }

    public int getTemperature() {
        return tempSlider.getValue();
    }

    public int getParticleCount() {
        return countSlider.getValue();
    }

    public void setEnabledAll(boolean enabled) {
        comboBox.setEnabled(enabled);
        massSlider.setEnabled(enabled);
        tempSlider.setEnabled(enabled);
        countSlider.setEnabled(enabled);
        massSpinner.setEnabled(enabled);
        tempSpinner.setEnabled(enabled);
        countSpinner.setEnabled(enabled);
    }
}