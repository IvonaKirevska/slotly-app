
package slotly.demo.web;

import org.springframework.web.bind.annotation.*;
import slotly.demo.service.application.GeminiService;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/confirmation")
    public ConfirmationResponse generateConfirmation(
            @RequestBody ConfirmationRequest request) {

        String message = geminiService.generateConfirmation(
                request.customerName(),
                request.serviceName(),
                request.date(),
                request.time()
        );

        return new ConfirmationResponse(message);
    }

    public record ConfirmationRequest(
            String customerName,
            String serviceName,
            String date,
            String time
    ) {}

    public record ConfirmationResponse(String message) {}
}
