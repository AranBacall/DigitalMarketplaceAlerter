package uk.andrewgorton.digitalmarketplace.alerter.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import uk.andrewgorton.digitalmarketplace.alerter.User;

import java.nio.charset.StandardCharsets;

/**
 * Provides security specific services such as hashing and salting to the rest of the application.
 */
public class SecurityService {

    /**
     * Creates a protected password with a randomised salt
     *
     * @param password the password to be hashed
     * @return a new {@link ProtectedPassword}
     */
    public ProtectedPassword protectPassword(final String password) {
        return this.protectPassword(password, RandomStringUtils.randomAlphanumeric(12));
    }
    /**
     * Creates a protected password
     *
     * @param password the password to be hashed
     * @return a new {@link ProtectedPassword}
     */
    public ProtectedPassword protectPassword(final String password, final String salt) {
        return new ProtectedPassword(this.sha256(salt + password), salt);
    }

    /**
     * Salts and hashes 'guess' and compares it to the user's password
     *
     * @param user the user that to compare passwords with
     * @param guess the password to compare against
     * @return <code>true</code> if the passwords match or <code>false</code> if they do not
     */
    public boolean verifyPassword(User user, String guess) {
        return user.getPassword().equals(this.protectPassword(guess, user.getSalt()).getProtected());
    }

    /**
     * Hashes the provided string using SHA-256
     *
     * @param in the string to produce a has of
     * @return
     */
    public String sha256(String in) {
        return new String(DigestUtils.sha256(in.getBytes(StandardCharsets.UTF_8)));
    }
}
