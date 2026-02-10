import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class main {

    public static void main(String[] args) {
        SystemManager systemManager = new SystemManager();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            String msg = "Error setting look and feel: " + e.getMessage();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE));
        }


        SwingUtilities.invokeLater(() -> {
            try {
                AFSLoginFrame loginFrame = new AFSLoginFrame(systemManager);
                loginFrame.setVisible(true); // Show the login window
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error creating window: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
