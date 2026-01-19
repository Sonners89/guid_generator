package guidgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class GUIDGenerator  extends JFrame {

    private JTextArea guidArea;

    public GUIDGenerator() {
        initUI();
        generateNewGuid();
    }

    private void initUI() {
        setTitle("Генератор GUID");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);
        setLayout(new BorderLayout());

        // Текстовое поле для отображения GUID
        guidArea = new JTextArea();
        guidArea.setFont(new Font("Arial", Font.BOLD, 14));
        guidArea.setEditable(false);
        guidArea.setLineWrap(true);
        guidArea.setWrapStyleWord(true);
        guidArea.setMargin(new Insets(10, 10, 10, 10));

        // Добавляем скролл для текстового поля
        JScrollPane scrollPane = new JScrollPane(guidArea);

        // Панель для кнопки
        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Сгенерировать GUID");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Обработчик нажатия кнопки
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewGuid();
            }
        });

        // Добавляем компоненты на форму
        buttonPanel.add(generateButton);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Центрируем окно
        setLocationRelativeTo(null);
    }

    private void generateNewGuid() {
        // Генерируем новый GUID
        String guid = UUID.randomUUID().toString().toUpperCase();
        guidArea.setText(guid);

        // Копируем в буфер обмена
        StringSelection stringSelection = new StringSelection(guid);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);

        // Показываем сообщение (опционально, можно убрать)
        JOptionPane.showMessageDialog(this,
                "GUID скопирован в буфер обмена!",
                "Готово",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
