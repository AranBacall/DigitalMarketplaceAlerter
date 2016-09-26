package uk.andrewgorton.digitalmarketplace.alerter.dao.factory;

import org.glassfish.hk2.api.Factory;
import uk.andrewgorton.digitalmarketplace.alerter.dao.UserDAO;

public class UserDAOFactory implements Factory<UserDAO> {
    private final UserDAO userDAO;

    public UserDAOFactory(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDAO provide() {
        return userDAO;
    }

    @Override
    public void dispose(UserDAO userDAO) {

    }
}
