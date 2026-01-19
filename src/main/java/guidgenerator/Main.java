package guidgenerator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Запускаем в Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUIDGenerator app = new GUIDGenerator();
                app.setVisible(true);
            }
        });
    }
}