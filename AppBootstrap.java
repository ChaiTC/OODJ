public class AppBootstrap {
    public static void main(String[] args) {
        // Initialize system manager to load data and prepare services
        SystemManager systemManager = new SystemManager();
        // Launch login frame on the EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            AFSLoginFrame login = new AFSLoginFrame(systemManager);
            login.setVisible(true);
        });
    }
}
