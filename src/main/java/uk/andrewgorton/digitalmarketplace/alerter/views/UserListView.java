package uk.andrewgorton.digitalmarketplace.alerter.views;

import io.dropwizard.views.View;
import uk.andrewgorton.digitalmarketplace.alerter.User;
import uk.andrewgorton.digitalmarketplace.alerter.model.BidManager;

import java.util.List;

public class UserListView extends View {
    private List<User> users;

    public UserListView(List<User> users) {
        super("userlistview.ftl");
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
