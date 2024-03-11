package org.example.controller.advice;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.exception.EntityAlreadyExistsException;
import org.example.exception.EntityNotFoundException;
import org.example.utils.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode,
            WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            var fieldName = AnnotationUtils.fieldDescription(ex.getParameter().getParameterType(), error.getField());
            var errorText = error.getField() + (fieldName == null ? ": " : "(" + fieldName + "): ") + error.getDefaultMessage();
            errors.add(errorText);
            break;
        }

        return this.handleExceptionInternal(ex, errors.size() > 0 ? errors.get(0) : null, headers, statusCode, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handle(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler({EntityAlreadyExistsException.class})
    public ResponseEntity<String> handle(EntityAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handle(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionUtils.getStackTrace(ex));
    }

}
