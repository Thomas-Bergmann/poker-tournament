package de.hatoka.user.internal.app.servlets;

import java.net.URI;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBusinessFactory;
import de.hatoka.user.capi.config.UserConfiguration;
import de.hatoka.user.internal.app.actions.LoginAction;
import de.hatoka.user.internal.app.forms.LoginForm;
import de.hatoka.user.internal.app.forms.SignUpForm;
import de.hatoka.user.internal.app.models.LoginPageModel;

@Path("/login")
public class LoginService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/user/internal/templates/app/";

    public LoginService()
    {
        super(RESOURCE_PREFIX);
    }

    @POST
    @Path("/login")
    public Response login(@FormParam("email") String email, @FormParam("password") String password,
                    @FormParam("origin") String origin)
    {
        LoginForm form = new LoginForm();
        form.setEmail(email);
        form.setPassword(password);
        if (form.validate())
        {
            getAction().accept(form);
            if (!form.isLoginFailed())
            {
                NewCookie userCookie = createCookie(ServletConstants.USER_ID_COOKIE_NAME, form.getUserID(), "hatoka user cookie");
                String sign = getInstance(EncryptionUtils.class).sign(getSecret(), form.getUserID());
                NewCookie signCookie = createCookie(ServletConstants.USER_SIGN_COOKIE_NAME, sign, "hatoka sign cookie");
                if (origin != null)
                {
                    URI uri = UriBuilder.fromUri(URI.create(origin)).queryParam("accountID", form.getUserID())
                                    .queryParam("accountSign", sign).build();
                    return Response.seeOther(uri).cookie(userCookie, signCookie).build();
                }
                return render(form, null, userCookie, signCookie);
            }
        }
        return render(form, null);
    }

    private String getSecret()
    {
        return getInstance(UserConfiguration.class).getSecret();
    }

    @GET
    @Path("/index.html")
    public Response printLogin(@QueryParam("origin") String origin)
    {
        LoginForm loginForm = new LoginForm();
        loginForm.setOrigin(origin);
        return render(loginForm, null);
    }

    private Response render(LoginForm loginForm, SignUpForm signupForm, NewCookie... cookies)
    {
        LoginPageModel forms = new LoginPageModel();
        forms.setLoginForm(loginForm == null ? new LoginForm() : loginForm);
        forms.setSignUpForm(signupForm == null ? new SignUpForm() : signupForm);
        return renderResponseWithStylesheet(forms, "login.xslt", "login", cookies);
    }

    @POST
    @Path("/signup")
    public Response signup(@FormParam("email") String email, @FormParam("name") String name,
                    @FormParam("newPassword") String password)
    {
        SignUpForm form = new SignUpForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setName(name);
        if (form.validate())
        {
            URI uri = getUriBuilder(LoginService.class, "verifySignUp").build();
            runInTransaction(() -> {
                getAction().accept(uri, form);
                form.setSuccessfullyRegistered(true);
            });
        }
        return render(null, form);
    }

    private LoginAction getAction()
    {
        LoginAction result = new LoginAction();
        injectMembers(result);
        return result;
    }

    @GET
    @Path("/verifySignUp")
    public Response verifySignUp(@QueryParam("email") String email, @QueryParam("token") String token)
    {
        Boolean applySignInToken;
        try
        {
            applySignInToken = runInTransaction(() -> {
                UserBO userBO = getInstance(UserBusinessFactory.class).getUserBORepository().getUserBOByLogin(
                                email);
                return userBO.applySignInToken(token);
            });
        }
        catch(Exception exception)
        {
            return render(500, exception);
        }
        if (applySignInToken)
        {
            LoginForm form = new LoginForm();
            form.setEmail(email);
            form.setNowActive(true);
            return render(form, null);
        }
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setTokenValidationFailed(true);
        return render(null, signUpForm);
    }

}