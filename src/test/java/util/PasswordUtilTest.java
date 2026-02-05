package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void testHashPassword_generatesNonNullHash() {
        String hash = PasswordUtil.hashPassword("password123");

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
    }

    @Test
    void testHashPassword_differentHashesForSamePassword() {
        String hash1 = PasswordUtil.hashPassword("password123");
        String hash2 = PasswordUtil.hashPassword("password123");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testVerifyPassword_correctPassword() {
        String password = "mySecurePassword";
        String hash = PasswordUtil.hashPassword(password);

        boolean result = PasswordUtil.verifyPassword(password, hash);

        assertTrue(result);
    }

    @Test
    void testVerifyPassword_wrongPassword() {
        String password = "mySecurePassword";
        String hash = PasswordUtil.hashPassword(password);

        boolean result = PasswordUtil.verifyPassword("wrongPassword", hash);

        assertFalse(result);
    }

    @Test
    void testVerifyPassword_invalidHash() {
        boolean result = PasswordUtil.verifyPassword("password123", "invalidHash");

        assertFalse(result);
    }

    @Test
    void testIsValidPassword_validPassword() {
        assertTrue(PasswordUtil.isValidPassword("password123"));
        assertTrue(PasswordUtil.isValidPassword("abcdef"));
        assertTrue(PasswordUtil.isValidPassword("123456"));
    }

    @Test
    void testIsValidPassword_shortPassword() {
        assertFalse(PasswordUtil.isValidPassword("abc"));
        assertFalse(PasswordUtil.isValidPassword("12345"));
    }

    @Test
    void testIsValidPassword_nullPassword() {
        assertFalse(PasswordUtil.isValidPassword(null));
    }

    @Test
    void testIsValidPassword_emptyPassword() {
        assertFalse(PasswordUtil.isValidPassword(""));
    }

    @Test
    void testGetPasswordStrengthMessage_nullPassword() {
        String message = PasswordUtil.getPasswordStrengthMessage(null);

        assertEquals("Le mot de passe est requis", message);
    }

    @Test
    void testGetPasswordStrengthMessage_emptyPassword() {
        String message = PasswordUtil.getPasswordStrengthMessage("");

        assertEquals("Le mot de passe est requis", message);
    }

    @Test
    void testGetPasswordStrengthMessage_shortPassword() {
        String message = PasswordUtil.getPasswordStrengthMessage("abc");

        assertEquals("Le mot de passe doit contenir au moins 6 caractères", message);
    }

    @Test
    void testGetPasswordStrengthMessage_weakPassword6chars() {
        String message = PasswordUtil.getPasswordStrengthMessage("123456");

        assertEquals("Mot de passe faible - Recommandé: 8+ caractères", message);
    }

    @Test
    void testGetPasswordStrengthMessage_weakPassword7chars() {
        String message = PasswordUtil.getPasswordStrengthMessage("1234567");

        assertEquals("Mot de passe faible - Recommandé: 8+ caractères", message);
    }

    @Test
    void testGetPasswordStrengthMessage_validPassword8chars() {
        String message = PasswordUtil.getPasswordStrengthMessage("Password");

        assertEquals("Mot de passe valide", message);
    }

    @Test
    void testGetPasswordStrengthMessage_validPassword10chars() {
        String message = PasswordUtil.getPasswordStrengthMessage("Password123!");

        assertEquals("Mot de passe valide", message);
    }
}
