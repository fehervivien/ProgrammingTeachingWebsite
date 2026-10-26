package com.ptw.progteaching.common.util;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ErrorMessageUtil {

    private static final String ERROR_MESSAGE = "errorMessage";

    public String getErrorMessage(String errorCode) {
        return ResourceBundle.getBundle(ERROR_MESSAGE).getString(errorCode);
    }

    public String getErrorMessageWithParams(String errorCode, List<String> params) {
        String template = getErrorMessage(errorCode);

        if (params == null || params.isEmpty()) {
            String withoutPlaceholdersAndExtraSpaces = template.replaceAll("\\{\\d+}", "")
                    .replaceAll("\\s{2,}", " ");

            return withoutPlaceholdersAndExtraSpaces.trim();
        }

        return MessageFormat.format(template, params.toArray());
    }
}
