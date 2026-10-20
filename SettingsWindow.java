import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

// זהו החלון הראשון שהמשתמש רואה כשמריצים את התוכנית
public class SettingsWindow extends JFrame {

    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 100;
    private static final int DEFAULT_SIZE = 30;

    private JLabel wallColorLabel;
    private JLabel pathColorLabel;
    private JLabel drawGridLabel;
    private JLabel gridColorLabel;
    private JLabel delayLabel;

    private JTextField widthField;
    private JTextField heightField;

    private RenderConfig currentConfig; // ההגדרות האחרונות שהתקבלו מהשרת

    public SettingsWindow() {
        setTitle("הגדרות מבוך");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 5, 5));

        // שורות שמציגות את ההגדרות שמתקבלות מהשרת
        add(new JLabel("Wall Color:"));
        wallColorLabel = new JLabel("-");
        add(wallColorLabel);

        add(new JLabel("Path Color:"));
        pathColorLabel = new JLabel("-");
        add(pathColorLabel);

        add(new JLabel("Draw Grid:"));
        drawGridLabel = new JLabel("-");
        add(drawGridLabel);

        add(new JLabel("Grid Color:"));
        gridColorLabel = new JLabel("-");
        add(gridColorLabel);

        add(new JLabel("Animation Delay (ms):"));
        delayLabel = new JLabel("-");
        add(delayLabel);

        // כפתור לרענון ההגדרות מהשרת
        JButton refreshButton = new JButton("Refresh Config");
        refreshButton.addActionListener(e -> loadConfigFromServer());
        add(refreshButton);
        add(new JLabel("")); // תא ריק כדי לשמור על סידור העמודות

        // שדות לבחירת גודל המבוך
        add(new JLabel("Width:"));
        widthField = new JTextField(String.valueOf(DEFAULT_SIZE));
        add(widthField);

        add(new JLabel("Height:"));
        heightField = new JTextField(String.valueOf(DEFAULT_SIZE));
        add(heightField);

        // כפתור לשליפת המבוך עצמו
        JButton getMazeButton = new JButton("GET MAZE");
        getMazeButton.addActionListener(e -> onGetMazeClicked());
        add(getMazeButton);
        add(new JLabel(""));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // מיד כשהחלון נפתח - שולפים את ההגדרות הראשונות מהשרת
        loadConfigFromServer();
    }

    // שולף הגדרות מהשרת ומעדכן את מה שמוצג בחלון
    private void loadConfigFromServer() {
        // מריצים ברקע (thread נפרד) כדי שהחלון לא "יקפא" בזמן שמחכים לשרת
        new Thread(() -> {
            try {
                RenderConfig config = ConfigFetcher.fetchConfig();
                currentConfig = config;

                // עדכון התצוגה חייב לקרות בחזרה על ה-thread של הממשק הגרפי
                SwingUtilities.invokeLater(() -> updateConfigLabels(config));

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "שגיאה בקבלת הגדרות מהשרת: " + ex.getMessage()));
            }
        }).start();
    }

    private void updateConfigLabels(RenderConfig config) {
        wallColorLabel.setText(config.getWallCellColor());
        pathColorLabel.setText(config.getPathColor());
        drawGridLabel.setText(String.valueOf(config.isDrawGrid()));
        gridColorLabel.setText(config.getGridColor());
        delayLabel.setText(String.valueOf(config.getAnimationDelayMs()));
    }

    // נקרא כשלוחצים על GET MAZE
    private void onGetMazeClicked() {

        if (currentConfig == null) {
            JOptionPane.showMessageDialog(this, "עדיין לא התקבלו הגדרות מהשרת, נסה שוב בעוד רגע");
            return;
        }

        int width = parseSizeOrDefault(widthField.getText());
        int height = parseSizeOrDefault(heightField.getText());

        // מריצים ברקע כדי לא לתקוע את הממשק בזמן ההמתנה לשרת
        new Thread(() -> {
            try {
                BufferedImage image = MazeImageFetcher.fetchMazeImage(width, height);
                boolean[][] maze = MazeParser.parseImage(image, width, height);

                SwingUtilities.invokeLater(() -> new MazeWindow(maze, currentConfig, width, height));

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "שגיאה בקבלת המבוך מהשרת: " + ex.getMessage()));
            }
        }).start();
    }

    // הופך טקסט שהוזן לערך מספרי תקין, או מחזיר 30 אם הערך לא תקין
    private int parseSizeOrDefault(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            if (value < MIN_SIZE || value > MAX_SIZE) {
                return DEFAULT_SIZE;
            }
            return value;
        } catch (NumberFormatException e) {
            return DEFAULT_SIZE;
        }
    }
}
