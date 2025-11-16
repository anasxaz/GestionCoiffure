<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Créer un Rendez-vous - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <nav class="navbar role-barber">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon"><i class="fas fa-cut"></i></div>
                <div>
                    <div style="font-weight: 700;">GestionCoiffure</div>
                    <div style="font-size: 0.75rem; opacity: 0.9;">Espace Coiffeur</div>
                </div>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/barber/dashboard" class="navbar-link"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/appointments" class="navbar-link active"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/schedule" class="navbar-link"><i class="fas fa-calendar-week"></i><span>Emploi du Temps</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        <div class="container-narrow">
            <div class="page-header-row">
                <div class="page-header-info">
                    <h1 class="page-title">Créer un Rendez-vous</h1>
                    <p class="page-subtitle">Planifiez un rendez-vous pour un client</p>
                </div>
            </div>

            <div class="alert alert-info" style="margin-bottom: var(--space-6);">
                <i class="alert-icon fas fa-info-circle"></i>
                <div class="alert-content">Créez un rendez-vous pour un client. Le rendez-vous sera automatiquement confirmé.</div>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="alert-icon fas fa-exclamation-triangle"></i>
                    <div class="alert-content">${error}</div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/barber/create-appointment" method="post" class="form-section">
                <div class="form-group">
                    <label for="clientId" class="form-label form-label-required">Client</label>
                    <select name="clientId" id="clientId" class="form-select" required>
                        <option value="">Sélectionner un client</option>
                        <c:forEach var="client" items="${clients}">
                            <option value="${client.clientId}">${client.name} - ${client.email}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="serviceId" class="form-label form-label-required">Service</label>
                    <select name="serviceId" id="serviceId" class="form-select" required>
                        <option value="">Sélectionner un service</option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.serviceId}">${service.name} - ${service.price} MAD (${service.duration} min)</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="date" class="form-label form-label-required">Date</label>
                    <input type="date" id="date" name="date" class="form-input" required>
                </div>

                <div class="form-group">
                    <label for="startTime" class="form-label form-label-required">Heure de début</label>
                    <input type="time" id="startTime" name="startTime" class="form-input" required>
                </div>

                <div class="form-buttons">
                    <a href="${pageContext.request.contextPath}/barber/appointments" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check"></i> Créer le rendez-vous
                    </button>
                </div>
            </form>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        document.getElementById('date').setAttribute('min', new Date().toISOString().split('T')[0]);
    </script>
</body>
</html>
