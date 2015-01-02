package de.hatoka.common.internal.app.models;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorModel
{
    private String id;
    private String message;
    private String stacktrace;

    public ErrorModel()
    {

    }

    public ErrorModel(String id, Throwable exception)
    {
        this.id = id;
        message = exception.getMessage();
        Writer writer = new StringWriter();
        PrintWriter paramPrintWriter = new PrintWriter(writer);
        exception.printStackTrace(paramPrintWriter);
        stacktrace = writer.toString();
    }

    public String getId()
    {
        return id;
    }

    public String getMessage()
    {
        return message;
    }

    public String getStacktrace()
    {
        return stacktrace;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setStacktrace(String stacktrace)
    {
        this.stacktrace = stacktrace;
    }

}
