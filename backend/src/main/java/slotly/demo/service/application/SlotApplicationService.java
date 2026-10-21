package slotly.demo.service.application;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import slotly.demo.model.dto.BookSlotRequest;
import slotly.demo.model.dto.CreateSlotRequest;
import slotly.demo.model.dto.SlotResponse;
import slotly.demo.service.domain.SlotDomainService;

import java.util.List;

@Service
public class SlotApplicationService {
    private final SlotDomainService domain;

    public SlotApplicationService(SlotDomainService domain) {
        this.domain = domain;
    }

    @Transactional
    public List<SlotResponse> list(){
        return domain.findAll().stream().map(SlotResponse::from).toList();
    }

    @Transactional
    public SlotResponse create(CreateSlotRequest req){
        return SlotResponse.from(domain.create(req.date(), req.start(), req.duration()));
    }

    @Transactional
    public SlotResponse book(Long id, BookSlotRequest req){
        return SlotResponse.from(domain.book(id, req.name(), req.email(), req.service().name(), req.notes()));
    }
}
