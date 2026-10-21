package com.ptw.progteaching.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator jakartaValidator;

    public <T> void badRequestFieldValidations(final T request) {
        final Set<ConstraintViolation<T>> violations = jakartaValidator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
