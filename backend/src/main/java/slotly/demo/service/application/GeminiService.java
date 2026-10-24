
package slotly.demo.service.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    public String generateConfirmation(
            String customerName,
            String serviceName,
            String date,
            String time) {

        try {
            String prompt = """
                    Write a friendly, short booking confirmation message
                    for a scheduling application called Slotly.

                    Customer name: %s
                    Service: %s
                    Date: %s
                    Time: %s

                    Confirm the appointment using these details.
                    Do not invent additional information.
                    Keep the message to 2-3 sentences.
                    """.formatted(
                    customerName, serviceName, date, time
            );

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of("text", prompt)
                                    )
                            )
                    )
            );

            String jsonBody =
                    objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://generativelanguage.googleapis.com/v1beta/"
                                    + "models/gemini-3.8-flash:generateContent"
                    ))
                    .timeout(Duration.ofSeconds(90))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Gemini API returned HTTP "
                                + response.statusCode()
                );
            }

            JsonNode root =
                    objectMapper.readTree(response.body());

            JsonNode textNode = root.path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isMissingNode()
                    || textNode.asText().isBlank()) {
                throw new IllegalStateException(
                        "Gemini returned no confirmation message."
                );
            }

            return textNode.asText();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Gemini request was interrupted."
            );
        } catch (Exception e) {
            System.err.println("Gemini error: " + e.getMessage());
            e.printStackTrace();

            throw new IllegalStateException(
                    "Could not generate the AI confirmation.", e
            );
        }
    }
}
