package slotly.demo.model.exceptions;

public class SlotConflictException extends RuntimeException {
    public SlotConflictException(String message) {
        super(message);
    }
}
