package slotly.demo.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import slotly.demo.model.exceptions.SlotConflictException;
import slotly.demo.model.exceptions.SlotNotFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SlotConflictException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, String>> conflict(Exception e) {
        String msg = e instanceof SlotConflictException
                ? e.getMessage()
                : "A slot already exists at that time.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", msg));
    }

    @ExceptionHandler(SlotNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(SlotNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Please check the form: name, a valid email and a service are required."));
    }
}