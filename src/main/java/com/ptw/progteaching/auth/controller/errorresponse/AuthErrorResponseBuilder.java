package com.ptw.progteaching.auth.controller.errorresponse;

import com.ptw.progteaching.auth.controller.AuthController;
import com.ptw.progteaching.auth.model.exception.UserAlreadyExistException;
import com.ptw.progteaching.auth.model.exception.InvalidCredentialsException;
import com.ptw.progteaching.auth.model.response.AuthErrorResponse;
import com.ptw.progteaching.common.controller.exceptionhandling.ControllerExceptionHandler;
import com.ptw.progteaching.common.model.Error;
import com.ptw.progteaching.common.util.ErrorMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice(assignableTypes = AuthController.class)
@RequiredArgsConstructor
public class AuthErrorResponseBuilder extends ControllerExceptionHandler<AuthErrorResponse> {

    private final ErrorMessageUtil errorMessageUtil;

    @Override
    public ResponseEntity<AuthErrorResponse> buildResponse(final HttpStatus status, List<Error> errors) {
        return new ResponseEntity<>(new AuthErrorResponse(errors), status);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<AuthErrorResponse> handleUserAlreadyExistException() {
        String errorMessage = errorMessageUtil.getErrorMessage("LOGIN-01");

        return buildResponse(HttpStatus.CONFLICT, List.of(new Error(errorMessage)));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<AuthErrorResponse> handleInvalidCredentialsException() {
        String errorMessage = errorMessageUtil.getErrorMessage("LOGIN-02");

        return buildResponse(HttpStatus.UNAUTHORIZED, List.of(new Error(errorMessage)));
    }
}
