package uk.andrewgorton.digitalmarketplace.alerter;

public class User {
    private final long id;
    private final String username;
    private final String email;
    private final String salt;
    private final String password;
    private final boolean disabled;

    public User(long id, String username, String email, String salt, String password, boolean disabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.disabled = disabled;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
