import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// מחלקה שתפקידה להביא מהשרת את תמונת המבוך (PNG) לפי גודל שנבחר
public class MazeImageFetcher {

    private static final String BASE_URL =
            "https://shaitest-production-3066.up.railway.app/fm1/get-maze-image";

    // מקבל width ו-height, שולח בקשה לשרת, ומחזיר תמונה מוכנה לשימוש
    public static BufferedImage fetchMazeImage(int width, int height) throws Exception {

        String url = BASE_URL + "?width=" + width + "&height=" + height;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // כאן אנחנו מבקשים את התשובה כ"בייטים גולמיים" (bytes) כי זו תמונה, לא טקסט
        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        byte[] imageBytes = response.body();

        // הופכים את הבייטים לתמונה שאפשר לעבוד איתה בג'אווה
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        if (image == null) {
            throw new RuntimeException("לא הצלחנו לקרוא את התמונה שהתקבלה מהשרת");
        }

        return image;
    }
}
