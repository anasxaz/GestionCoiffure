<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty offer ? 'Ajouter' : 'Modifier'} une Offre - Barbershop</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-page">
        <!-- Navigation Bar -->
        <nav class="navbar role-admin">
            <div class="navbar-container">
                <div class="navbar-brand">
                    <div class="navbar-brand-icon">
                        <i class="fas fa-${empty offer ? 'plus-circle' : 'edit'}"></i>
                    </div>
                    <span>${empty offer ? 'Ajouter' : 'Modifier'} une Offre</span>
                </div>
                <ul class="navbar-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/offers" class="navbar-link">
                            <i class="fas fa-arrow-left"></i>
                            <span>Retour aux Offres</span>
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
                    <h1 class="page-title">${empty offer ? 'Nouvelle Offre' : 'Modifier l\'Offre'}</h1>
                    <p class="page-subtitle">
                        ${empty offer ? 'Créez une nouvelle offre de fidélité pour récompenser vos clients' : 'Modifiez les détails de l\'offre de fidélité'}
                    </p>
                </div>
            </div>

            <!-- Info Alert -->
            <div class="alert alert-info">
                <i class="fas fa-info-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Programme de Fidélité</div>
                    Les offres permettent aux clients de dépenser leurs points de fidélité pour obtenir des réductions de 10% sur leurs rendez-vous. Assurez-vous de définir un nombre de points approprié.
                </div>
            </div>

            <!-- Form Section -->
            <form action="${pageContext.request.contextPath}/admin/offers" method="post">
                <input type="hidden" name="action" value="${empty offer ? 'create' : 'update'}">
                <c:if test="${not empty offer}">
                    <input type="hidden" name="offerId" value="${offer.offerId}">
                </c:if>

                <div class="form-section">
                    <div class="form-section-header">
                        <h2 class="form-section-title">
                            <i class="fas fa-info-circle"></i>
                            Informations de l'offre
                        </h2>
                    </div>

                    <!-- Title Field -->
                    <div class="form-group">
                        <label for="title" class="form-label form-label-required">
                            <i class="fas fa-tag"></i>
                            Titre de l'offre
                        </label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            class="form-input"
                            value="${offer.title}"
                            required
                            placeholder="Ex: Réduction 10% sur votre prochain rendez-vous"
                            maxlength="100">
                        <span class="form-help">
                            <i class="fas fa-lightbulb"></i>
                            Choisissez un titre clair et attractif qui sera visible par vos clients
                        </span>
                    </div>

                    <!-- Description Field -->
                    <div class="form-group">
                        <label for="description" class="form-label">
                            <i class="fas fa-align-left"></i>
                            Description
                        </label>
                        <textarea
                            id="description"
                            name="description"
                            class="form-textarea"
                            placeholder="Décrivez les détails et conditions de l'offre..."
                            rows="4"
                            maxlength="500">${offer.description}</textarea>
                        <span class="form-help">
                            <i class="fas fa-lightbulb"></i>
                            Ajoutez des informations supplémentaires sur l'offre, les conditions d'utilisation, etc.
                        </span>
                    </div>

                    <!-- Points Required Field -->
                    <div class="form-group">
                        <label for="pointsRequired" class="form-label form-label-required">
                            <i class="fas fa-coins"></i>
                            Points Requis
                        </label>
                        <input
                            type="number"
                            id="pointsRequired"
                            name="pointsRequired"
                            class="form-input"
                            value="${offer.pointsRequired}"
                            min="0"
                            step="10"
                            required
                            placeholder="100">
                        <span class="form-help">
                            <i class="fas fa-lightbulb"></i>
                            Nombre de points de fidélité nécessaires pour échanger cette offre (recommandé: 100-500 points)
                        </span>
                    </div>

                    <!-- Active Status Field -->
                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-toggle-on"></i>
                            Statut de l'offre
                        </label>
                        <div class="form-checkbox">
                            <input
                                type="checkbox"
                                id="isActive"
                                name="isActive"
                                ${offer.active ? 'checked' : ''}>
                            <label for="isActive">
                                Offre active et disponible pour échange
                            </label>
                        </div>
                        <span class="form-help">
                            <i class="fas fa-lightbulb"></i>
                            Les offres inactives ne seront pas visibles par les clients dans le programme de fidélité
                        </span>
                    </div>
                </div>

                <!-- Summary Card -->
                <c:if test="${not empty offer}">
                    <div class="card card-accent">
                        <div class="card-header">
                            <h3 class="card-title">
                                <i class="fas fa-info-circle"></i>
                                Informations supplémentaires
                            </h3>
                        </div>
                        <div class="card-body">
                            <div class="service-info-display">
                                <div class="service-info-item">
                                    <span class="service-info-label">
                                        <i class="fas fa-hashtag"></i>
                                        ID de l'offre
                                    </span>
                                    <span class="service-info-value">${offer.offerId}</span>
                                </div>
                                <div class="service-info-item">
                                    <span class="service-info-label">
                                        <i class="fas fa-calendar-alt"></i>
                                        Date de création
                                    </span>
                                    <span class="service-info-value">
                                        <c:choose>
                                            <c:when test="${not empty offer.createdAt}">
                                                ${offer.createdAt}
                                            </c:when>
                                            <c:otherwise>
                                                Non disponible
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="service-info-item">
                                    <span class="service-info-label">
                                        <i class="fas fa-tag"></i>
                                        Type de réduction
                                    </span>
                                    <span class="service-info-value">10% de réduction</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Form Buttons -->
                <div class="form-buttons">
                    <a href="${pageContext.request.contextPath}/admin/offers" class="btn btn-secondary">
                        <i class="fas fa-times"></i>
                        Annuler
                    </a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-${empty offer ? 'plus-circle' : 'save'}"></i>
                        ${empty offer ? 'Créer l\'Offre' : 'Enregistrer les Modifications'}
                    </button>
                </div>
            </form>

            <!-- Help Section -->
            <div class="card card-flat" style="margin-top: var(--space-6);">
                <div class="card-header">
                    <h3 class="card-title">
                        <i class="fas fa-question-circle"></i>
                        Aide et Conseils
                    </h3>
                </div>
                <div class="card-body">
                    <div style="display: grid; gap: var(--space-4);">
                        <div>
                            <h4 style="font-size: var(--text-base); font-weight: var(--font-weight-semibold); margin-bottom: var(--space-2); color: var(--color-neutral-900);">
                                <i class="fas fa-coins" style="color: var(--color-warning);"></i>
                                Comment définir le nombre de points?
                            </h4>
                            <p style="font-size: var(--text-sm); color: var(--color-neutral-600); margin: 0;">
                                Les clients gagnent généralement des points à chaque rendez-vous. Définissez un nombre de points qui encourage la fidélité sans être trop difficile à atteindre. Par exemple: 100-200 points pour les petites réductions, 300-500 points pour les offres plus importantes.
                            </p>
                        </div>
                        <div>
                            <h4 style="font-size: var(--text-base); font-weight: var(--font-weight-semibold); margin-bottom: var(--space-2); color: var(--color-neutral-900);">
                                <i class="fas fa-toggle-on" style="color: var(--color-success);"></i>
                                Quand désactiver une offre?
                            </h4>
                            <p style="font-size: var(--text-sm); color: var(--color-neutral-600); margin: 0;">
                                Vous pouvez désactiver temporairement une offre sans la supprimer. C'est utile pour les offres saisonnières ou promotionnelles. Les clients ne verront que les offres actives dans leur espace de fidélité.
                            </p>
                        </div>
                        <div>
                            <h4 style="font-size: var(--text-base); font-weight: var(--font-weight-semibold); margin-bottom: var(--space-2); color: var(--color-neutral-900);">
                                <i class="fas fa-edit" style="color: var(--color-primary);"></i>
                                Modification d'offres existantes
                            </h4>
                            <p style="font-size: var(--text-sm); color: var(--color-neutral-600); margin: 0;">
                                Vous pouvez modifier les détails d'une offre à tout moment. Les changements seront immédiatement visibles par les clients. Attention: modifier le nombre de points requis peut affecter les clients qui économisaient pour cette offre.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Form validation feedback
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.querySelector('form');
            const titleInput = document.getElementById('title');
            const pointsInput = document.getElementById('pointsRequired');

            // Real-time validation for title
            if (titleInput) {
                titleInput.addEventListener('input', function() {
                    if (this.value.length < 3) {
                        this.classList.add('is-error');
                    } else {
                        this.classList.remove('is-error');
                    }
                });
            }

            // Real-time validation for points
            if (pointsInput) {
                pointsInput.addEventListener('input', function() {
                    if (this.value < 0) {
                        this.classList.add('is-error');
                    } else {
                        this.classList.remove('is-error');
                    }
                });
            }

            // Form submission validation
            if (form) {
                form.addEventListener('submit', function(e) {
                    let isValid = true;

                    if (titleInput && titleInput.value.trim().length < 3) {
                        titleInput.classList.add('is-error');
                        isValid = false;
                    }

                    if (pointsInput && (pointsInput.value < 0 || pointsInput.value === '')) {
                        pointsInput.classList.add('is-error');
                        isValid = false;
                    }

                    if (!isValid) {
                        e.preventDefault();
                        alert('Veuillez corriger les erreurs dans le formulaire avant de soumettre.');
                    }
                });
            }
        });
    </script>
</body>
</html>
