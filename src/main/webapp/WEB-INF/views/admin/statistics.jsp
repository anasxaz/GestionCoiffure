<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Statistiques - GestionCoiffure</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
         
        .chart-card {
            background: white;
            border: 1px solid var(--color-neutral-200);
            border-radius: var(--radius-xl);
            padding: var(--space-6);
            transition: all var(--transition-base);
        }

        .chart-card:hover {
            box-shadow: var(--shadow-md);
        }

        .chart-title {
            font-size: var(--text-xl);
            font-weight: var(--font-weight-semibold);
            color: var(--color-neutral-900);
            margin-bottom: var(--space-6);
            display: flex;
            align-items: center;
            gap: var(--space-3);
        }

        .chart-title i {
            color: var(--color-primary);
        }

        .bar-chart {
            display: flex;
            flex-direction: column;
            gap: var(--space-5);
        }

        .bar-item {
            display: flex;
            flex-direction: column;
            gap: var(--space-2);
        }

        .bar-label {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: var(--text-sm);
            color: var(--color-neutral-700);
        }

        .bar-label-left {
            display: flex;
            align-items: center;
            gap: var(--space-2);
            font-weight: var(--font-weight-medium);
        }

        .bar-label-right {
            font-weight: var(--font-weight-semibold);
            color: var(--color-neutral-900);
        }

        .bar-container {
            background: var(--color-neutral-100);
            height: 32px;
            border-radius: var(--radius-md);
            overflow: hidden;
            position: relative;
        }

        .bar-fill {
            height: 100%;
            border-radius: var(--radius-md);
            transition: width 0.8s cubic-bezier(0.4, 0, 0.2, 1);
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding-right: var(--space-3);
            color: white;
            font-size: var(--text-sm);
            font-weight: var(--font-weight-semibold);
        }

        .bar-fill.gradient-1 {
            background: linear-gradient(90deg, var(--color-primary), var(--color-primary-light));
        }

        .bar-fill.gradient-2 {
            background: linear-gradient(90deg, var(--color-success), #34d399);
        }

        .bar-fill.gradient-3 {
            background: linear-gradient(90deg, var(--color-secondary), var(--color-secondary-light));
        }

        .bar-fill.gradient-4 {
            background: linear-gradient(90deg, var(--color-warning), var(--color-warning-light));
        }

        .pie-chart-container {
            display: flex;
            align-items: center;
            gap: var(--space-8);
            flex-wrap: wrap;
        }

        .pie-visual {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            position: relative;
            background: conic-gradient(
                var(--color-warning) 0deg,
                var(--color-warning) var(--pending-deg),
                var(--color-success) var(--pending-deg),
                var(--color-success) var(--confirmed-deg),
                var(--color-primary) var(--confirmed-deg),
                var(--color-primary) var(--completed-deg),
                var(--color-neutral-400) var(--completed-deg),
                var(--color-neutral-400) 360deg
            );
            box-shadow: var(--shadow-md);
        }

        .pie-legend {
            flex: 1;
            min-width: 200px;
            display: flex;
            flex-direction: column;
            gap: var(--space-3);
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: var(--space-3);
            padding: var(--space-3);
            border-radius: var(--radius-md);
            background: var(--color-neutral-50);
            transition: all var(--transition-fast);
        }

        .legend-item:hover {
            background: var(--color-neutral-100);
            transform: translateX(4px);
        }

        .legend-color {
            width: 20px;
            height: 20px;
            border-radius: var(--radius-sm);
            flex-shrink: 0;
        }

        .legend-text {
            flex: 1;
            font-size: var(--text-sm);
            font-weight: var(--font-weight-medium);
            color: var(--color-neutral-700);
        }

        .legend-value {
            font-weight: var(--font-weight-bold);
            color: var(--color-neutral-900);
            font-size: var(--text-base);
        }

        .charts-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: var(--space-6);
            margin-bottom: var(--space-6);
        }

        @media (max-width: 1024px) {
            .charts-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="dashboard-page">
        
        <nav class="navbar role-admin">
            <div class="navbar-container">
                <div class="navbar-brand">
                    <div class="navbar-brand-icon">
                        <i class="fas fa-cut"></i>
                    </div>
                    <span>GestionCoiffure</span>
                </div>

                <ul class="navbar-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link">
                            <i class="fas fa-arrow-left"></i>
                            <span>Tableau de bord</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/statistics" class="navbar-link active">
                            <i class="fas fa-chart-line"></i>
                            <span>Statistiques</span>
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
                    <h1 class="page-title">Statistiques & Rapports</h1>
                    <p class="page-subtitle">Analyse des performances et tendances</p>
                </div>
            </div>

            <div class="metrics-row">
                <div class="stat-card" style="background: linear-gradient(135deg, var(--color-admin) 0%, var(--color-admin-light) 100%); border: none; color: white;">
                    <div class="stat-card-icon" style="background: rgba(255, 255, 255, 0.2); color: white;">
                        <i class="fas fa-coins"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label" style="color: rgba(255, 255, 255, 0.9);">Revenu Total</div>
                        <div class="stat-card-value" style="color: white;">
                            <fmt:formatNumber value="${totalRevenue}" pattern="#,##0.00"/> MAD
                        </div>
                        <div class="stat-card-change" style="color: rgba(255, 255, 255, 0.9); border-color: rgba(255, 255, 255, 0.2);">
                            <i class="fas fa-check-circle"></i>
                            <span>RDV complétés</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-primary-50); color: var(--color-primary);">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Total Rendez-vous</div>
                        <div class="stat-card-value">${totalAppointments}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-check" style="color: var(--color-success);"></i>
                            <c:set var="completed" value="${appointmentsByStatus['completed'] != null ? appointmentsByStatus['completed'] : 0}"/>
                            <span>${completed} Complétés</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light); color: var(--color-success);">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Clients Actifs</div>
                        <div class="stat-card-value">${totalClients}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-crown" style="color: var(--color-warning);"></i>
                            <span>${loyaltyStats['fidele'] != null ? loyaltyStats['fidele'] : 0} Fidèles</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-secondary-light); color: var(--color-secondary-dark);">
                        <i class="fas fa-user-tie"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Coiffeurs</div>
                        <div class="stat-card-value">${activeBarbers}/${totalBarbers}</div>
                        <div class="stat-card-change">
                            <span class="status-dot status-dot-success"></span>
                            <span>Actifs / Total</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="charts-grid">
                
                <div class="chart-card">
                    <div class="chart-title">
                        <i class="fas fa-chart-pie"></i>
                        Répartition des Rendez-vous
                    </div>

                    <c:set var="pending" value="${appointmentsByStatus['pending'] != null ? appointmentsByStatus['pending'] : 0}"/>
                    <c:set var="confirmed" value="${appointmentsByStatus['confirmed'] != null ? appointmentsByStatus['confirmed'] : 0}"/>
                    <c:set var="completed" value="${appointmentsByStatus['completed'] != null ? appointmentsByStatus['completed'] : 0}"/>
                    <c:set var="cancelled" value="${appointmentsByStatus['cancelled'] != null ? appointmentsByStatus['cancelled'] : 0}"/>
                    <c:set var="total" value="${pending + confirmed + completed + cancelled}"/>

                    <div class="pie-chart-container">
                        <div class="pie-visual" style="
                            --pending-deg: ${total > 0 ? (pending * 360 / total) : 0}deg;
                            --confirmed-deg: ${total > 0 ? ((pending + confirmed) * 360 / total) : 0}deg;
                            --completed-deg: ${total > 0 ? ((pending + confirmed + completed) * 360 / total) : 0}deg;
                        "></div>

                        <div class="pie-legend">
                            <div class="legend-item">
                                <div class="legend-color" style="background: var(--color-warning);"></div>
                                <div class="legend-text">En attente</div>
                                <div class="legend-value">${pending}</div>
                            </div>
                            <div class="legend-item">
                                <div class="legend-color" style="background: var(--color-success);"></div>
                                <div class="legend-text">Confirmés</div>
                                <div class="legend-value">${confirmed}</div>
                            </div>
                            <div class="legend-item">
                                <div class="legend-color" style="background: var(--color-primary);"></div>
                                <div class="legend-text">Complétés</div>
                                <div class="legend-value">${completed}</div>
                            </div>
                            <div class="legend-item">
                                <div class="legend-color" style="background: var(--color-neutral-400);"></div>
                                <div class="legend-text">Annulés</div>
                                <div class="legend-value">${cancelled}</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="chart-card">
                    <div class="chart-title">
                        <i class="fas fa-trophy"></i>
                        Services les plus demandés
                    </div>

                    <div class="bar-chart">
                        <c:set var="maxCount" value="0"/>
                        <c:forEach var="service" items="${topServices}">
                            <c:if test="${service.count > maxCount}">
                                <c:set var="maxCount" value="${service.count}"/>
                            </c:if>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${not empty topServices}">
                                <c:forEach var="service" items="${topServices}" varStatus="status">
                                    <div class="bar-item">
                                        <div class="bar-label">
                                            <div class="bar-label-left">
                                                <i class="fas fa-scissors" style="color: var(--color-primary);"></i>
                                                <span>${service.name}</span>
                                            </div>
                                            <div class="bar-label-right">
                                                ${service.count} RDV • <fmt:formatNumber value="${service.revenue}" pattern="#,##0"/> MAD
                                            </div>
                                        </div>
                                        <div class="bar-container">
                                            <div class="bar-fill gradient-${(status.index % 4) + 1}"
                                                 style="width: ${maxCount > 0 ? (service.count * 100 / maxCount) : 0}%;">
                                                ${service.count}
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div style="text-align: center; padding: var(--space-8); color: var(--color-neutral-500);">
                                    <i class="fas fa-chart-bar" style="font-size: var(--text-3xl); opacity: 0.3; margin-bottom: var(--space-4);"></i>
                                    <p>Aucune donnée disponible</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="chart-card">
                    <div class="chart-title">
                        <i class="fas fa-star"></i>
                        Meilleurs Coiffeurs
                    </div>

                    <div class="bar-chart">
                        <c:set var="maxBarberCount" value="0"/>
                        <c:forEach var="barber" items="${topBarbers}">
                            <c:if test="${barber.count > maxBarberCount}">
                                <c:set var="maxBarberCount" value="${barber.count}"/>
                            </c:if>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${not empty topBarbers}">
                                <c:forEach var="barber" items="${topBarbers}" varStatus="status">
                                    <div class="bar-item">
                                        <div class="bar-label">
                                            <div class="bar-label-left">
                                                <i class="fas fa-user-tie" style="color: var(--color-success);"></i>
                                                <span>${barber.name}</span>
                                            </div>
                                            <div class="bar-label-right">
                                                ${barber.count} RDV complétés
                                            </div>
                                        </div>
                                        <div class="bar-container">
                                            <div class="bar-fill gradient-${(status.index % 4) + 1}"
                                                 style="width: ${maxBarberCount > 0 ? (barber.count * 100 / maxBarberCount) : 0}%;">
                                                ${barber.count}
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div style="text-align: center; padding: var(--space-8); color: var(--color-neutral-500);">
                                    <i class="fas fa-user-tie" style="font-size: var(--text-3xl); opacity: 0.3; margin-bottom: var(--space-4);"></i>
                                    <p>Aucune donnée disponible</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="chart-card">
                    <div class="chart-title">
                        <i class="fas fa-chart-line"></i>
                        Tendances Mensuelles (6 derniers mois)
                    </div>

                    <div class="bar-chart">
                        <c:set var="maxMonthCount" value="0"/>
                        <c:forEach var="trend" items="${monthlyTrends}">
                            <c:if test="${trend.count > maxMonthCount}">
                                <c:set var="maxMonthCount" value="${trend.count}"/>
                            </c:if>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${not empty monthlyTrends}">
                                <c:forEach var="trend" items="${monthlyTrends}">
                                    <div class="bar-item">
                                        <div class="bar-label">
                                            <div class="bar-label-left">
                                                <i class="fas fa-calendar" style="color: var(--color-secondary);"></i>
                                                <span>${trend.month}</span>
                                            </div>
                                            <div class="bar-label-right">
                                                ${trend.count} RDV • ${trend.completed} Complétés
                                            </div>
                                        </div>
                                        <div class="bar-container">
                                            <div class="bar-fill gradient-1"
                                                 style="width: ${maxMonthCount > 0 ? (trend.count * 100 / maxMonthCount) : 0}%;">
                                                ${trend.count}
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div style="text-align: center; padding: var(--space-8); color: var(--color-neutral-500);">
                                    <i class="fas fa-calendar-alt" style="font-size: var(--text-3xl); opacity: 0.3; margin-bottom: var(--space-4);"></i>
                                    <p>Aucune donnée disponible</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div>
                <div class="chart-card">
                    <div class="chart-title">
                        <i class="fas fa-clock"></i>
                        Derniers Rendez-vous
                    </div>

                    <div class="table-container" style="border: none; border-radius: 0;">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th><i class="fas fa-calendar"></i> Date</th>
                                    <th><i class="fas fa-clock"></i> Heure</th>
                                    <th><i class="fas fa-user"></i> Client</th>
                                    <th><i class="fas fa-user-tie"></i> Coiffeur</th>
                                    <th><i class="fas fa-scissors"></i> Service</th>
                                    <th><i class="fas fa-info-circle"></i> Statut</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty recentAppointments}">
                                        <c:forEach var="apt" items="${recentAppointments}">
                                            <tr>
                                                <td>
                                                    <div style="display: flex; align-items: center; gap: var(--space-2);">
                                                        <i class="fas fa-calendar-day" style="color: var(--color-neutral-400); font-size: var(--text-xs);"></i>
                                                        <span class="font-medium">${apt.date}</span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div style="display: flex; align-items: center; gap: var(--space-2);">
                                                        <i class="fas fa-clock" style="color: var(--color-neutral-400); font-size: var(--text-xs);"></i>
                                                        <span class="font-medium">${apt.time}</span>
                                                    </div>
                                                </td>
                                                <td>
                                                    <span class="font-medium" style="color: var(--color-neutral-900);">${apt.client}</span>
                                                </td>
                                                <td>
                                                    <span class="font-medium" style="color: var(--color-neutral-900);">${apt.barber}</span>
                                                </td>
                                                <td>${apt.service}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${apt.status == 'pending'}">
                                                            <span class="badge badge-warning">
                                                                <i class="fas fa-hourglass-half"></i>
                                                                En attente
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${apt.status == 'confirmed'}">
                                                            <span class="badge badge-success">
                                                                <i class="fas fa-check"></i>
                                                                Confirmé
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${apt.status == 'completed'}">
                                                            <span class="badge badge-primary">
                                                                <i class="fas fa-check-double"></i>
                                                                Complété
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-error">
                                                                <i class="fas fa-times"></i>
                                                                Annulé
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="6">
                                                <div class="empty-state" style="padding: var(--space-12) var(--space-6);">
                                                    <div class="empty-state-icon">
                                                        <i class="fas fa-calendar-times"></i>
                                                    </div>
                                                    <div class="empty-state-title">Aucun rendez-vous récent</div>
                                                    <div class="empty-state-description">
                                                        Les rendez-vous récents apparaîtront ici.
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
