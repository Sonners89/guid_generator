package guidgenerator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Запускаем в Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Устанавливаем Look and Feel для более современного вида
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                IdentifierGenerator app = new IdentifierGenerator();
                app.setVisible(true);
            }
        });
    }
}