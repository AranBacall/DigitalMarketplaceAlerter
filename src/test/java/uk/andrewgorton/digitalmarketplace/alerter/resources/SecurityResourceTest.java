package uk.andrewgorton.digitalmarketplace.alerter.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;
import uk.andrewgorton.digitalmarketplace.alerter.email.EmailService;
import uk.andrewgorton.digitalmarketplace.alerter.security.SecurityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link SecurityResource}
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityResourceTest {

    @Mock
    public UserDAO userDAO;
    @Mock
    public SecurityService securityService;

    @Test
    public void HandleLoginXForwardedRedirctToHome() throws Exception {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UriInfo info = mock(UriInfo.class, RETURNS_DEEP_STUBS);
        String returnLocation = "";
        String username = "user";
        long userId = 1L;
        String password = "pass";

        // mock the DAO
        UserDAO dao = mock(UserDAO.class);
        User user = mock(User.class);
        when(user.isDisabled()).thenReturn(false);
        when(user.getUsername()).thenReturn(username);
        when(user.getId()).thenReturn(userId);
        when(dao.findByUsername(username)).thenReturn(user);
        when(securityService.verifyPassword(user, password)).thenReturn(true);
        SecurityResource resource = new SecurityResource(dao, null, securityService);

        // mock method parameters
        when(request.getHeader("X-Forwarded-For")).thenReturn("1.2.3.4");
        when(info.getBaseUriBuilder()
                .path(HomepageResource.class)
                .build())
                .thenReturn(URI.create("home"));

        // difficult to assert anything on the response
        resource.handleLogin(session, request, info, returnLocation, username, password);

        verify(session).setAttribute("username", username);
        verify(session).setAttribute("userid", userId);
        verify(session).setAttribute("authenticated", true);
    }

    @Test
    public void HandleLoginRedirectToReturnLocation() throws Exception {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UriInfo info = mock(UriInfo.class, RETURNS_DEEP_STUBS);
        String returnLocation = "return";
        String username = "user";
        long userId = 1L;
        String password = "pass";

        // mock the DAO
        UserDAO dao = mock(UserDAO.class);
        User user = mock(User.class);
        when(user.isDisabled()).thenReturn(false);
        when(user.getUsername()).thenReturn(username);
        when(user.getId()).thenReturn(userId);
        when(dao.findByUsername(username)).thenReturn(user);
        when(securityService.verifyPassword(user, password)).thenReturn(true);
        SecurityResource resource = new SecurityResource(dao, null, securityService);

        // difficult to assert anything on the response
        resource.handleLogin(session, request, info, returnLocation, username, password);

        verify(session).setAttribute("username", username);
        verify(session).setAttribute("userid", userId);
        verify(session).setAttribute("authenticated", true);
    }
    
    @Test(expected = ForbiddenException.class)
    public void HandleLoginEmptyUsername() throws Exception {
        new SecurityResource(userDAO, null, null)
                .handleLogin(null, null, null, null, "", null);
    }

    @Test
    public void VerifyUserAndSendPasswordResetEmail() throws Exception {
        UriInfo info = mock(UriInfo.class, RETURNS_DEEP_STUBS);
        long id = 1;
        String email = "user@example.com";
        String url = "path/to/password/reset/page";
        EmailService emailService = mock(EmailService.class);
        UserDAO userDAO = mock(UserDAO.class);
        User user = mock(User.class);

        when(userDAO.findByEmail(email)).thenReturn(user);
        when(user.getEmail()).thenReturn(email);
        when(user.getId()).thenReturn(id);
        when(info.getBaseUriBuilder()
                .path(any(Class.class))
                .path(anyString())
                .path(anyString())
                .path(anyString())
                .build())
                .thenReturn(URI.create(url));

        Response response = new SecurityResource(userDAO, emailService, null)
                .verifyUserAndSendPasswordResetEmail(info, email);

        verify(emailService).sendPasswordResetEmail(user, url);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains(email));
    }

    @Test
    public void VerifyUserNotFound() throws Exception {
        String email = "";
        UserDAO userDAO = mock(UserDAO.class);

        Response response = new SecurityResource(userDAO, null, null)
                .verifyUserAndSendPasswordResetEmail(null, email);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        verify(userDAO, never()).insertKey(anyLong(), anyString());
    }

}