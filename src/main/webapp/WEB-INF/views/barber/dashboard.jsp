<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de Bord Coiffeur - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Modern Minimalist Navbar -->
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
                <li><a href="${pageContext.request.contextPath}/barber/dashboard" class="navbar-link active"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/schedule" class="navbar-link"><i class="fas fa-calendar-week"></i><span>Emploi du Temps</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <!-- Dashboard Main Content -->
    <main class="dashboard-main">
        <div class="dashboard-container">
            <!-- Page Header -->
            <div class="page-header-row">
                <div class="page-header-info">
                    <h1 class="page-title">Bonjour, ${sessionScope.userName}!</h1>
                    <p class="page-subtitle">Gérez vos rendez-vous et optimisez votre emploi du temps</p>
                </div>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/barber/create-appointment" class="btn btn-primary">
                        <i class="fas fa-plus"></i>
                        Nouveau Rendez-vous
                    </a>
                </div>
            </div>

            <!-- Statistics Cards Row -->
            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-barber-light); color: var(--color-barber);">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Aujourd'hui</div>
                        <div class="stat-card-value">${todayAppointments != null ? todayAppointments : 0}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-calendar-day" style="color: var(--color-barber);"></i>
                            <span>Rendez-vous du jour</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light); color: var(--color-success);">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Confirmés</div>
                        <div class="stat-card-value">${confirmedAppointments != null ? confirmedAppointments : 0}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-check" style="color: var(--color-success);"></i>
                            <span>Rendez-vous confirmés</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-warning-light); color: var(--color-warning);">
                        <i class="fas fa-clock"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">En Attente</div>
                        <div class="stat-card-value">${pendingAppointments != null ? pendingAppointments : 0}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-hourglass-half" style="color: var(--color-warning);"></i>
                            <span>À confirmer</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-info-light); color: var(--color-info);">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Clients ce Mois</div>
                        <div class="stat-card-value">${monthlyClients != null ? monthlyClients : 0}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-user-check" style="color: var(--color-info);"></i>
                            <span>Clients uniques</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Quick Actions Grid -->
            <div class="grid grid-auto-fill">
                <a href="${pageContext.request.contextPath}/barber/appointments" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-barber-light);">
                            <i class="fas fa-calendar-check" style="color: var(--color-barber);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Rendez-vous</h3>
                        <p class="card-subtitle">Gérer vos rendez-vous</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/barber/schedule" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-info-light);">
                            <i class="fas fa-calendar-week" style="color: var(--color-info);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Emploi du Temps</h3>
                        <p class="card-subtitle">Planning de la semaine</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/barber/clients" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-success-light);">
                            <i class="fas fa-users" style="color: var(--color-success);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Mes Clients</h3>
                        <p class="card-subtitle">Historique clients</p>
                    </div>
                </a>

                <a href="${pageContext.request.contextPath}/barber/create-appointment" class="card card-hover" style="text-decoration: none; color: inherit;">
                    <div class="card-header">
                        <div class="stat-icon" style="background: var(--color-accent-light);">
                            <i class="fas fa-plus-circle" style="color: var(--color-accent);"></i>
                        </div>
                    </div>
                    <div class="card-body">
                        <h3 class="card-title">Créer RDV</h3>
                        <p class="card-subtitle">Nouveau rendez-vous</p>
                    </div>
                </a>
            </div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
