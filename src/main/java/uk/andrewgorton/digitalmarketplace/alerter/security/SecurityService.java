package uk.andrewgorton.digitalmarketplace.alerter.security;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import uk.andrewgorton.digitalmarketplace.alerter.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        return user.getPassword().compareToIgnoreCase(this.sha256(user.getSalt() + guess)) == 0;
    }

    /**
     * Hashes the provided string using SHA-256
     *
     * @param in the string to produce a hash of
     * @return the hash of `in`, or throws a RuntimeException in the extremely unlikely case that a SHA-256 algorithm
     * provider is not found inside this JVM.
     */
    public String sha256(String in) {
        try {
            return Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(in.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }
}
