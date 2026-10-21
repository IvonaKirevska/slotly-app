package slotly.demo.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import slotly.demo.model.dto.BookSlotRequest;
import slotly.demo.model.dto.CreateSlotRequest;
import slotly.demo.model.dto.SlotResponse;
import slotly.demo.service.application.SlotApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {
    private final SlotApplicationService app;

    public SlotController(SlotApplicationService app) {
        this.app = app;
    }

    @GetMapping
    public List<SlotResponse> list(){
        return app.list();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SlotResponse create (@Valid @RequestBody CreateSlotRequest req){
        return app.create(req);
    }

    @PostMapping("/{id}/book")
    public SlotResponse book(@PathVariable Long id, @Valid @RequestBody BookSlotRequest req) {
        return app.book(id, req);
    }
}
