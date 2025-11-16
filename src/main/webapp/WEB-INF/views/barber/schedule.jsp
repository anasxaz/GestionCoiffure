<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Emploi du Temps - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .week-navigation {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: var(--space-4);
            margin-bottom: var(--space-8);
            flex-wrap: wrap;
        }
        .week-info-display {
            background: white;
            padding: var(--space-4) var(--space-6);
            border-radius: var(--radius-lg);
            border: 1px solid var(--color-neutral-200);
            font-weight: var(--font-weight-semibold);
            color: var(--color-neutral-900);
            display: flex;
            align-items: center;
            gap: var(--space-2);
        }
    </style>
</head>
<body class="dashboard-page">
    <!-- Navigation -->
    <nav class="navbar role-barber">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon">
                    <i class="fas fa-cut"></i>
                </div>
                <div>
                    <div style="font-weight: 700;">GestionCoiffure</div>
                    <div style="font-size: 0.75rem; opacity: 0.9;">Espace Coiffeur</div>
                </div>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/barber/dashboard" class="navbar-link"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/schedule" class="navbar-link active"><i class="fas fa-calendar-week"></i><span>Emploi du Temps</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <!-- Main Content -->
    <main class="dashboard-main">
        <!-- Page Header -->
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Mon Emploi du Temps</h1>
                <p class="page-subtitle">Consultez vos disponibilités et votre calendrier hebdomadaire</p>
            </div>
        </div>

        <!-- Availability Section -->
        <div class="card" style="margin-bottom: var(--space-8);">
            <div class="card-header">
                <div>
                    <h2 class="card-title">Mes Disponibilités</h2>
                    <p class="card-subtitle">Vos horaires de travail définis</p>
                </div>
            </div>
            <div class="card-body">
                <div class="alert alert-info" style="margin-bottom: var(--space-6);">
                    <i class="alert-icon fas fa-info-circle"></i>
                    <div class="alert-content">
                        Pour modifier vos disponibilités, veuillez contacter l'administrateur.
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty availability}">
                        <div class="grid grid-auto-fill" style="gap: var(--space-4);">
                            <c:forEach var="avail" items="${availability}">
                                <div class="card card-flat" style="padding: var(--space-4);">
                                    <div style="font-weight: 700; color: var(--color-barber); margin-bottom: var(--space-2); display: flex; align-items: center; gap: var(--space-2);">
                                        <i class="fas fa-calendar-day"></i>
                                        <c:choose>
                                            <c:when test="${avail.dayOfWeek == 'MON'}">Lundi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'TUE'}">Mardi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'WED'}">Mercredi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'THU'}">Jeudi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'FRI'}">Vendredi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'SAT'}">Samedi</c:when>
                                            <c:when test="${avail.dayOfWeek == 'SUN'}">Dimanche</c:when>
                                        </c:choose>
                                    </div>
                                    <div style="color: var(--color-neutral-600); font-size: var(--text-sm); display: flex; align-items: center; gap: var(--space-2);">
                                        <i class="fas fa-clock"></i>
                                        ${avail.startTime} - ${avail.endTime}
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="empty-state-icon">
                                <i class="fas fa-calendar-times"></i>
                            </div>
                            <h3 class="empty-state-title">Aucune disponibilité</h3>
                            <p class="empty-state-description">
                                Contactez l'administrateur pour configurer vos disponibilités.
                            </p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Week Navigation -->
        <div class="week-navigation">
            <a href="${pageContext.request.contextPath}/barber/schedule?week=${previousWeek}" class="btn btn-secondary">
                <i class="fas fa-chevron-left"></i>
                Semaine précédente
            </a>
            <div class="week-info-display">
                <i class="fas fa-calendar-week"></i>
                Semaine du ${weekDays[0]} au ${weekDays[6]}
            </div>
            <a href="${pageContext.request.contextPath}/barber/schedule?week=${nextWeek}" class="btn btn-secondary">
                Semaine suivante
                <i class="fas fa-chevron-right"></i>
            </a>
        </div>

        <!-- Calendar -->
        <div class="schedule-grid">
            <div class="schedule-header">
                <div class="schedule-header-cell"><i class="fas fa-clock"></i> Heure</div>
                <c:forEach var="dayDate" items="${weekDaysAsDate}">
                    <div class="schedule-header-cell ${dayDate.time == todayDate.time ? 'today' : ''}">
                        <fmt:formatDate value="${dayDate}" pattern="EEEE" />
                        <div style="font-size: 0.75rem; opacity: 0.9; margin-top: 0.25rem;">
                            <fmt:formatDate value="${dayDate}" pattern="dd/MM" />
                        </div>
                    </div>
                </c:forEach>
            </div>

            <c:forEach var="hour" begin="8" end="19">
                <div class="schedule-body">
                    <div class="schedule-time-cell">${hour}:00</div>
                    <c:forEach var="day" items="${weekDays}">
                        <div class="schedule-cell">
                            <c:forEach var="apt" items="${appointmentsByDay[day]}">
                                <c:if test="${apt.startTime.hour == hour}">
                                    <div class="schedule-appointment">
                                        <div class="schedule-appointment-time">
                                            <i class="fas fa-clock"></i>
                                            ${apt.startTime} - ${apt.endTime}
                                        </div>
                                        <div class="schedule-appointment-client">
                                            <i class="fas fa-user"></i>
                                            ${apt.clientName}
                                        </div>
                                        <div style="font-size: 0.625rem; opacity: 0.8; margin-top: 0.25rem;">
                                            <i class="fas fa-cut"></i>
                                            ${apt.serviceName}
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
