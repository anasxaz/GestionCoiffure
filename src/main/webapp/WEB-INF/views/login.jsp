<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="auth-page">
        
        <div class="auth-illustration-side">
            <div class="auth-illustration-content">
                <h2>Bienvenue sur GestionCoiffure</h2>
                <p>
                    Connectez-vous pour accéder à votre espace personnalisé et profiter
                    de toutes les fonctionnalités de gestion de salon.
                </p>
                
                <svg class="auth-illustration-svg" viewBox="0 0 400 300" fill="none" xmlns="http://www.w3.org/2000/svg">
                    
                    <circle cx="200" cy="120" r="50" fill="rgba(255,255,255,0.1)"/>
                    <circle cx="200" cy="120" r="35" fill="white"/>
                    <path d="M 200 155 Q 180 165, 180 185 L 180 210 Q 180 215, 185 215 L 215 215 Q 220 215, 220 210 L 220 185 Q 220 165, 200 155 Z" fill="white"/>

                    <rect x="180" y="200" width="40" height="40" rx="8" fill="rgba(37,99,235,0.2)" stroke="#2563eb" stroke-width="2"/>
                    <circle cx="200" cy="220" r="6" fill="#2563eb"/>
                    <rect x="198" y="220" width="4" height="12" fill="#2563eb"/>

                    <circle cx="280" cy="100" r="20" fill="rgba(16,185,129,0.2)"/>
                    <path d="M 273 100 L 278 105 L 287 93" stroke="#10b981" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" fill="none"/>

                    <circle cx="120" cy="140" r="18" fill="rgba(37,99,235,0.2)"/>
                    <path d="M 114 140 L 118 144 L 126 134" stroke="#2563eb" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" fill="none"/>

                    <circle cx="150" cy="80" r="4" fill="rgba(245,158,11,0.6)"/>
                    <circle cx="260" cy="180" r="5" fill="rgba(245,158,11,0.4)"/>
                    <circle cx="100" cy="200" r="3" fill="rgba(255,255,255,0.3)"/>
                    <circle cx="310" cy="140" r="4" fill="rgba(255,255,255,0.2)"/>
                </svg>
            </div>
        </div>

        <div class="auth-form-side">
            <div class="auth-form-container">
                <div class="auth-header">
                    <a href="${pageContext.request.contextPath}/" class="auth-back-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Retour à l'accueil</span>
                    </a>
                    <h1 class="auth-title">Connexion</h1>
                    <p class="auth-subtitle">Accédez à votre espace de gestion</p>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="alert-icon fas fa-exclamation-circle"></i>
                        <div class="alert-content">${error}</div>
                    </div>
                </c:if>

                <c:if test="${param.registration == 'success'}">
                    <div class="alert alert-success">
                        <i class="alert-icon fas fa-check-circle"></i>
                        <div class="alert-content">Inscription réussie ! Vous pouvez maintenant vous connecter.</div>
                    </div>
                </c:if>

                <c:if test="${param.logout == 'success'}">
                    <div class="alert alert-success">
                        <i class="alert-icon fas fa-check-circle"></i>
                        <div class="alert-content">Déconnexion réussie.</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                    <div class="form-group">
                        <label for="userType" class="form-label">Type de compte</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-users"></i>
                            <select name="userType" id="userType" class="form-select" required>
                                <option value="">Sélectionnez votre rôle</option>
                                <option value="admin" ${userType == 'admin' ? 'selected' : ''}>Administrateur</option>
                                <option value="barber" ${userType == 'barber' ? 'selected' : ''}>Coiffeur</option>
                                <option value="client" ${userType == 'client' ? 'selected' : ''}>Client</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email" class="form-label form-label-required">Adresse email</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-envelope"></i>
                            <input type="email" id="email" name="email" class="form-input"
                                   value="${email}" required placeholder="votre@email.com" autocomplete="email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password" class="form-label form-label-required">Mot de passe</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-lock"></i>
                            <input type="password" id="password" name="password" class="form-input"
                                   required placeholder="••••••••" autocomplete="current-password">
                            <button type="button" class="form-input-action" id="togglePassword">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: var(--space-8);">
                        <i class="fas fa-sign-in-alt"></i>
                        <span>Se connecter</span>
                    </button>
                </form>

                <div class="auth-divider">
                    <span>ou</span>
                </div>

                <div class="auth-form-footer">
                    <p class="auth-form-footer-text">
                        Pas encore de compte ?
                        <a href="${pageContext.request.contextPath}/register" class="auth-form-footer-link">
                            Créer un compte client
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Password visibility toggle
        const togglePassword = document.getElementById('togglePassword');
        const passwordInput = document.getElementById('password');

        if (togglePassword && passwordInput) {
            togglePassword.addEventListener('click', function() {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                const icon = this.querySelector('i');
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            });
        }
    </script>
</body>
</html>
