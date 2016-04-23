package de.hatoka.common.capi.app.servlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.hatoka.common.capi.dao.UUIDGenerator;

@Path("/resources")
public class ResourceService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    private Injector getInjector(Application application)
    {
        return (Injector)application.getProperties().get(ServletConstants.PROPERTY_INJECTOR);
    }

    @GET
    @Path("/{filename: [-a-zA-Z_0-9\\.\\/]*}")
    public Response getResource(@Context ServletContext context, @Context Application application,
                    @PathParam("filename") String filename)
    {
        try
        {
            String relative = "WEB-INF/resources/" + filename;
            String realPath = context.getRealPath("/" + relative);
            if (realPath == null)
            {
                // web application without context
                realPath = relative;
            }
            LOGGER.warn("Resource '{}' translated to '{}'.", filename, realPath);
            java.nio.file.Path path = Paths.get(realPath);
            if (!path.toFile().exists())
            {
                LOGGER.warn("Resource '{}' not found. Resource translated to '{}'.", filename, path);
                return Response.status(404).entity("404 - resource not found: " + filename).build();
            }
            return Response.status(200).type(Files.probeContentType(path)).entity(Files.readAllBytes(path)).build();
        }
        catch(IOException | NullPointerException e)
        {
            String errorID = getUUID(application);
            LOGGER.error("Error-Identifier: '" + errorID + "' - Load resource '"+filename+"' failed.", e);
            return Response.status(404).entity("Load resource failed: " + errorID).build();
        }
    }

    private String getUUID(Application application)
    {
        return getInjector(application).getInstance(UUIDGenerator.class).generate();
    }
}