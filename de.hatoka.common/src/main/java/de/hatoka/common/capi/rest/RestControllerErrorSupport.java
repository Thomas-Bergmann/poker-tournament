package de.hatoka.common.capi.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RestControllerErrorSupport
{
    public static final String ERROR_DATA_VALIDATION = "general.data.validation.error";

    public void throwRestControllerException(HttpStatus status, String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(status, errorMessage, logMessage);
    }

    public void throwNotFoundException(String errorKey, String resourceId)
    {
        String message = "error-key: " + errorKey + " " + "resource with id '" + resourceId + "' does not exists";
        throw new RestControllerException(HttpStatus.NOT_FOUND, message);
    }

    public void throwForbiddenException(String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(HttpStatus.FORBIDDEN, logMessage);
    }

    public void throwMethodNotAllowedException(String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(HttpStatus.METHOD_NOT_ALLOWED, logMessage);
    }

    public void throwBadRequestException(String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(HttpStatus.BAD_REQUEST, logMessage);
    }

    public void throwPreconditionFailedException(String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(HttpStatus.PRECONDITION_FAILED, logMessage);
    }

    public void throwInternalServerErrorException(String errorKey, String errorMessage)
    {
        String logMessage = "error-key: " + errorKey + " " + errorMessage;
        throw new RestControllerException(HttpStatus.INTERNAL_SERVER_ERROR, logMessage);
    }
}
