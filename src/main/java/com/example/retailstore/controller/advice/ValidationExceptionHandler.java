package com.example.retailstore.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    /**
     * Exception handler method to handle MethodArgumentNotValidException, which occurs when validation on an argument annotated with @Valid fails.
     * This method returns a map containing information about the validation failure.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return A map containing information about the validation failure, including an error message and details about each validation error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", "Validation Failed");
        List<Map<String, String>> validationErrors = ex.getBindingResult().getFieldErrors().stream().map(
                fieldError -> {
                    Map<String, String> fieldErrorMap = new HashMap<>();
                    fieldErrorMap.put("target", fieldError.getField());
                    fieldErrorMap.put("errorMessage", fieldError.getDefaultMessage());
                    return fieldErrorMap;
                }).toList();
        errorMap.put("validationErrors", validationErrors);
        return errorMap;
    }
}
