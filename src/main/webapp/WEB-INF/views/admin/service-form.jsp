<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty service ? 'Ajouter' : 'Modifier'} Service - Barbershop</title>
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
                        <i class="fas fa-scissors"></i>
                    </div>
                    <span>Barbershop Admin</span>
                </div>
                <ul class="navbar-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/services" class="navbar-link">
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
            <div class="container container-narrow">
                <!-- Page Header -->
                <div class="page-header">
                    <h1 class="page-title">
                        <i class="fas ${empty service ? 'fa-plus-circle' : 'fa-edit'}"></i>
                        ${empty service ? 'Ajouter un Service' : 'Modifier le Service'}
                    </h1>
                    <p class="page-subtitle">
                        ${empty service ? 'Créez un nouveau service pour votre catalogue' : 'Modifiez les informations du service'}
                    </p>
                </div>

                <!-- Form Section -->
                <div class="form-section">
                    <form action="${pageContext.request.contextPath}/admin/services" method="post">
                        <input type="hidden" name="action" value="${empty service ? 'create' : 'update'}">
                        <c:if test="${not empty service}">
                            <input type="hidden" name="serviceId" value="${service.serviceId}">
                        </c:if>

                        <!-- Service Name -->
                        <div class="form-group">
                            <label for="name" class="form-label form-label-required">
                                <i class="fas fa-scissors"></i>
                                Nom du service
                            </label>
                            <input
                                type="text"
                                id="name"
                                name="name"
                                class="form-input"
                                value="${service.name}"
                                required
                                placeholder="Ex: Coupe classique, Barbe et moustache, Coloration..."
                                maxlength="100">
                            <span class="form-help">
                                Le nom du service tel qu'il apparaîtra dans le catalogue
                            </span>
                        </div>

                        <!-- Description -->
                        <div class="form-group">
                            <label for="description" class="form-label">
                                <i class="fas fa-align-left"></i>
                                Description
                            </label>
                            <textarea
                                id="description"
                                name="description"
                                class="form-textarea"
                                rows="4"
                                placeholder="Description détaillée du service, ce qui est inclus, les techniques utilisées...">${service.description}</textarea>
                            <span class="form-help">
                                <i class="fas fa-info-circle"></i>
                                Décrivez en détail ce qui est inclus dans ce service (optionnel)
                            </span>
                        </div>

                        <!-- Duration and Price -->
                        <div class="form-two-column">
                            <!-- Duration -->
                            <div class="form-group">
                                <label for="duration" class="form-label form-label-required">
                                    <i class="fas fa-clock"></i>
                                    Durée (minutes)
                                </label>
                                <input
                                    type="number"
                                    id="duration"
                                    name="duration"
                                    class="form-input"
                                    value="${service.duration}"
                                    min="5"
                                    max="480"
                                    step="5"
                                    required
                                    placeholder="30">
                                <span class="form-help">
                                    Durée moyenne du service
                                </span>
                            </div>

                            <!-- Price -->
                            <div class="form-group">
                                <label for="price" class="form-label form-label-required">
                                    <i class="fas fa-money-bill-wave"></i>
                                    Prix (MAD)
                                </label>
                                <input
                                    type="number"
                                    id="price"
                                    name="price"
                                    class="form-input"
                                    value="${service.price}"
                                    step="0.01"
                                    min="0"
                                    max="10000"
                                    required
                                    placeholder="100.00">
                                <span class="form-help">
                                    Prix en dirhams marocains
                                </span>
                            </div>
                        </div>

                        <!-- Active Status -->
                        <div class="form-group">
                            <div class="card" style="background: var(--color-neutral-50); padding: var(--space-5);">
                                <div class="form-checkbox">
                                    <input
                                        type="checkbox"
                                        name="isActive"
                                        id="isActive"
                                        ${service.active ? 'checked' : ''}>
                                    <label for="isActive" style="display: flex; flex-direction: column; gap: var(--space-1);">
                                        <span class="flex items-center gap-2" style="font-size: var(--text-base); font-weight: var(--font-weight-semibold);">
                                            <i class="fas fa-toggle-on"></i>
                                            Service actif et disponible
                                        </span>
                                        <span class="text-sm text-neutral-600">
                                            Lorsque activé, ce service sera visible et disponible pour les réservations des clients
                                        </span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <!-- Preview Card -->
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-eye"></i>
                                Aperçu du service
                            </label>
                            <div class="service-card">
                                <div class="service-card-header">
                                    <div class="service-card-icon">
                                        <i class="fas fa-cut"></i>
                                    </div>
                                    <div class="service-card-price" id="previewPrice">${not empty service.price ? service.price : '0.00'} MAD</div>
                                </div>
                                <h3 class="service-card-title" id="previewName">${not empty service.name ? service.name : 'Nom du service'}</h3>
                                <p class="service-card-description" id="previewDescription">
                                    ${not empty service.description ? service.description : 'La description du service apparaîtra ici...'}
                                </p>
                                <div class="service-card-meta">
                                    <div class="service-card-meta-item">
                                        <i class="fas fa-clock"></i>
                                        <span id="previewDuration">${not empty service.duration ? service.duration : '0'} min</span>
                                    </div>
                                    <div class="service-card-meta-item">
                                        <i class="fas fa-tag"></i>
                                        <span id="previewStatus">Statut: <span id="previewStatusText">${service.active ? 'Actif' : 'Inactif'}</span></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Form Actions -->
                        <div class="form-buttons">
                            <a href="${pageContext.request.contextPath}/admin/services" class="btn btn-secondary">
                                <i class="fas fa-times"></i>
                                Annuler
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas ${empty service ? 'fa-plus-circle' : 'fa-save'}"></i>
                                ${empty service ? 'Créer le Service' : 'Mettre à jour'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Live preview functionality
        document.getElementById('name').addEventListener('input', function(e) {
            const value = e.target.value || 'Nom du service';
            document.getElementById('previewName').textContent = value;
        });

        document.getElementById('description').addEventListener('input', function(e) {
            const value = e.target.value || 'La description du service apparaîtra ici...';
            document.getElementById('previewDescription').textContent = value;
        });

        document.getElementById('duration').addEventListener('input', function(e) {
            const value = e.target.value || '0';
            document.getElementById('previewDuration').textContent = value + ' min';
        });

        document.getElementById('price').addEventListener('input', function(e) {
            const value = e.target.value || '0.00';
            document.getElementById('previewPrice').textContent = value + ' MAD';
        });

        document.getElementById('isActive').addEventListener('change', function(e) {
            const statusText = e.target.checked ? 'Actif' : 'Inactif';
            document.getElementById('previewStatusText').textContent = statusText;
        });

        // Form validation
        document.querySelector('form').addEventListener('submit', function(e) {
            const name = document.getElementById('name').value.trim();
            const duration = parseInt(document.getElementById('duration').value);
            const price = parseFloat(document.getElementById('price').value);

            if (!name) {
                e.preventDefault();
                alert('Veuillez entrer un nom pour le service.');
                document.getElementById('name').focus();
                return false;
            }

            if (duration < 5 || duration > 480) {
                e.preventDefault();
                alert('La durée doit être entre 5 et 480 minutes.');
                document.getElementById('duration').focus();
                return false;
            }

            if (price < 0 || price > 10000) {
                e.preventDefault();
                alert('Le prix doit être entre 0 et 10000 MAD.');
                document.getElementById('price').focus();
                return false;
            }

            return true;
        });
    </script>
</body>
</html>
