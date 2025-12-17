import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class main {

    public static void main(String[] args) {
        // Display a fancy welcome message in the console
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   Assessment Feedback System (AFS) - GUI Starting...        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        // Initialize system manager to load data and prepare services
        // This loads all users, modules, classes from files
        System.out.println("✓ Initializing system manager...");
        SystemManager systemManager = new SystemManager();
        System.out.println("✓ System manager ready");
        System.out.println();

        // Set up the visual appearance (look and feel) of the GUI
        // This makes buttons and windows look like the operating system style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✓ Look and feel configured");
        } catch (Exception e) {
            // If look and feel fails, show error but continue
            System.out.println("✗ Error setting look and feel: " + e.getMessage());
            e.printStackTrace();
        }

        // Launch GUI application on the Event Dispatch Thread (EDT)
        // This is important for Swing - GUI code must run on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("✓ Creating login frame...");
                AFSLoginFrame loginFrame = new AFSLoginFrame();
                System.out.println("✓ Login frame created successfully");
                System.out.println("✓ Making window visible...");
                loginFrame.setVisible(true); // Show the login window
                System.out.println("✓ Window is now visible!");
                System.out.println();
                System.out.println("📝 Login window should appear on your screen.");
                System.out.println("   If you don't see it, check your screen.");
            } catch (Exception e) {
                // If GUI creation fails, show error
                System.out.println("✗ Error creating window: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
