package de.hatoka.account.internal.app.servlets;

import java.net.URI;
import java.util.concurrent.Callable;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.config.AccountConfiguration;
import de.hatoka.account.internal.app.actions.LoginAction;
import de.hatoka.account.internal.app.actions.RegisterAccountAction;
import de.hatoka.account.internal.app.forms.LoginForm;
import de.hatoka.account.internal.app.forms.SignUpForm;
import de.hatoka.account.internal.app.models.LoginPageModel;
import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.common.capi.dao.EncryptionUtils;

@Path("/login")
public class LoginService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/app/";

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
            LoginAction action = new LoginAction();
            injectMembers(action);
            action.accept(form);
            if (!form.isLoginFailed())
            {
                NewCookie userCookie = createCookie(CookieConstants.USER_COOKIE_NAME, form.getUserID(),
                                "hatoka user cookie");
                NewCookie accountCookie = createCookie(CookieConstants.ACCOUNT_COOKIE_NAME, form.getAccountID(),
                                "hatoka account cookie");
                if (origin != null)
                {
                    EncryptionUtils utils = getInstance(EncryptionUtils.class);
                    URI uri = UriBuilder.fromUri(URI.create(origin)).queryParam("accountID", form.getAccountID())
                                    .queryParam("accountSign", utils.sign(getSecret(), form.getAccountID())).build();
                    return Response.seeOther(uri).cookie(userCookie, accountCookie).build();
                }
                return render(form, null, userCookie, accountCookie);
            }
        }
        return render(form, null);
    }

    private String getSecret()
    {
        return getInstance(AccountConfiguration.class).getSecret();
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
            RegisterAccountAction action = new RegisterAccountAction(getUriBuilder(LoginService.class, "verifySignUp")
                            .build());
            injectMembers(action);
            runInTransaction(new Runnable()
            {
                @Override
                public void run()
                {
                    action.accept(form);
                    form.setSuccessfullyRegistered(true);
                }
            });
        }
        return render(null, form);
    }

    @GET
    @Path("/verifySignUp")
    public Response verifySignUp(@QueryParam("email") String email, @QueryParam("token") String token)
    {
        Boolean applySignInToken;
        try
        {
            applySignInToken = runInTransaction(new Callable<Boolean>()
            {
                @Override
                public Boolean call()
                {
                    UserBO userBO = getInstance(AccountBusinessFactory.class).getUserBORepository().getUserBOByLogin(
                                    email);
                    return userBO.applySignInToken(token);
                }
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