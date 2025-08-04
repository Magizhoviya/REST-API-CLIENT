import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp {
    public static void main(String[] args) {
        try {
            String name = "John"; // You can change the name
            String apiUrl = "https://api.agify.io?name=" + name;

            // Make HTTP GET request
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Example response: {"name":"john","age":59,"count":60037}
            String json = response.toString();

            // Manual parsing using basic string methods
            String nameValue = extractValue(json, "name");
            String ageValue = extractValue(json, "age");
            String countValue = extractValue(json, "count");

            // Display result
            System.out.println("----- Age Prediction -----");
            System.out.println("Name  : " + nameValue);
            System.out.println("Age   : " + ageValue);
            System.out.println("Count : " + countValue);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper method to extract values like "key":value or "key":"value"
    public static String extractValue(String json, String key) {
        try {
            String search = "\"" + key + "\":";
            int startIndex = json.indexOf(search);
            if (startIndex == -1) return "Not found";

            startIndex += search.length();
            char nextChar = json.charAt(startIndex);

            boolean isString = nextChar == '"';
            int endIndex;

            if (isString) {
                startIndex++; // skip the opening quote
                endIndex = json.indexOf("\"", startIndex);
            } else {
                // value is number
                endIndex = json.indexOf(",", startIndex);
                if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
            }

            return json.substring(startIndex, endIndex).trim();
        } catch (Exception e) {
            return "Parse error";
        }
    }
}
