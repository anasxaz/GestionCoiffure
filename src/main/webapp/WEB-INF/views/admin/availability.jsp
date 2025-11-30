<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gérer les Disponibilités - Barbershop</title>

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
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Tableau de bord</span>
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
        
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Disponibilités des Coiffeurs</h1>
                <p class="page-subtitle">Gérez les horaires de travail de vos coiffeurs</p>
            </div>
            <div class="page-actions">
                <a href="${pageContext.request.contextPath}/admin/availability?action=add" class="btn btn-primary">
                    <i class="fas fa-plus"></i>
                    Ajouter une Disponibilité
                </a>
            </div>
        </div>

        <c:if test="${param.success == 'created'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Succès</div>
                    Disponibilité ajoutée avec succès!
                </div>
            </div>
        </c:if>

        <c:if test="${param.success == 'deleted'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Succès</div>
                    Disponibilité supprimée avec succès!
                </div>
            </div>
        </c:if>

        <c:if test="${param.error == 'deleteFailed'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Erreur</div>
                    Erreur lors de la suppression de la disponibilité.
                </div>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty barbers}">
                <c:forEach var="barber" items="${barbers}">
                    <div class="card mb-6">
                        <div class="card-header">
                            <h2 class="card-title">
                                <i class="fas fa-user-tie" style="color: var(--color-admin);"></i>
                                ${barber.name}
                            </h2>
                        </div>

                        <c:set var="hasAvailability" value="false"/>
                        <c:forEach var="avail" items="${availabilities}">
                            <c:if test="${avail.barberId == barber.barberId}">
                                <c:set var="hasAvailability" value="true"/>
                            </c:if>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${hasAvailability}">
                                <div class="table-container">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>
                                                    <i class="fas fa-calendar-day"></i>
                                                    Jour de la semaine
                                                </th>
                                                <th>
                                                    <i class="fas fa-clock"></i>
                                                    Heure de début
                                                </th>
                                                <th>
                                                    <i class="fas fa-clock"></i>
                                                    Heure de fin
                                                </th>
                                                <th>
                                                    <i class="fas fa-cog"></i>
                                                    Actions
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="avail" items="${availabilities}">
                                                <c:if test="${avail.barberId == barber.barberId}">
                                                    <tr>
                                                        <td>
                                                            <span class="badge badge-primary">
                                                                <i class="fas fa-calendar-day"></i>
                                                                <c:choose>
                                                                    <c:when test="${avail.dayOfWeek == 'Mon'}">Lundi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Tue'}">Mardi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Wed'}">Mercredi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Thu'}">Jeudi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Fri'}">Vendredi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Sat'}">Samedi</c:when>
                                                                    <c:when test="${avail.dayOfWeek == 'Sun'}">Dimanche</c:when>
                                                                </c:choose>
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <i class="fas fa-play-circle" style="color: var(--color-success); margin-right: var(--space-2);"></i>
                                                            <strong>${avail.startTime}</strong>
                                                        </td>
                                                        <td>
                                                            <i class="fas fa-stop-circle" style="color: var(--color-error); margin-right: var(--space-2);"></i>
                                                            <strong>${avail.endTime}</strong>
                                                        </td>
                                                        <td>
                                                            <div class="table-actions-group">
                                                                <a href="${pageContext.request.contextPath}/admin/availability?action=delete&id=${avail.availabilityId}"
                                                                   class="btn btn-error btn-sm"
                                                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette disponibilité ?')">
                                                                    <i class="fas fa-trash"></i>
                                                                    Supprimer
                                                                </a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-state">
                                    <div class="empty-state-icon">
                                        <i class="fas fa-calendar-times"></i>
                                    </div>
                                    <h3 class="empty-state-title">Aucune disponibilité définie</h3>
                                    <p class="empty-state-description">
                                        Ce coiffeur n'a pas encore d'horaires de travail configurés. Ajoutez une disponibilité pour permettre la prise de rendez-vous.
                                    </p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="card">
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <i class="fas fa-user-slash"></i>
                        </div>
                        <h3 class="empty-state-title">Aucun coiffeur enregistré</h3>
                        <p class="empty-state-description">
                            Veuillez d'abord créer des coiffeurs avant de définir leurs disponibilités.
                        </p>
                        <a href="${pageContext.request.contextPath}/admin/barbers" class="btn btn-primary">
                            <i class="fas fa-plus"></i>
                            Ajouter un coiffeur
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
