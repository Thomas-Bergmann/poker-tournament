package de.hatoka.common.capi.rest;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("error")
public class ErrorRO
{
    private final int code;
    private final String message;
    private Set<String> details;

    public ErrorRO(int httpStatus, String errorCode)
    {
        this.code = httpStatus;
        this.message = errorCode;
    }

    /**
     * Only used by JSON mapper
     */
    public ErrorRO()
    {
        this(0, null);
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    public void addDetail(String detail)
    {
        this.details.add(detail);
    }

    public Set<String> getDetails()
    {
        return details;
    }

    public void setDetails(Set<String> details)
    {
        this.details = details;
    }
}
