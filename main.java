import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class main {

    public static void main(String[] args) {
        // Initialize system manager to load data and prepare services
        // This loads all users, modules, classes from files
        SystemManager systemManager = new SystemManager();
        
        // Set up the visual appearance (look and feel) of the GUI
        // This makes buttons and windows look like the operating system style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If look and feel fails, show error but continue
            // silently ignore look-and-feel errors
        }


        SwingUtilities.invokeLater(() -> {
            try {
                AFSLoginFrame loginFrame = new AFSLoginFrame();
                loginFrame.setVisible(true); // Show the login window
            } catch (Exception e) {
                // If GUI creation fails, show error
                // silently ignore GUI creation errors
            }
        });
    }
}
