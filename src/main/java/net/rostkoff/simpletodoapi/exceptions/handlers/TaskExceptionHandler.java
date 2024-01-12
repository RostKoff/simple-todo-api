package net.rostkoff.simpletodoapi.exceptions.handlers;

import net.rostkoff.simpletodoapi.exceptions.ErrorResponse;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskConflict;
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
    protected ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex, WebRequest request) {
        var response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotFound.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        var response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskConflict.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        var response = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
