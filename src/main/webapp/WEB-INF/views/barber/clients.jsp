<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Clients - GestionCoiffure</title>
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
                <li><a href="${pageContext.request.contextPath}/barber/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/schedule" class="navbar-link"><i class="fas fa-calendar-week"></i><span>Emploi du Temps</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/clients" class="navbar-link active"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Mes Clients</h1>
                <p class="page-subtitle">Historique et informations de vos clients</p>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty clients}">
                <div class="empty-state">
                    <div class="empty-state-icon"><i class="fas fa-user-slash"></i></div>
                    <h3 class="empty-state-title">Aucun client</h3>
                    <p class="empty-state-description">Aucun client n'a encore pris rendez-vous avec vous</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-user"></i> Nom</th>
                                <th><i class="fas fa-envelope"></i> Email</th>
                                <th><i class="fas fa-phone"></i> Téléphone</th>
                                <th><i class="fas fa-crown"></i> Statut</th>
                                <th><i class="fas fa-coins"></i> Points</th>
                                <th><i class="fas fa-calendar-check"></i> RDV Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="client" items="${clients}">
                                <tr>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 0.5rem;">
                                            <div style="width: 32px; height: 32px; border-radius: 50%; background: linear-gradient(135deg, var(--color-barber), var(--color-barber-light)); display: flex; align-items: center; justify-content: center; color: white; font-weight: 700; font-size: 0.875rem;">
                                                ${client.name.substring(0,1)}
                                            </div>
                                            <strong>${client.name}</strong>
                                        </div>
                                    </td>
                                    <td>${client.email}</td>
                                    <td>${client.phone}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${client.loyaltyStatus == 'FIDELE'}">
                                                <span class="badge" style="background: linear-gradient(135deg, var(--color-accent), var(--color-accent-light)); color: white;">
                                                    <i class="fas fa-crown"></i> Fidèle
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral"><i class="fas fa-user"></i> Standard</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge badge-success">
                                            <i class="fas fa-coins"></i> ${client.pointsBalance} pts
                                        </span>
                                    </td>
                                    <td><strong>${client.totalAppointments}</strong></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:set var="baseUrl" value="${pageContext.request.contextPath}/barber/clients" />
                <c:set var="itemName" value="client(s)" />
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="baseUrl" value="${baseUrl}" />
                    <jsp:param name="totalItems" value="${totalClients}" />
                    <jsp:param name="itemName" value="${itemName}" />
                </jsp:include>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
