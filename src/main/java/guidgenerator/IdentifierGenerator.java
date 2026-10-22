package guidgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class IdentifierGenerator extends JFrame {

    public IdentifierGenerator() {
        initUI();
    }

    private void initUI() {
        setTitle("Генератор идентификаторов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 120);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton generateButton = new JButton("GUID");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewGuid();
            }
        });

        add(generateButton);

        setLocationRelativeTo(null);
    }

    private void generateNewGuid() {
        String guid = UUID.randomUUID().toString().toUpperCase();
        copyToClipboard(guid);
        showCopiedMessage(guid);
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);
    }

    private void showCopiedMessage(String guid) {
        JOptionPane.showMessageDialog(
                IdentifierGenerator.this,
                "GUID скопирован в буфер обмена:\n\n" + guid,
                "Готово",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
