package de.hatoka.common.capi.rest;

import java.util.HashSet;
import java.util.Set;

import javax.security.sasl.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler
{
    final static Logger LOGGER = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @ExceptionHandler(value = RestControllerException.class)
    private ResponseEntity<ErrorRO> handleRestControllerException(RestControllerException e)
    {
        LOGGER.error(e.getLogMessage(), e);
        return ErrorROFactory.createErrorResponseEntity(e.getHttpStatus(), e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorRO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        Set<String> details = new HashSet<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            StringBuilder detail = createSingleValidationDetail(error);
            details.add(detail.toString());
        });
        LOGGER.debug(details.toString(), e);
        return ErrorROFactory.createErrorResponseEntity(HttpStatus.BAD_REQUEST,
                        RestControllerErrorSupport.ERROR_DATA_VALIDATION, details);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    private ResponseEntity<ErrorRO> handleIllegalArgumentException(IllegalArgumentException e)
    {
        LOGGER.debug("illegal argument exception", e);
        return ErrorROFactory.createErrorResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    private ResponseEntity<ErrorRO> handleAuthenticationException(AuthenticationException e)
    {
        LOGGER.error("unauthorized (no or wrong authentication)", e);
        return ErrorROFactory.createErrorResponseEntity(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    private ResponseEntity<ErrorRO> handleAccessDeniedException(AccessDeniedException e)
    {
        LOGGER.debug("access denied", e);
        return ErrorROFactory.createErrorResponseEntity(HttpStatus.FORBIDDEN, e.getMessage());
    }

    private StringBuilder createSingleValidationDetail(ObjectError error)
    {
        StringBuilder detail = new StringBuilder("{");
        if (error instanceof FieldError)
        {
            FieldError fieldError = (FieldError)error;
            detail.append("fieldName:").append("\"" + fieldError.getField() + "\"").append(',').append("code:")
            .append("\"" + fieldError.getCode() + "\"").append(',').append("message:")
            .append("\"" + fieldError.getDefaultMessage() + "\"");
        }
        else
        {
            detail.append("objectName:").append("\"" + error.getObjectName() + "\"").append(',').append("code:")
            .append("\"" + error.getCode() + "\"").append(',').append("message:")
            .append("\"" + error.getDefaultMessage() + "\"");
        }
        detail.append('}');
        return detail;
    }
}
