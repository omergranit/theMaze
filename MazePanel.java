import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// הפאנל הזה אחראי אך ורק על ציור המבוך על המסך.
// הוא לא יודע כלום על השרת - הוא רק מקבל נתונים ומצייר אותם.
public class MazePanel extends JPanel {

    private static final int CELL_SIZE = 20; // גודל כל משבצת בפיקסלים - קבוע בקוד, מותר

    private final boolean[][] maze;   // true = מעבר, false = קיר
    private final RenderConfig config;
    private final int mazeWidth;
    private final int mazeHeight;

    // רשימת המשבצות מהפתרון שכבר "נצבעו" עד כה באנימציה
    private List<Point> revealedPathCells = new ArrayList<>();

    public MazePanel(boolean[][] maze, RenderConfig config, int mazeWidth, int mazeHeight) {
        this.maze = maze;
        this.config = config;
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;

        setPreferredSize(new Dimension(mazeWidth * CELL_SIZE, mazeHeight * CELL_SIZE));
    }

    // מוסיף עוד משבצת אחת לנתיב המוצג, ומצייר מחדש
    public void revealNextPathCell(Point cell) {
        revealedPathCells.add(cell);
        repaint();
    }

    // מאפס את הציור של הנתיב (לקראת הרצה חוזרת של האנימציה)
    public void clearPath() {
        revealedPathCells.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color wallColor = Color.decode(config.getWallCellColor());
        Color gridColor = config.isDrawGrid() ? Color.decode(config.getGridColor()) : null;

        // שלב א': ציור כל המשבצות - לבן למעבר, צבע הקיר לקיר
        for (int row = 0; row < mazeHeight; row++) {
            for (int col = 0; col < mazeWidth; col++) {

                boolean isPath = maze[row][col];
                g.setColor(isPath ? Color.WHITE : wallColor);
                g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // שלב ב': ציור קווי רשת, אם צריך
        if (config.isDrawGrid()) {
            g.setColor(gridColor);
            for (int row = 0; row <= mazeHeight; row++) {
                g.drawLine(0, row * CELL_SIZE, mazeWidth * CELL_SIZE, row * CELL_SIZE);
            }
            for (int col = 0; col <= mazeWidth; col++) {
                g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, mazeHeight * CELL_SIZE);
            }
        }

        // שלב ג': ציור המשבצות שכבר נחשפו מהפתרון (אנימציה)
        Color pathColor = Color.decode(config.getPathColor());
        g.setColor(pathColor);
        for (Point cell : revealedPathCells) {
            g.fillRect(cell.x * CELL_SIZE, cell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }
}
