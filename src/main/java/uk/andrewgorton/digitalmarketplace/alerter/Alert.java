package uk.andrewgorton.digitalmarketplace.alerter;

/**
 * Created by gortonaj on 23/09/2016.
 */
public class Alert {
    private final long id;
    private final String email;
    private final String customerMatchRegex;
    private final boolean enabled;

    public Alert(long id, String email, String customerMatchRegex, boolean enabled) {
        this.id = id;
        this.email = email;
        this.customerMatchRegex = customerMatchRegex;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerMatchRegex() {
        return customerMatchRegex;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
