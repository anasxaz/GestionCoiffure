<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty barber ? 'Ajouter' : 'Modifier'} Coiffeur - Admin Dashboard</title>

    <!-- Design System CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Navigation Bar -->
    <nav class="navbar role-admin">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon">
                    <i class="fas fa-cut"></i>
                </div>
                <span>Gestion Coiffure</span>
            </div>

            <ul class="navbar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link">
                        <i class="fas fa-th-large"></i>
                        <span>Tableau de bord</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/barbers" class="navbar-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Retour à la liste</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout">
                        <i class="fas fa-sign-out-alt"></i>
                        <span>Déconnexion</span>
                    </a>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="dashboard-main">
        <!-- Page Header -->
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">
                    <i class="fas ${empty barber ? 'fa-user-plus' : 'fa-user-edit'}"></i>
                    ${empty barber ? 'Ajouter un Coiffeur' : 'Modifier le Coiffeur'}
                </h1>
                <p class="page-subtitle">
                    ${empty barber ? 'Créez un nouveau profil de coiffeur dans le système' : 'Modifiez les informations du coiffeur'}
                </p>
            </div>
        </div>

        <!-- Error Alert -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Erreur</div>
                    ${error}
                </div>
            </div>
        </c:if>

        <!-- Info Alert for New Barber -->
        <c:if test="${empty barber}">
            <div class="alert alert-info">
                <i class="fas fa-info-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Création d'un nouveau coiffeur</div>
                    Remplissez tous les champs requis. Le coiffeur recevra un email avec ses identifiants de connexion.
                </div>
            </div>
        </c:if>

        <!-- Form Section -->
        <form action="${pageContext.request.contextPath}/admin/barbers" method="post">
            <input type="hidden" name="action" value="${empty barber ? 'create' : 'update'}">
            <c:if test="${not empty barber}">
                <input type="hidden" name="barberId" value="${barber.barberId}">
            </c:if>

            <!-- Basic Information Section -->
            <div class="form-section">
                <div class="form-section-header">
                    <h2 class="form-section-title">
                        <i class="fas fa-user"></i>
                        Informations de base
                    </h2>
                </div>

                <!-- Full Name -->
                <div class="form-group">
                    <label for="name" class="form-label form-label-required">
                        <i class="fas fa-user"></i>
                        Nom complet
                    </label>
                    <input type="text"
                           id="name"
                           name="name"
                           class="form-input"
                           value="${barber.name}"
                           required
                           placeholder="Ex: Mohammed Alami">
                    <span class="form-help">Le nom complet du coiffeur tel qu'il apparaîtra aux clients</span>
                </div>

                <!-- Email & Phone Row -->
                <div class="form-two-column">
                    <div class="form-group">
                        <label for="email" class="form-label form-label-required">
                            <i class="fas fa-envelope"></i>
                            Adresse email
                        </label>
                        <input type="email"
                               id="email"
                               name="email"
                               class="form-input"
                               value="${barber.email}"
                               required
                               placeholder="exemple@email.com">
                        <span class="form-help">Utilisé pour la connexion et les notifications</span>
                    </div>

                    <div class="form-group">
                        <label for="phone" class="form-label">
                            <i class="fas fa-phone"></i>
                            Numéro de téléphone
                        </label>
                        <input type="tel"
                               id="phone"
                               name="phone"
                               class="form-input"
                               value="${barber.phone}"
                               placeholder="0612345678">
                        <span class="form-help">Format: 0612345678</span>
                    </div>
                </div>

                <!-- Password for New Barber -->
                <c:if test="${empty barber}">
                    <div class="form-group">
                        <label for="password" class="form-label form-label-required">
                            <i class="fas fa-lock"></i>
                            Mot de passe
                        </label>
                        <input type="password"
                               id="password"
                               name="password"
                               class="form-input"
                               required
                               placeholder="••••••••"
                               minlength="6">
                        <span class="form-help">Minimum 6 caractères requis</span>
                    </div>
                </c:if>
            </div>

            <!-- Professional Information Section -->
            <div class="form-section">
                <div class="form-section-header">
                    <h2 class="form-section-title">
                        <i class="fas fa-briefcase"></i>
                        Informations professionnelles
                    </h2>
                </div>

                <!-- Biography -->
                <div class="form-group">
                    <label for="bio" class="form-label">
                        <i class="fas fa-file-alt"></i>
                        Biographie / Spécialités
                    </label>
                    <textarea id="bio"
                              name="bio"
                              class="form-textarea"
                              rows="5"
                              placeholder="Décrivez l'expérience, les spécialités et les compétences du coiffeur...">${barber.bio}</textarea>
                    <span class="form-help">Cette information sera visible par les clients lors de la réservation</span>
                </div>

                <!-- Status -->
                <div class="form-group">
                    <label for="status" class="form-label">
                        <i class="fas fa-toggle-on"></i>
                        Statut
                    </label>
                    <select id="status" name="status" class="form-select">
                        <option value="active" ${empty barber || barber.status == 'active' ? 'selected' : ''}>
                            Actif - Disponible pour les réservations
                        </option>
                        <option value="inactive" ${barber.status == 'inactive' ? 'selected' : ''}>
                            Inactif - Non disponible pour les réservations
                        </option>
                    </select>
                    <span class="form-help">Un coiffeur inactif ne pourra pas recevoir de nouveaux rendez-vous</span>
                </div>
            </div>

            <!-- Form Actions -->
            <div class="form-buttons">
                <a href="${pageContext.request.contextPath}/admin/barbers" class="btn btn-secondary">
                    <i class="fas fa-times"></i>
                    Annuler
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="fas ${empty barber ? 'fa-plus-circle' : 'fa-save'}"></i>
                    ${empty barber ? 'Créer le Coiffeur' : 'Enregistrer les modifications'}
                </button>
            </div>
        </form>

        <!-- Additional Info Card for Edit Mode -->
        <c:if test="${not empty barber}">
            <div class="card" style="margin-top: var(--space-6);">
                <div class="card-header">
                    <h3 class="card-title">
                        <i class="fas fa-info-circle"></i>
                        Informations du compte
                    </h3>
                </div>
                <div class="card-body">
                    <div class="quick-stats-list">
                        <div class="quick-stat-item">
                            <span class="quick-stat-label">
                                <i class="fas fa-hashtag"></i>
                                ID du coiffeur
                            </span>
                            <span class="quick-stat-value">#${barber.barberId}</span>
                        </div>
                        <div class="quick-stat-item">
                            <span class="quick-stat-label">
                                <i class="fas fa-calendar-plus"></i>
                                Date de création
                            </span>
                            <span class="quick-stat-value">${barber.createdAt}</span>
                        </div>
                        <div class="quick-stat-item">
                            <span class="quick-stat-label">
                                <i class="fas fa-shield-alt"></i>
                                Rôle
                            </span>
                            <span class="badge badge-primary">Coiffeur</span>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </main>

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Form validation enhancement
        const form = document.querySelector('form');
        const passwordInput = document.getElementById('password');

        if (form) {
            form.addEventListener('submit', function(e) {
                // Validate password length for new barbers
                if (passwordInput && passwordInput.value.length < 6) {
                    e.preventDefault();
                    alert('Le mot de passe doit contenir au moins 6 caractères.');
                    passwordInput.focus();
                    return false;
                }

                // Confirm submission
                const isEdit = ${not empty barber};
                const message = isEdit
                    ? 'Confirmer la modification des informations du coiffeur?'
                    : 'Confirmer la création de ce nouveau coiffeur?';

                if (!confirm(message)) {
                    e.preventDefault();
                    return false;
                }
            });
        }

        // Email format validation
        const emailInput = document.getElementById('email');
        if (emailInput) {
            emailInput.addEventListener('blur', function() {
                const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (this.value && !emailPattern.test(this.value)) {
                    this.classList.add('is-error');
                    alert('Veuillez entrer une adresse email valide.');
                } else {
                    this.classList.remove('is-error');
                }
            });
        }

        // Phone format validation (French phone numbers)
        const phoneInput = document.getElementById('phone');
        if (phoneInput) {
            phoneInput.addEventListener('blur', function() {
                if (this.value) {
                    const phonePattern = /^0[1-9]\d{8}$/;
                    if (!phonePattern.test(this.value.replace(/\s/g, ''))) {
                        this.classList.add('is-error');
                        alert('Veuillez entrer un numéro de téléphone valide (10 chiffres commençant par 0).');
                    } else {
                        this.classList.remove('is-error');
                    }
                }
            });
        }
    </script>
</body>
</html>
