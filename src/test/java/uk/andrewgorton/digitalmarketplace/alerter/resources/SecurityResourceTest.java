package uk.andrewgorton.digitalmarketplace.alerter.resources;

import org.junit.Test;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.mockito.Mockito.*;

public class SecurityResourceTest {


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
        when(user.getSalt()).thenReturn("salt");
        when(user.getPassword()).thenReturn("9c95bf909cf17beaa7a4c71d86671566294699a994db7aaa8ffea004f425954f");
        when(user.getUsername()).thenReturn(username);
        when(user.getId()).thenReturn(userId);
        when(dao.findByUsername(username)).thenReturn(user);
        SecurityResource resource = new SecurityResource(dao);

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
        when(user.getSalt()).thenReturn("salt");
        when(user.getPassword()).thenReturn("9c95bf909cf17beaa7a4c71d86671566294699a994db7aaa8ffea004f425954f");
        when(user.getUsername()).thenReturn(username);
        when(user.getId()).thenReturn(userId);
        when(dao.findByUsername(username)).thenReturn(user);
        SecurityResource resource = new SecurityResource(dao);

        // difficult to assert anything on the response
        resource.handleLogin(session, request, info, returnLocation, username, password);

        verify(session).setAttribute("username", username);
        verify(session).setAttribute("userid", userId);
        verify(session).setAttribute("authenticated", true);
    }
    
    @Test
    public void HandleLoginEmptyUsername() throws Exception {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UriInfo info = mock(UriInfo.class, RETURNS_DEEP_STUBS);
        String returnLocation = "";
        String username = "";
        String password = "";

        when(request.getRemoteHost()).thenReturn("remote-host");
        when(info.getBaseUriBuilder()
                .path(SecurityResource.class)
                .path("/login")
                .queryParam("returnLocation", returnLocation)
                .build())
                .thenReturn(URI.create("test"));

        new SecurityResource(null)
                .handleLogin(session, request, info, returnLocation, username, password);

        verify(session).setAttribute("username", username);
        verify(session).setAttribute("authenticated", false);
    }

}