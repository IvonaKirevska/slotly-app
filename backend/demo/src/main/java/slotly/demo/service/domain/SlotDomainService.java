package slotly.demo.service.domain;

import org.springframework.stereotype.Service;
import slotly.demo.model.domain.Slot;
import slotly.demo.model.exceptions.SlotConflictException;
import slotly.demo.model.exceptions.SlotNotFoundException;
import slotly.demo.repository.SlotRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SlotDomainService {
    private final SlotRepository repo;

    public SlotDomainService(SlotRepository repo) {
        this.repo = repo;
    }

    public List<Slot> findAll(){
        return repo.findAllByOrderByDateAscStartAsc();
    }

    public Slot create(LocalDate date, LocalTime start, int duration){
        boolean overlaps=repo.findByDate(date).stream().anyMatch(s->
                start.isBefore(s.getStart().plusMinutes(s.getDuration()))
                && s.getStart().isBefore(start.plusMinutes(duration)));
        if (overlaps) throw new IllegalArgumentException("Slot overlaps with existing slot");

        Slot slot=new Slot();
        slot.setDate(date);
        slot.setStart(start);
        slot.setDuration(duration);
        return repo.save(slot);
    }

    public Slot book(Long id, String name, String email, String service, String notes){
        if (repo.book(id, name, email, service, notes)==0){
            if (!repo.existsById(id)) throw new SlotNotFoundException("Slot not found.");
            throw new SlotConflictException("Sorry, that slot was already taken. Pick another one.");
        }
        return repo.findById(id).orElseThrow();
    }
}
