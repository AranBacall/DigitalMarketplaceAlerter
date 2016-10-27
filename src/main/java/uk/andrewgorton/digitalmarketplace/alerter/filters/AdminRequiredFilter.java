package uk.andrewgorton.digitalmarketplace.alerter.filters;

import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;


public class AdminRequiredFilter implements ContainerRequestFilter {

    private final HttpServletRequest webRequest;
    private UserDAO userDAO;

    public AdminRequiredFilter(HttpServletRequest webRequest,UserDAO userDAO) {
        this.webRequest = webRequest;
        this.userDAO = userDAO;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        final HttpSession session = webRequest.getSession();
        isAdmin(session,userDAO);
    }

    /**
     * Queries the database in order to define whether the user associated with the provided session is an admin
     *
     * @param session : the HttpSession provided by the user request
     * @param userDAO : the DAO that provides access to the users table in the database
     *
     * @return boolean
     * */
    public static boolean isAdmin(HttpSession session, UserDAO userDAO)
    {
        Long userId = (Long) session.getAttribute("userid");
        User currentUser = userDAO.findById(userId);

        if (session.getAttribute("authenticated") == null || !((boolean) session.getAttribute("authenticated"))) {
            throw new ForbiddenException();
        }
        else if(currentUser == null || !currentUser.isAdmin()) {
            throw new ForbiddenException();
        }
        return true;
    }
}

