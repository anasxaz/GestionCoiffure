<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.LocalDate" %>
<%
    request.setAttribute("today", LocalDate.now().toString());
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Rendez-vous - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <nav class="navbar role-client">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon"><i class="fas fa-cut"></i></div>
                <div>
                    <div style="font-weight: 700;">GestionCoiffure</div>
                    <div style="font-size: 0.75rem; opacity: 0.9;">Espace Client</div>
                </div>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/client/dashboard" class="navbar-link"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/appointments" class="navbar-link active"><i class="fas fa-calendar-check"></i><span>Mes Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/book-appointment" class="navbar-link"><i class="fas fa-calendar-plus"></i><span>Réserver</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/offers" class="navbar-link"><i class="fas fa-gift"></i><span>Offres</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Mes Rendez-vous</h1>
                <p class="page-subtitle">Consultez vos rendez-vous passés et à venir</p>
            </div>
            <div class="page-actions">
                <a href="${pageContext.request.contextPath}/client/book-appointment" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Nouveau Rendez-vous
                </a>
            </div>
        </div>

        <c:if test="${param.success == 'cancelled'}">
            <div class="alert alert-success">
                <i class="alert-icon fas fa-check-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Rendez-vous annulé</div>
                    Votre rendez-vous a été annulé avec succès.
                </div>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty appointments}">
                <div class="empty-state">
                    <div class="empty-state-icon"><i class="fas fa-calendar-times"></i></div>
                    <h3 class="empty-state-title">Aucun rendez-vous</h3>
                    <p class="empty-state-description">Vous n'avez aucun rendez-vous. Réservez dès maintenant!</p>
                    <a href="${pageContext.request.contextPath}/client/book-appointment" class="btn btn-primary">
                        <i class="fas fa-calendar-plus"></i> Réserver un rendez-vous
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid grid-auto-fill">
                    <c:forEach var="apt" items="${appointments}">
                        <div class="appointment-card">
                            <div class="appointment-card-header">
                                <div class="appointment-card-date">
                                    <i class="fas fa-calendar"></i>
                                    ${apt.date}
                                </div>
                                <c:choose>
                                    <c:when test="${apt.status == 'pending'}">
                                        <span class="badge badge-warning"><i class="status-dot status-dot-warning"></i> En attente</span>
                                    </c:when>
                                    <c:when test="${apt.status == 'confirmed'}">
                                        <span class="badge badge-primary"><i class="status-dot" style="background: var(--color-primary);"></i> Confirmé</span>
                                    </c:when>
                                    <c:when test="${apt.status == 'refused'}">
                                        <span class="badge badge-error"><i class="status-dot status-dot-error"></i> Refusé</span>
                                    </c:when>
                                    <c:when test="${apt.status == 'cancelled'}">
                                        <span class="badge badge-neutral"><i class="status-dot status-dot-neutral"></i> Annulé</span>
                                    </c:when>
                                    <c:when test="${apt.status == 'completed'}">
                                        <span class="badge badge-success"><i class="status-dot status-dot-success"></i> Terminé</span>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="appointment-card-body">
                                <h3 class="appointment-card-title">${apt.serviceName}</h3>
                                <div class="appointment-card-info">
                                    <div class="appointment-card-info-item">
                                        <i class="fas fa-clock"></i>
                                        <span>${apt.startTime} - ${apt.endTime}</span>
                                    </div>
                                    <div class="appointment-card-info-item">
                                        <i class="fas fa-user"></i>
                                        <span>${apt.barberName}</span>
                                    </div>
                                    <div class="appointment-card-info-item">
                                        <i class="fas fa-money-bill-wave"></i>
                                        <span>${apt.servicePrice} MAD</span>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${(apt.status == 'pending' || apt.status == 'confirmed') && apt.date >= today}">
                                <div class="appointment-card-actions">
                                    <a href="${pageContext.request.contextPath}/client/appointments?action=cancel&id=${apt.appointmentId}"
                                       class="btn btn-sm btn-error"
                                       data-confirm="Voulez-vous vraiment annuler ce rendez-vous ?">
                                        <i class="fas fa-times"></i> Annuler
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
