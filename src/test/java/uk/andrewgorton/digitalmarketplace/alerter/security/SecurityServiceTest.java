package uk.andrewgorton.digitalmarketplace.alerter.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import uk.andrewgorton.digitalmarketplace.alerter.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link SecurityService}
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityServiceTest {


    @Test
    public void protectPassword() throws Exception {
        String pass = "pass";
        String salt = "salt";
        SecurityService securityService = spy(SecurityService.class);
        doReturn(pass).when(securityService).sha256(salt + pass);

        ProtectedPassword actual = securityService.protectPassword(pass, salt);

        assertEquals(pass, actual.getProtected());
        assertEquals(salt, actual.getSalt());
    }

    @Test
    public void verifyPassword() throws Exception {
        String guess = "guess";
        String salt = "salt";
        String password = "Ae)ikas908asdi90uajgf";
        User user = mock(User.class);
        when(user.getSalt()).thenReturn("salt");
        when(user.getPassword()).thenReturn(password);
        SecurityService securityService = spy(new SecurityService());
        ProtectedPassword protectedPassword = mock(ProtectedPassword.class);
        when(protectedPassword.getProtected()).thenReturn(password);
        doReturn(protectedPassword).when(securityService).protectPassword(guess, salt);

        boolean result = securityService.verifyPassword(user, guess);

        assertTrue(result);
    }

    @Test
    public void verifyPasswordRejected() throws Exception {
        String guess = "guess";
        String salt = "salt";
        User user = mock(User.class);
        when(user.getSalt()).thenReturn("salt");
        when(user.getPassword()).thenReturn("user-password");
        SecurityService securityService = spy(new SecurityService());
        ProtectedPassword protectedPassword = mock(ProtectedPassword.class);
        when(protectedPassword.getProtected()).thenReturn("hashed-guess");
        doReturn(protectedPassword).when(securityService).protectPassword(guess, salt);

        boolean result = securityService.verifyPassword(user, guess);

        assertFalse(result);
    }

}