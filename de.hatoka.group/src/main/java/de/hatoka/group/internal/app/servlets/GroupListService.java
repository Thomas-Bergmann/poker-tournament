package de.hatoka.group.internal.app.servlets;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.group.internal.app.models.GroupListModel;

@Path("/groups")
public class GroupListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/group/internal/templates/";
    public static final String METHOD_NAME_LIST = "list";
    public static final String METHOD_NAME_ADD = "add";

    @Context
    private UriInfo info;

    public GroupListService()
    {
        super(RESOURCE_PREFIX);
    }

    @POST
    @Path("/actionList")
    public Response actionList(@FormParam("groupID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (isButtonPressed(deleteButton))
        {
            return delete(identifiers);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("name") String name, @FormParam("owner") String ownerName, @FormParam("add") String addButton, @FormParam("join") String joinButton)
    {
        GroupListAction action = getAction();
        if (isButtonPressed(addButton))
        {
            runInTransaction(() -> action.create(name, ownerName));
        } else if (isButtonPressed(joinButton))
        {
            runInTransaction(() -> action.join(name, ownerName));
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("groupID") final List<String> identifiers)
    {
        GroupListAction action = getAction();
        runInTransaction(() -> action.delete(identifiers));
        return redirect(METHOD_NAME_LIST);
    }

    private GroupListAction getAction()
    {
        GroupListAction action = new GroupListAction();
        getInjector().injectMembers(action);
        return action;
    }

    /**
     * Lists all tournaments
     *
     * @return
     */
    @GET
    @Path("/list.html")
    public Response list()
    {
        final GroupListModel model = getAction().getGroupListModel();
        try
        {
            String content = renderStyleSheet(model, "group_list.xslt", getXsltProcessorParameter("group"));
            return Response.status(200).entity(renderFrame(content, "list")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    /**
     * Lists all tournaments
     *
     * @return
     */
    @GET
    @Path("/add.html")
    public Response add()
    {
        final GroupListModel model = getAction().getGroupListModel();
        try
        {
            String content = renderStyleSheet(model, "group_add.xslt", getXsltProcessorParameter("group"));
            return Response.status(200).entity(renderFrame(content, "create")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    private String renderFrame(String content, String subItem)
    {
        return getInstance(FrameRenderer.class).renderFame(content, "groups", subItem);
    }
}
