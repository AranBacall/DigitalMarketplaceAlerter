package uk.andrewgorton.digitalmarketplace.alerter.security;

/**
 * Represents a salted and hashed password that is ready to be stored in the database
 */
public class ProtectedPassword {

    /**
     * A random alphanumeric string appended to the password
     */
    private String salt;
    /**
     * The hashed and salted password
     */
    private String password;

    /**
     * Produces a hashed and salted password
     *
     * @param password user password
     */
    public ProtectedPassword(String password, String salt) {
        this.salt = salt;
        this.password = password;
    }

    public String getSalt() {
        return this.salt;
    }

    public String getProtected() {
        return this.password;
    }
}
