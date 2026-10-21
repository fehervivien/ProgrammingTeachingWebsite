package com.ptw.progteaching.auth.model.response;

import com.ptw.progteaching.common.model.Error;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthErrorResponse {

    // TODO: Majd frontend esetén lehet alakítani kell rajta
    private List<Error> errors;
}
