import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// מחלקה שתפקידה היחיד הוא לדבר עם השרת ולהביא את הגדרות הציור
public class ConfigFetcher {

    private static final String URL =
            "https://shaitest-production-3066.up.railway.app/fm1/get-render-config";

    // שולח בקשה לשרת ומחזיר אובייקט RenderConfig מוכן
    public static RenderConfig fetchConfig() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        RenderConfig config = new RenderConfig();
        config.setWallCellColor(extractString(json, "wallCellColor"));
        config.setPathColor(extractString(json, "pathColor"));
        config.setDrawGrid(extractBoolean(json, "drawGrid"));
        config.setGridColor(extractString(json, "gridColor"));
        config.setAnimationDelayMs(extractInt(json, "animationDelayMs"));

        return config;
    }

    // פונקציות עזר קטנות שמחלצות ערך בודד מתוך טקסט ה-JSON
    // (בלי להשתמש בספרייה חיצונית - רק חיפוש טקסט פשוט)

    private static String extractString(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("לא נמצא שדה בשם: " + key);
    }

    private static boolean extractBoolean(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(true|false)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Boolean.parseBoolean(matcher.group(1));
        }
        throw new RuntimeException("לא נמצא שדה בשם: " + key);
    }

    private static int extractInt(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new RuntimeException("לא נמצא שדה בשם: " + key);
    }
}
