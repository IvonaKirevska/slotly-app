package slotly.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import slotly.demo.model.domain.Slot;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByDate(LocalDate date);

    List<Slot> findAllByOrderByDateAscStartAsc();

    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE Slot s
    SET s.bookedName = :name, s.bookedEmail = :email, s.service = :service, s.notes = :notes
    WHERE s.id = :id AND s.bookedName IS NULL
    """)
    int book(@Param("id") Long id, @Param("name") String name, @Param("email") String email,
             @Param("service") String service, @Param("notes") String notes);
}
