package de.hatoka.group.internal.app.servlets;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;

@Path("/groups/{id}")
public class GroupService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/group/internal/templates/";
    public static final String METHOD_NAME_OVERVIEW = "list";
    public static final String METHOD_NAME_SCREEN = "screen";

    @PathParam("id")
    private String groupID;

    @Context
    private UriInfo info;

    public GroupService()
    {
        super(RESOURCE_PREFIX);
    }

    private GroupAction getAction()
    {
        String accountRef = getUserRef();
        return new GroupAction(accountRef, groupID);
    }
}