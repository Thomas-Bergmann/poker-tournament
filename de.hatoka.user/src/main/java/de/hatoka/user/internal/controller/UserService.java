package de.hatoka.user.internal.controller;

import java.net.URI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.common.capi.app.servlet.ServletConstants;

@Path(UserService.PATH)
public class UserService extends AbstractService
{
    public static final String PATH = "/user";

    @Context
    private UriInfo info;

    @POST
    @Path("/logout")
    public Response logout()
    {
        NewCookie userCookie = createCookie(ServletConstants.USER_ID_COOKIE_NAME, "", "hatoka user cookie");
        NewCookie signCookie = createCookie(ServletConstants.USER_SIGN_COOKIE_NAME, "", "hatoka sign cookie");
        URI uri = info.getBaseUri();
        return Response.seeOther(uri).cookie(userCookie, signCookie).build();
    }
}