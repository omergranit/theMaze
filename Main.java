import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // כל תוכנית Swing צריכה להיפתח בצורה הזו
        SwingUtilities.invokeLater(() -> new SettingsWindow());
    }
}
