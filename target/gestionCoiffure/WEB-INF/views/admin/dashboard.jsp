<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Barbershop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Navigation Bar -->
    <nav class="navbar role-admin">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon"><i class="fas fa-cut"></i></div>
                <div>
                    <div style="font-weight: 700;">GestionCoiffure</div>
                    <div style="font-size: 0.75rem; opacity: 0.9;">Administration</div>
                </div>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link active"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/barbers" class="navbar-link"><i class="fas fa-user-tie"></i><span>Coiffeurs</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/services" class="navbar-link"><i class="fas fa-scissors"></i><span>Services</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <!-- Main Dashboard Content -->
    <main class="dashboard-main">
        <div class="dashboard-container">
            <!-- Page Header -->
            <div class="page-header-row">
                <div class="page-header-content">
                    <h1 class="page-title">Tableau de bord</h1>
                    <p class="page-subtitle">Bienvenue, ${sessionScope.userName}. Gérez votre salon de coiffure efficacement.</p>
                </div>
                <div class="page-header-actions">
                    <button class="btn btn-outline btn-sm" onclick="location.reload()">
                        <i class="fas fa-sync-alt"></i>
                        <span>Actualiser</span>
                    </button>
                </div>
            </div>

            <!-- Quick Stats Section -->
            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-admin-light);">
                        <i class="fas fa-user-tie" style="color: var(--color-admin);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Coiffeurs</div>
                        <div class="stat-card-value">${totalBarbers}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light);">
                        <i class="fas fa-calendar-check" style="color: var(--color-success);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Aujourd'hui</div>
                        <div class="stat-card-value">${todayAppointments}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-warning-light);">
                        <i class="fas fa-users" style="color: var(--color-warning);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Clients</div>
                        <div class="stat-card-value">${totalClients}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-info-light);">
                        <i class="fas fa-scissors" style="color: var(--color-info);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Services</div>
                        <div class="stat-card-value">${totalServices}</div>
                    </div>
                </div>
            </div>

            <!-- Management Grid -->
            <div class="grid grid-auto-fill">
                <a href="${pageContext.request.contextPath}/admin/barbers" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-admin-light);">
                            <i class="fas fa-user-tie" style="color: var(--color-admin);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Coiffeurs</h3>
                        <p class="card-subtitle">Gérer les coiffeurs</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/services" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-primary-light);">
                            <i class="fas fa-scissors" style="color: var(--color-primary);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Services</h3>
                        <p class="card-subtitle">Catalogue de services</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/appointments" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-info-light);">
                            <i class="fas fa-calendar-check" style="color: var(--color-info);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Rendez-vous</h3>
                        <p class="card-subtitle">Tous les rendez-vous</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/availability" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-success-light);">
                            <i class="fas fa-clock" style="color: var(--color-success);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Disponibilités</h3>
                        <p class="card-subtitle">Horaires des coiffeurs</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/offers" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-accent-light);">
                            <i class="fas fa-gift" style="color: var(--color-accent);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Offres</h3>
                        <p class="card-subtitle">Programme fidélité</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/clients" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-warning-light);">
                            <i class="fas fa-users" style="color: var(--color-warning);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Clients</h3>
                        <p class="card-subtitle">Base de données clients</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/admin/statistics" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-error-light);">
                            <i class="fas fa-chart-line" style="color: var(--color-error);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Statistiques</h3>
                        <p class="card-subtitle">Rapports et analyses</p>
                    </div>
                </a>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
