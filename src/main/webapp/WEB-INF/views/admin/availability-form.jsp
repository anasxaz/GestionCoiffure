<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter Disponibilité - Barbershop</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    
    <nav class="navbar role-admin">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon">
                    <i class="fas fa-cut"></i>
                </div>
                <span>Barbershop Admin</span>
            </div>
            <ul class="navbar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/availability" class="navbar-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Retour à la liste</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/availability" class="navbar-link active">
                        <i class="fas fa-clock"></i>
                        <span>Disponibilités</span>
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

    <main class="dashboard-main">
        <div class="container container-narrow">
            
            <div class="page-header">
                <h1 class="page-title">
                    <i class="fas fa-calendar-plus" style="color: var(--color-admin);"></i>
                    Ajouter une Disponibilité
                </h1>
                <p class="page-subtitle">Définissez les horaires de travail d'un coiffeur pour un jour spécifique</p>
            </div>

            <div class="alert alert-info">
                <i class="fas fa-info-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Information</div>
                    Les horaires définis seront utilisés pour la prise de rendez-vous. Assurez-vous de configurer tous les jours de travail de chaque coiffeur.
                </div>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Erreur</div>
                        ${error}
                    </div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/availability" method="post">
                <div class="form-section">
                    <div class="form-section-header">
                        <h2 class="form-section-title">
                            <i class="fas fa-calendar-alt"></i>
                            Informations de disponibilité
                        </h2>
                    </div>

                    <div class="form-group">
                        <label for="barberId" class="form-label form-label-required">
                            <i class="fas fa-user-tie" style="color: var(--color-admin);"></i>
                            Coiffeur
                        </label>
                        <select id="barberId" name="barberId" class="form-select" required>
                            <option value="">-- Sélectionnez un coiffeur --</option>
                            <c:forEach var="barber" items="${barbers}">
                                <option value="${barber.barberId}">${barber.name}</option>
                            </c:forEach>
                        </select>
                        <span class="form-help">
                            <i class="fas fa-question-circle"></i>
                            Choisissez le coiffeur pour lequel vous souhaitez définir une disponibilité
                        </span>
                    </div>

                    <div class="form-group">
                        <label for="dayOfWeek" class="form-label form-label-required">
                            <i class="fas fa-calendar-day" style="color: var(--color-primary);"></i>
                            Jour de la semaine
                        </label>
                        <select id="dayOfWeek" name="dayOfWeek" class="form-select" required>
                            <option value="">-- Sélectionnez un jour --</option>
                            <option value="Mon">
                                <i class="fas fa-circle"></i>
                                Lundi
                            </option>
                            <option value="Tue">Mardi</option>
                            <option value="Wed">Mercredi</option>
                            <option value="Thu">Jeudi</option>
                            <option value="Fri">Vendredi</option>
                            <option value="Sat">Samedi</option>
                            <option value="Sun">Dimanche</option>
                        </select>
                        <span class="form-help">
                            <i class="fas fa-question-circle"></i>
                            Sélectionnez le jour de la semaine pour cette disponibilité
                        </span>
                    </div>

                    <div class="form-two-column">
                        <div class="form-group">
                            <label for="startTime" class="form-label form-label-required">
                                <i class="fas fa-clock" style="color: var(--color-success);"></i>
                                Heure de début
                            </label>
                            <input
                                type="time"
                                id="startTime"
                                name="startTime"
                                class="form-input"
                                required
                                placeholder="09:00">
                            <span class="form-help">
                                <i class="fas fa-info-circle"></i>
                                Heure de début de la journée de travail
                            </span>
                        </div>

                        <div class="form-group">
                            <label for="endTime" class="form-label form-label-required">
                                <i class="fas fa-clock" style="color: var(--color-error);"></i>
                                Heure de fin
                            </label>
                            <input
                                type="time"
                                id="endTime"
                                name="endTime"
                                class="form-input"
                                required
                                placeholder="18:00">
                            <span class="form-help">
                                <i class="fas fa-info-circle"></i>
                                Heure de fin de la journée de travail
                            </span>
                        </div>
                    </div>

                    <div class="service-info-display">
                        <div class="service-info-item">
                            <span class="service-info-label">
                                <i class="fas fa-business-time"></i>
                                Durée de travail
                            </span>
                            <span class="service-info-value" id="workDuration">--:-- heures</span>
                        </div>
                        <div class="service-info-item">
                            <span class="service-info-label">
                                <i class="fas fa-calendar-check"></i>
                                Statut
                            </span>
                            <span class="service-info-value">
                                <span class="badge badge-neutral">En attente de validation</span>
                            </span>
                        </div>
                    </div>

                    <div class="form-buttons">
                        <a href="${pageContext.request.contextPath}/admin/availability" class="btn btn-secondary">
                            <i class="fas fa-times"></i>
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-check"></i>
                            Ajouter la Disponibilité
                        </button>
                    </div>
                </div>
            </form>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">
                        <i class="fas fa-lightbulb"></i>
                        Conseils
                    </h3>
                </div>
                <div class="card-body">
                    <ul style="list-style: none; padding: 0; margin: 0;">
                        <li style="padding: var(--space-3) 0; border-bottom: 1px solid var(--color-neutral-200); display: flex; gap: var(--space-3);">
                            <i class="fas fa-check-circle" style="color: var(--color-success); margin-top: 2px;"></i>
                            <span>Définissez des horaires réalistes en tenant compte des pauses</span>
                        </li>
                        <li style="padding: var(--space-3) 0; border-bottom: 1px solid var(--color-neutral-200); display: flex; gap: var(--space-3);">
                            <i class="fas fa-check-circle" style="color: var(--color-success); margin-top: 2px;"></i>
                            <span>Vérifiez qu'il n'existe pas déjà une disponibilité pour ce jour</span>
                        </li>
                        <li style="padding: var(--space-3) 0; border-bottom: 1px solid var(--color-neutral-200); display: flex; gap: var(--space-3);">
                            <i class="fas fa-check-circle" style="color: var(--color-success); margin-top: 2px;"></i>
                            <span>Les rendez-vous ne peuvent être pris que pendant ces horaires</span>
                        </li>
                        <li style="padding: var(--space-3) 0; display: flex; gap: var(--space-3);">
                            <i class="fas fa-check-circle" style="color: var(--color-success); margin-top: 2px;"></i>
                            <span>Vous pouvez modifier les horaires en supprimant et recréant la disponibilité</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>

    <script>
        // Calculate work duration when time inputs change
        const startTimeInput = document.getElementById('startTime');
        const endTimeInput = document.getElementById('endTime');
        const workDurationSpan = document.getElementById('workDuration');

        function calculateDuration() {
            const startTime = startTimeInput.value;
            const endTime = endTimeInput.value;

            if (startTime && endTime) {
                const start = new Date('2000-01-01 ' + startTime);
                const end = new Date('2000-01-01 ' + endTime);
                const diff = (end - start) / 1000 / 60 / 60; // hours

                if (diff > 0) {
                    const hours = Math.floor(diff);
                    const minutes = Math.round((diff - hours) * 60);
                    workDurationSpan.textContent = hours + 'h' + (minutes > 0 ? ' ' + minutes + 'min' : '');
                    workDurationSpan.parentElement.querySelector('.badge').className = 'badge badge-success';
                    workDurationSpan.parentElement.querySelector('.badge').textContent = 'Valide';
                } else {
                    workDurationSpan.textContent = 'Horaire invalide';
                    workDurationSpan.parentElement.querySelector('.badge').className = 'badge badge-error';
                    workDurationSpan.parentElement.querySelector('.badge').textContent = 'Invalide';
                }
            } else {
                workDurationSpan.textContent = '--:-- heures';
                workDurationSpan.parentElement.querySelector('.badge').className = 'badge badge-neutral';
                workDurationSpan.parentElement.querySelector('.badge').textContent = 'En attente de validation';
            }
        }

        startTimeInput.addEventListener('change', calculateDuration);
        endTimeInput.addEventListener('change', calculateDuration);

        // Form validation
        const form = document.querySelector('form');
        form.addEventListener('submit', function(e) {
            const startTime = startTimeInput.value;
            const endTime = endTimeInput.value;

            if (startTime && endTime) {
                const start = new Date('2000-01-01 ' + startTime);
                const end = new Date('2000-01-01 ' + endTime);

                if (end <= start) {
                    e.preventDefault();
                    alert('L\'heure de fin doit être après l\'heure de début.');
                    return false;
                }
            }
        });
    </script>
</body>
</html>
