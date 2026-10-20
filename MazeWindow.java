import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.List;

// החלון שמוצג אחרי שלחצו על GET MAZE - מציג את המבוך ומאפשר בדיקת פתרון
public class MazeWindow extends JFrame {

    private final MazePanel mazePanel;
    private final boolean[][] maze;
    private boolean animationRunning = false; // נעילה כדי למנוע כמה אנימציות במקביל

    public MazeWindow(boolean[][] maze, RenderConfig config, int width, int height) {
        this.maze = maze;

        setTitle("המבוך שלכם");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        mazePanel = new MazePanel(maze, config, width, height);
        add(mazePanel, BorderLayout.CENTER);

        JButton checkSolutionButton = new JButton("Check Solution");
        checkSolutionButton.addActionListener(e -> onCheckSolutionClicked(config));
        add(checkSolutionButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onCheckSolutionClicked(RenderConfig config) {

        // אם אנימציה כבר רצה כרגע - לא עושים כלום, מונעים הרצה כפולה
        if (animationRunning) {
            return;
        }

        // מאפסים ציור קודם אם היה
        mazePanel.clearPath();

        List<Point> path = MazeSolver.findPath(maze);

        if (path == null) {
            JOptionPane.showMessageDialog(this, "No solution found");
            return;
        }

        // מריצים אנימציה: כל פעם צובעים משבצת אחת מהנתיב, וממתינים בין משבצת למשבצת
        animationRunning = true;

        int delay = config.getAnimationDelayMs();

        Timer timer = new Timer(delay, null);
        final int[] index = {0}; // "משתנה" שאפשר לשנות בתוך ה-lambda

        timer.addActionListener(e -> {
            if (index[0] < path.size()) {
                mazePanel.revealNextPathCell(path.get(index[0]));
                index[0]++;
            } else {
                timer.stop();
                animationRunning = false; // האנימציה נגמרה - מותר להריץ שוב
            }
        });

        timer.start();
    }
}
