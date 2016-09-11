package de.hatoka.group.internal.app.member;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.common.capi.app.servlet.AbstractService;

@Path("/groups/{id}/members")
public class GroupMemberService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/group/internal/templates/";

    public static final String METHOD_NAME_LIST = "list";

    @PathParam("id")
    private String groupID;

    @Context
    private UriInfo info;

    public GroupMemberService()
    {
        super(RESOURCE_PREFIX);
    }

    private GroupMemberAction getAction()
    {
        GroupMemberAction result = new GroupMemberAction(groupID);
        getInjector().injectMembers(result);
        return result;
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        GroupMemberModel model = getAction().getGroupModel();
        try
        {
            String content = renderStyleSheet(model, "group_members.xslt", getXsltProcessorParameter("group"));
            return Response.status(200).entity(renderFrame(content, "members")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @GET
    public Response get()
    {
        return Response.ok(getAction().getMembers()).build();
    }

    private String renderFrame(String content, String subItem)
    {
        return getInstance(FrameRenderer.class).renderFame(content, "group", groupID, subItem);
    }

}