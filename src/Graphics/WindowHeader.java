package Graphics;

import Model.ApplicationRunner;
import Model.Models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class WindowHeader extends JPanel {
    private Point mouseOffset;

    public WindowHeader(String title, JFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Левая часть - название и кнопка помощи
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftPanel.setOpaque(false);

        // Название окна (слева)
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(titleLabel);

        // Кнопка помощи (справа от названия)
        JButton helpButton = createControlButton("?", new Color(70, 70, 180));
        helpButton.addActionListener(e -> showHelpDialog());
        leftPanel.add(helpButton);

        add(leftPanel, BorderLayout.WEST);

        // Правая часть - кнопки управления
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);

        if (!Objects.equals(title, ChoiceFrame.description)) {
            JButton backButton = createControlButton("—>", new Color(70, 70, 70));

            backButton.addActionListener(e -> {
            frame.dispose(); // от ошибка this.getFrame( нет метода
            new ChoiceFrame(ApplicationRunner.models);
        });
            rightPanel.add(backButton);

        }

        JButton minimizeButton = createControlButton("—", new Color(70, 70, 70));
        minimizeButton.addActionListener(e -> minimizeWindow());
        rightPanel.add(minimizeButton);

        JButton closeButton = createControlButton("×", new Color(200, 60, 60));
        closeButton.addActionListener(e -> closeWindow());


        rightPanel.add(closeButton);

        add(rightPanel, BorderLayout.EAST);

        // Логика перемещения окна
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Window window = SwingUtilities.getWindowAncestor(WindowHeader.this);
                if (window != null && mouseOffset != null) {
                    Point newLocation = e.getLocationOnScreen();
                    newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                    window.setLocation(newLocation);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseOffset = null;
            }
        });
    }

    private JButton createControlButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(
                        Math.min(bgColor.getRed() + 30, 255),
                        Math.min(bgColor.getGreen() + 30, 255),
                        Math.min(bgColor.getBlue() + 30, 255)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void minimizeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof Frame) {
            ((Frame)window).setState(Frame.ICONIFIED);
        }
    }

    private void closeWindow() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    private void showHelpDialog() {
        // Создаем панель для отображения текста справки
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BoxLayout(helpPanel, BoxLayout.Y_AXIS));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Добавляем заголовок
        JLabel title = new JLabel("Справка по работе с приложением");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpPanel.add(title);

        // Добавляем описание выбора модели
        JLabel modelSelectionLabel = new JLabel("Выбор модели:");
        modelSelectionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(modelSelectionLabel);
        JTextArea modelList = new JTextArea();
        modelList.setEditable(false);
        modelList.setText("\n- Стандартная модель\n" +
                "- Биномиальная модель 1\n" +
                "- Биномиальная модель 2\n" +
                "- Модель с поршнем\n" +
                "- Вертикальная модель поршня\n" +
                "- Модель с пружинным поршнем");
        modelList.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(new JScrollPane(modelList));

        // Добавляем описание настройки параметров
        JLabel parameterSetupLabel = new JLabel("Настройка параметров:");
        parameterSetupLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(parameterSetupLabel);
        JTextArea parameterList = new JTextArea();
        parameterList.setEditable(false);
        parameterList.setText("\n- Параметры газов (температура, молярная масса, кол-во молекул)\n" +
                "- Характеристики расстояний между молекулами\n" +
                "- Параметры пружин и их жесткость\n" +
                "- Скорости движения молекул");
        parameterList.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(new JScrollPane(parameterList));

        // Добавляем описание запуска моделирования
        JLabel launchModelingLabel = new JLabel("Запуск моделирования:");
        launchModelingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(launchModelingLabel);
        JTextArea launchDescription = new JTextArea();
        launchDescription.setEditable(false);
        launchDescription.setText("После настройки всех параметров можно запустить процесс моделирования. Программа визуализирует движение молекул и их взаимодействие в соответствии с заданными параметрами.");
        launchDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(new JScrollPane(launchDescription));

        // Добавляем описание моделирования
        JLabel modelingDescriptionLabel = new JLabel("Описание моделирования:");
        modelingDescriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(modelingDescriptionLabel);
        JTextArea modelingDetails = new JTextArea();
        modelingDetails.setEditable(false);
        modelingDetails.setText("\n- Движение молекул и их столкновения в различных ситуациях\n" +
                "- Взаимодействие молекул с поршнями различных типов\n" +
                "- Прохождение молекул через полупроницаемые стенки\n" +
                "- Различные типы молекул и их поведение при разных условиях\n" +
                "- Влияние скоростей молекул на процессы взаимодействия");
        modelingDetails.setFont(new Font("Arial", Font.PLAIN, 12));
        helpPanel.add(new JScrollPane(modelingDetails));

        // Отображаем окно справки
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(this),
                helpPanel,
                "Справка",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
