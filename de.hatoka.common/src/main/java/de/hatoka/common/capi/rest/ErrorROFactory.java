package de.hatoka.common.capi.rest;

import java.util.Objects;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ErrorROFactory
{
    public static ResponseEntity<ErrorRO> createErrorResponseEntity(HttpStatus httpStatus, String message,
                    Set<String> details)
    {
        ErrorRO errorRO = new ErrorRO(httpStatus.value(), message);
        if (Objects.nonNull(details))
        {
            errorRO.setDetails(details);
        }
        return new ResponseEntity<>(errorRO, httpStatus);
    }

    public static ResponseEntity<ErrorRO> createErrorResponseEntity(HttpStatus httpStatus, String message)
    {
        return createErrorResponseEntity(httpStatus, message, null);
    }

}
