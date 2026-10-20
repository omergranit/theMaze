// מחלקה פשוטה שמחזיקה בתוכה את ההגדרות שהתקבלו מהשרת
public class RenderConfig {

    private String wallCellColor;   // צבע הקירות
    private String pathColor;       // צבע נתיב הפתרון
    private boolean drawGrid;       // האם לצייר רשת
    private String gridColor;       // צבע הרשת
    private int animationDelayMs;   // זמן המתנה בין צעדי האנימציה (במילישניות)

    public String getWallCellColor() {
        return wallCellColor;
    }

    public void setWallCellColor(String wallCellColor) {
        this.wallCellColor = wallCellColor;
    }

    public String getPathColor() {
        return pathColor;
    }

    public void setPathColor(String pathColor) {
        this.pathColor = pathColor;
    }

    public boolean isDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }

    public String getGridColor() {
        return gridColor;
    }

    public void setGridColor(String gridColor) {
        this.gridColor = gridColor;
    }

    public int getAnimationDelayMs() {
        return animationDelayMs;
    }

    public void setAnimationDelayMs(int animationDelayMs) {
        this.animationDelayMs = animationDelayMs;
    }

    @Override
    public String toString() {
        return "RenderConfig{" +
                "wallCellColor='" + wallCellColor + '\'' +
                ", pathColor='" + pathColor + '\'' +
                ", drawGrid=" + drawGrid +
                ", gridColor='" + gridColor + '\'' +
                ", animationDelayMs=" + animationDelayMs +
                '}';
    }
}
