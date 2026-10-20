import java.awt.Color;
import java.awt.image.BufferedImage;

// מחלקה שתפקידה לקחת את התמונה שהתקבלה מהשרת ולהפוך אותה למבנה נתונים
// שהתוכנית יכולה להשתמש בו - מערך של true/false לכל משבצת
public class MazeParser {

    // true = מעבר (פיקסל לבן)
    // false = קיר (כל פיקסל שאינו לבן)
    //
    // שימו לב: גילינו שהשרת לא מחזיר פיקסל בודד לכל משבצת,
    // אלא בלוק של כמה פיקסלים (למשל 16x16) לכל משבצת.
    // לכן קודם מחשבים כמה פיקסלים יש לכל משבצת, ואז דוגמים צבע מהאמצע של כל בלוק.
    public static boolean[][] parseImage(BufferedImage image, int width, int height) {

        boolean[][] maze = new boolean[height][width]; // [שורה][עמודה]

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        // כמה פיקסלים (בערך) תופסת כל משבצת בתמונה
        double cellPixelWidth = (double) imageWidth / width;
        double cellPixelHeight = (double) imageHeight / height;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                // מחשבים את הפיקסל שנמצא באמצע הבלוק של המשבצת הזו
                int sampleX = (int) ((col + 0.5) * cellPixelWidth);
                int sampleY = (int) ((row + 0.5) * cellPixelHeight);

                // הגנה מפני חריגה קלה מגבולות התמונה בגלל עיגול מספרים
                sampleX = Math.min(sampleX, imageWidth - 1);
                sampleY = Math.min(sampleY, imageHeight - 1);

                int rgb = image.getRGB(sampleX, sampleY);
                Color pixelColor = new Color(rgb);

                boolean isWhite = pixelColor.getRed() == 255
                        && pixelColor.getGreen() == 255
                        && pixelColor.getBlue() == 255;

                maze[row][col] = isWhite; // true אם לבן (מעבר), false אחרת (קיר)
            }
        }

        return maze;
    }
}
