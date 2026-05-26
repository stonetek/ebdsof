package stonetek.com.ebdsoft.exception.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import stonetek.com.ebdsoft.exception.EntityNotFoundException;
import stonetek.com.ebdsoft.exception.IgrejaAlreadyEnrolledException;
import stonetek.com.ebdsoft.exception.TurmaAlreadyEnrolledException;


public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> handlerObjectNotFoundException(EntityNotFoundException ex) {
        StandardError standardError = new StandardError(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ValidationError validationError = new ValidationError(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "Error na validação dos campos!");
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);
    }

    @ExceptionHandler(TurmaAlreadyEnrolledException.class)
    public ResponseEntity<String> handleTurmaAlreadyEnrolledException(TurmaAlreadyEnrolledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IgrejaAlreadyEnrolledException.class)
    public ResponseEntity<String> handleIgrejaAlreadyEnrolledException(TurmaAlreadyEnrolledException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}
