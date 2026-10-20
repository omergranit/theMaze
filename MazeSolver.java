import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

// מחלקה שתפקידה למצוא מסלול מהפינה השמאלית-עליונה לפינה הימנית-תחתונה
// באמצעות אלגוריתם BFS (חיפוש לפי רוחב - מוצא תמיד מסלול אם קיים)
public class MazeSolver {

    // מקבל את המבוך (true=מעבר, false=קיר)
    // מחזיר רשימת נקודות שמייצגות את הפתרון, או null אם אין פתרון
    public static List<Point> findPath(boolean[][] maze) {

        int height = maze.length;
        int width = maze[0].length;

        Point start = new Point(0, 0);                     // x=0, y=0
        Point end = new Point(width - 1, height - 1);       // x=width-1, y=height-1

        // אם נקודת ההתחלה או הסיום הן קיר - אין פתרון
        if (!maze[start.y][start.x] || !maze[end.y][end.x]) {
            return null;
        }

        // מבנה נתונים ל-BFS
        Queue<Point> queue = new ArrayDeque<>();
        Map<Point, Point> cameFrom = new HashMap<>(); // כדי לשחזר את המסלול בסוף
        boolean[][] visited = new boolean[height][width];

        queue.add(start);
        visited[start.y][start.x] = true;

        // 4 כיוונים אפשריים: למעלה, למטה, שמאלה, ימינה (בלי אלכסון)
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        boolean found = false;

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (int dir = 0; dir < 4; dir++) {
                int newX = current.x + dx[dir];
                int newY = current.y + dy[dir];

                // בדיקה שהמשבצת החדשה בתוך גבולות המבוך
                if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
                    continue;
                }

                // בדיקה שהמשבצת היא מעבר ולא ביקרנו בה כבר
                if (!maze[newY][newX] || visited[newY][newX]) {
                    continue;
                }

                Point next = new Point(newX, newY);
                visited[newY][newX] = true;
                cameFrom.put(next, current);
                queue.add(next);
            }
        }

        if (!found) {
            return null; // אין פתרון
        }

        // שחזור המסלול מהסוף להתחלה, ואז היפוך הסדר
        List<Point> path = new ArrayList<>();
        Point step = end;
        while (!step.equals(start)) {
            path.add(step);
            step = cameFrom.get(step);
        }
        path.add(start);

        java.util.Collections.reverse(path);

        return path;
    }
}
