package com.eaglebank.eaglebank_api.v1.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFieldException(InvalidFieldException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> details = new HashMap<>();
        details.put("field", ex.getField());
        details.put("message", ex.getInvalidReason());
        details.put("type", ex.getType());
        body.put("details", details);
        body.put("message", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
