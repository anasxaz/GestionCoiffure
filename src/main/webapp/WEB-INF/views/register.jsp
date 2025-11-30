<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="auth-page">
        
        <div class="auth-illustration-side">
            <div class="auth-illustration-content">
                <h2>Rejoignez-Nous Aujourd'hui</h2>
                <p>
                    Créez votre compte client pour réserver en ligne, accumuler des points de fidélité
                    et profiter d'offres exclusives.
                </p>
                
                <svg class="auth-illustration-svg" viewBox="0 0 400 300" fill="none" xmlns="http://www.w3.org/2000/svg">

                    <circle cx="150" cy="100" r="30" fill="white"/>
                    <path d="M 150 130 Q 135 138, 135 155 L 135 175 Q 135 179, 139 179 L 161 179 Q 165 179, 165 175 L 165 155 Q 165 138, 150 130 Z" fill="white"/>

                    <circle cx="250" cy="110" r="35" fill="rgba(255,255,255,0.9)"/>
                    <path d="M 250 145 Q 232 154, 232 173 L 232 195 Q 232 200, 237 200 L 263 200 Q 268 200, 268 195 L 268 173 Q 268 154, 250 145 Z" fill="rgba(255,255,255,0.9)"/>

                    <circle cx="200" cy="140" r="32" fill="rgba(255,255,255,0.95)"/>
                    <path d="M 200 172 Q 183 180, 183 198 L 183 220 Q 183 225, 188 225 L 212 225 Q 217 225, 217 220 L 217 198 Q 217 180, 200 172 Z" fill="rgba(255,255,255,0.95)"/>

                    <g fill="#10b981">
                        <rect x="108" y="88" width="4" height="16" rx="2"/>
                        <rect x="102" y="94" width="16" height="4" rx="2"/>
                    </g>

                    <g fill="#2563eb">
                        <rect x="288" y="98" width="4" height="16" rx="2"/>
                        <rect x="282" y="104" width="16" height="4" rx="2"/>
                    </g>

                    <circle cx="120" cy="180" r="4" fill="rgba(245,158,11,0.6)"/>
                    <circle cx="280" cy="160" r="5" fill="rgba(245,158,11,0.5)"/>
                    <circle cx="170" cy="70" r="3" fill="rgba(16,185,129,0.5)"/>
                    <circle cx="300" cy="130" r="3" fill="rgba(255,255,255,0.4)"/>
                </svg>
            </div>
        </div>

        <div class="auth-form-side">
            <div class="auth-form-container">
                <div class="auth-header">
                    <a href="${pageContext.request.contextPath}/login" class="auth-back-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Retour à la connexion</span>
                    </a>
                    <h1 class="auth-title">Créer un compte</h1>
                    <p class="auth-subtitle">Commencez votre expérience avec nous</p>
                </div>

                <div class="auth-info-box">
                    <i class="auth-info-box-icon fas fa-info-circle"></i>
                    <div class="auth-info-box-content">
                        Créez votre compte client pour réserver facilement et profiter de notre programme de fidélité.
                    </div>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">
                        <i class="alert-icon fas fa-exclamation-circle"></i>
                        <div class="alert-content">${error}</div>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form" id="registerForm">
                    <div class="form-group">
                        <label for="name" class="form-label form-label-required">Nom complet</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-user"></i>
                            <input type="text" id="name" name="name" class="form-input"
                                   value="${name}" required placeholder="Mohammed Alami" autocomplete="name">
                        </div>
                    </div>

                    <div class="auth-form-grid">
                        <div class="form-group">
                            <label for="email" class="form-label form-label-required">Email</label>
                            <div class="form-input-group">
                                <i class="form-input-icon fas fa-envelope"></i>
                                <input type="email" id="email" name="email" class="form-input"
                                       value="${email}" required placeholder="votre@email.com" autocomplete="email">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="phone" class="form-label form-label-required">Téléphone</label>
                            <div class="form-input-group">
                                <i class="form-input-icon fas fa-phone"></i>
                                <input type="tel" id="phone" name="phone" class="form-input"
                                       value="${phone}" required placeholder="0612345678" autocomplete="tel">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="password" class="form-label form-label-required">Mot de passe</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-lock"></i>
                            <input type="password" id="password" name="password" class="form-input"
                                   required placeholder="••••••••" autocomplete="new-password">
                            <button type="button" class="form-input-action" id="togglePassword">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                        <p class="form-help">Minimum 6 caractères requis</p>
                        <div class="password-strength">
                            <div class="password-strength-bar" id="strengthBar"></div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword" class="form-label form-label-required">Confirmer le mot de passe</label>
                        <div class="form-input-group">
                            <i class="form-input-icon fas fa-lock"></i>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-input"
                                   required placeholder="••••••••" autocomplete="new-password">
                            <button type="button" class="form-input-action" id="toggleConfirmPassword">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                        <p class="password-match-hint" id="matchHint">
                            <i class="fas fa-circle"></i>
                            <span id="matchText"></span>
                        </p>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: var(--space-8);">
                        <i class="fas fa-user-check"></i>
                        <span>Créer mon compte</span>
                    </button>
                </form>

                <div class="auth-form-footer">
                    <p class="auth-form-footer-text">
                        Vous avez déjà un compte ?
                        <a href="${pageContext.request.contextPath}/login" class="auth-form-footer-link">
                            Se connecter
                        </a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script>
         
        const togglePassword = document.getElementById('togglePassword');
        const passwordInput = document.getElementById('password');
        const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
        const confirmPasswordInput = document.getElementById('confirmPassword');

        if (togglePassword && passwordInput) {
            togglePassword.addEventListener('click', function() {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);
                const icon = this.querySelector('i');
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            });
        }

        if (toggleConfirmPassword && confirmPasswordInput) {
            toggleConfirmPassword.addEventListener('click', function() {
                const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                confirmPasswordInput.setAttribute('type', type);
                const icon = this.querySelector('i');
                icon.classList.toggle('fa-eye');
                icon.classList.toggle('fa-eye-slash');
            });
        }

        const strengthBar = document.getElementById('strengthBar');
        if (passwordInput && strengthBar) {
            passwordInput.addEventListener('input', function() {
                const password = this.value;
                let strength = 0;

                if (password.length >= 6) strength += 25;
                if (password.length >= 8) strength += 25;
                if (/[A-Z]/.test(password)) strength += 25;
                if (/[0-9]/.test(password) || /[^A-Za-z0-9]/.test(password)) strength += 25;

                strengthBar.style.width = strength + '%';

                if (strength <= 25) {
                    strengthBar.className = 'password-strength-bar weak';
                } else if (strength <= 75) {
                    strengthBar.className = 'password-strength-bar medium';
                } else {
                    strengthBar.className = 'password-strength-bar strong';
                }
            });
        }

        const matchHint = document.getElementById('matchHint');
        const matchText = document.getElementById('matchText');
        if (confirmPasswordInput && matchHint && matchText) {
            confirmPasswordInput.addEventListener('input', function() {
                if (this.value.length > 0) {
                    matchHint.style.display = 'flex';
                    if (this.value === passwordInput.value) {
                        matchText.textContent = 'Les mots de passe correspondent';
                        matchHint.className = 'password-match-hint match';
                    } else {
                        matchText.textContent = 'Les mots de passe ne correspondent pas';
                        matchHint.className = 'password-match-hint no-match';
                    }
                } else {
                    matchHint.style.display = 'none';
                }
            });
        }

        const form = document.getElementById('registerForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                if (passwordInput.value !== confirmPasswordInput.value) {
                    e.preventDefault();
                    alert('Les mots de passe ne correspondent pas!');
                    confirmPasswordInput.focus();
                }
            });
        }
    </script>
</body>
</html>
