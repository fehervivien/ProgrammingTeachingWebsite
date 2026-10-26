package com.ptw.progteaching.common.controller.exceptionhandling;

import com.ptw.progteaching.common.model.Error;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public abstract class ControllerExceptionHandler<T> {

    public abstract ResponseEntity<T> buildResponse(HttpStatus status, List<Error> errors);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<T> handleConstraintViolationException(final ConstraintViolationException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, buildErrorDetailsFromConstraintViolationException(exception));
    }

    private List<Error> buildErrorDetailsFromConstraintViolationException(final ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(constraintViolation -> new Error(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage()))
                .sorted(Comparator.comparing(Error::getMessage))
                .toList();
    }
}
