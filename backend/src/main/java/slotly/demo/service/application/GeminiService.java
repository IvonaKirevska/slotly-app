package slotly.demo.service.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    @Value("${GEMINI_API_KEY:}")
    private String apiKey;

    @Value("${GEMINI_MODEL:gemini-3.8-flash}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /** Never throws: falls back to a plain message so a booking never fails because of the AI. */
    public String generateConfirmation(String name, String service, String date, String time) {
        String fallback = "Hi %s, your %s appointment is confirmed for %s at %s. See you then!"
                .formatted(name, service, date, time);

        if (apiKey.isBlank()) {
            return fallback;
        }

        try {
            String prompt = """
                    Write a warm booking confirmation of 2 sentences for a beauty salon.
                    Customer: %s
                    Service: %s
                    When: %s at %s
                    Use only these details, no emojis, no placeholders.
                    """.formatted(name, service, date, time);

            String body = objectMapper.writeValueAsString(Map.of(
                    "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/"
                            + model + ":generateContent"))
                    .timeout(Duration.ofSeconds(90))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                log.warn("Gemini returned HTTP {}", response.statusCode());
                return fallback;
            }

            JsonNode text = objectMapper.readTree(response.body())
                    .path("candidates").path(0).path("content").path("parts").path(0).path("text");
            return text.asText("").isBlank() ? fallback : text.asText().trim();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return fallback;
        } catch (Exception e) {
            log.warn("Gemini call failed: {}", e.getMessage());
            return fallback;
        }
    }
}