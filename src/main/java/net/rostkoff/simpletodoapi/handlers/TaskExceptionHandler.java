package net.rostkoff.simpletodoapi.handlers;

import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(TaskBadRequest.class)
    protected ResponseEntity<Object> handleBadRequest(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotFound.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
