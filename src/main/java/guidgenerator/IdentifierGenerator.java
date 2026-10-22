package guidgenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.UUID;

public class IdentifierGenerator extends JFrame {

    public IdentifierGenerator() {
        initUI();
    }

    private void initUI() {
        setTitle("Генератор идентификаторов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 120);
        setResizable(false);
        setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton guidButton = new JButton("GUID");
        guidButton.setFont(new Font("Arial", Font.BOLD, 14));
        guidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewGuid();
            }
        });

        JButton unixTimeButton = new JButton("unixTime");
        unixTimeButton.setFont(new Font("Arial", Font.BOLD, 14));
        unixTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateUnixTime();
            }
        });

        buttonPanel.add(guidButton);
        buttonPanel.add(unixTimeButton);

        add(buttonPanel);

        setLocationRelativeTo(null);
    }

    private void generateNewGuid() {
        String guid = UUID.randomUUID().toString().toUpperCase();
        copyToClipboard(guid);
        showCopiedMessage("GUID", guid);
    }

    private void generateUnixTime() {
        Instant now = Instant.now();
        long micros = now.getEpochSecond() * 1_000_000L + now.getNano() / 1_000L;
        String value = String.valueOf(micros);
        copyToClipboard(value);
        showCopiedMessage("unixTime", value);
    }

    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(stringSelection, null);
    }

    private void showCopiedMessage(String label, String value) {
        JOptionPane.showMessageDialog(
                IdentifierGenerator.this,
                label + " скопирован в буфер обмена:\n\n" + value,
                "Готово",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
