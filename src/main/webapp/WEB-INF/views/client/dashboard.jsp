<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.Client" %>
<%
    Client client = (Client) session.getAttribute("user");
    request.setAttribute("client", client);
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de Bord Client - Gestion Coiffure</title>

    <!-- External Stylesheets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Navigation Bar -->
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
                <li><a href="${pageContext.request.contextPath}/client/dashboard" class="navbar-link active"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Mes Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/book-appointment" class="navbar-link"><i class="fas fa-calendar-plus"></i><span>Réserver</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/offers" class="navbar-link"><i class="fas fa-gift"></i><span>Offres</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <!-- Main Dashboard Content -->
    <main class="dashboard-main">
            <!-- Page Header -->
            <div class="page-header-row">
                <div class="page-header-info">
                    <h1 class="page-title">Bienvenue, ${sessionScope.userName}!</h1>
                    <p class="page-subtitle">Gérez vos rendez-vous et profitez de nos services</p>
                </div>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/client/book-appointment" class="btn btn-primary">
                        <i class="fas fa-calendar-plus"></i>
                        <span>Nouveau Rendez-vous</span>
                    </a>
                </div>
            </div>

            <!-- Stats Row -->
            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-client-light);">
                        <i class="fas fa-star" style="color: var(--color-client);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Statut</div>
                        <div class="stat-card-value">
                            ${client.loyaltyStatus == 'FIDELE' ? 'Fidèle' : 'Standard'}
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light);">
                        <i class="fas fa-coins" style="color: var(--color-success);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Points</div>
                        <div class="stat-card-value">${client.pointsBalance}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-accent-light);">
                        <i class="fas fa-gift" style="color: var(--color-accent);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Avantages</div>
                        <div class="stat-card-value" style="font-size: var(--text-sm);">
                            <c:choose>
                                <c:when test="${client.loyaltyStatus == 'FIDELE'}">Actifs</c:when>
                                <c:otherwise>Standard</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Quick Actions Grid -->
            <div class="grid grid-auto-fill">
                <a href="${pageContext.request.contextPath}/client/book-appointment" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-primary-light);">
                            <i class="fas fa-calendar-plus" style="color: var(--color-primary);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Prendre Rendez-vous</h3>
                        <p class="card-subtitle">Réservez votre créneau préféré</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/client/appointments" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-info-light);">
                            <i class="fas fa-calendar-check" style="color: var(--color-info);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Mes Rendez-vous</h3>
                        <p class="card-subtitle">Consultez vos rendez-vous</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/client/offers" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-accent-light);">
                            <i class="fas fa-gift" style="color: var(--color-accent);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Mes Offres</h3>
                        <p class="card-subtitle">Découvrez les offres fidélité</p>
                    </div>
                </a>
            </div>
    </main>

    <!-- External JavaScript -->
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
