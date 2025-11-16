package util;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    /**
     * Hash a password using BCrypt
     * @param plainPassword The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }
    
    /**
     * Verify a password against a hash
     * @param plainPassword The plain text password to check
     * @param hashedPassword The hashed password to verify against
     * @return true if the password matches
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if password meets minimum requirements
     * @param password The password to validate
     * @return true if password is valid
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }
    
    /**
     * Get password strength message
     * @param password The password to check
     * @return Message about password strength
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Le mot de passe est requis";
        }
        if (password.length() < 6) {
            return "Le mot de passe doit contenir au moins 6 caractères";
        }
        if (password.length() < 8) {
            return "Mot de passe faible - Recommandé: 8+ caractères";
        }
        return "Mot de passe valide";
    }
}