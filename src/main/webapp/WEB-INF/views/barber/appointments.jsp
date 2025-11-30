<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <li><a href="${pageContext.request.contextPath}/barber/appointments" class="navbar-link active"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/schedule" class="navbar-link"><i class="fas fa-calendar-week"></i><span>Emploi du Temps</span></a></li>
                <li><a href="${pageContext.request.contextPath}/barber/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Mes Rendez-vous</h1>
                <p class="page-subtitle">Gérez vos rendez-vous clients</p>
            </div>
        </div>

        <c:if test="${param.success == 'confirmed'}">
            <div class="alert alert-success">
                <i class="alert-icon fas fa-check-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Rendez-vous confirmé</div>
                    Le rendez-vous a été confirmé avec succès.
                </div>
            </div>
        </c:if>
        <c:if test="${param.success == 'refused'}">
            <div class="alert alert-error">
                <i class="alert-icon fas fa-times-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Rendez-vous refusé</div>
                    Le rendez-vous a été refusé.
                </div>
            </div>
        </c:if>
        <c:if test="${param.success == 'completed'}">
            <div class="alert alert-success">
                <i class="alert-icon fas fa-check-double"></i>
                <div class="alert-content">
                    <div class="alert-title">Rendez-vous terminé</div>
                    Le rendez-vous a été marqué comme terminé. Points attribués au client !
                </div>
            </div>
        </c:if>

        <c:choose>
            <c:when test="${not empty appointments}">
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-calendar"></i> Date</th>
                                <th><i class="fas fa-clock"></i> Heure</th>
                                <th><i class="fas fa-user"></i> Client</th>
                                <th><i class="fas fa-cut"></i> Service</th>
                                <th><i class="fas fa-money-bill-wave"></i> Prix</th>
                                <th><i class="fas fa-info-circle"></i> Statut</th>
                                <th style="text-align: right;"><i class="fas fa-cog"></i> Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="apt" items="${appointments}">
                                <c:set var="service" value="${serviceMap[apt.serviceId]}"/>
                                <c:set var="redemption" value="${redemptionMap[apt.redemptionId]}"/>
                                <tr>
                                    <td><strong>${apt.date}</strong></td>
                                    <td>${apt.startTime} - ${apt.endTime}</td>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 0.5rem;">
                                            <div style="width: 32px; height: 32px; border-radius: 50%; background: linear-gradient(135deg, var(--color-barber), var(--color-barber-light)); display: flex; align-items: center; justify-content: center; color: white; font-weight: 700; font-size: 0.875rem;">
                                                ${apt.clientName.substring(0,1)}
                                            </div>
                                            <span>${apt.clientName}</span>
                                        </div>
                                    </td>
                                    <td>${apt.serviceName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${redemption != null}">
                                                <div>
                                                    <div style="text-decoration: line-through; color: var(--color-neutral-400); font-size: 0.75rem;">${service.price} MAD</div>
                                                    <div style="font-weight: 700; color: var(--color-success); font-size: 1rem;">${service.price * 0.9} MAD</div>
                                                    <span class="badge" style="background: var(--color-accent-light); color: var(--color-accent-dark); font-size: 0.625rem;">-10%</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <strong>${service.price} MAD</strong>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
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
                                    </td>
                                    <td>
                                        <div class="table-actions-group" style="justify-content: flex-end;">
                                            <c:if test="${apt.status == 'pending'}">
                                                <a href="${pageContext.request.contextPath}/barber/appointments?action=confirm&id=${apt.appointmentId}"
                                                   class="btn btn-sm btn-success"
                                                   data-confirm="Confirmer ce rendez-vous ?">
                                                    <i class="fas fa-check"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/barber/appointments?action=refuse&id=${apt.appointmentId}"
                                                   class="btn btn-sm btn-error"
                                                   data-confirm="Refuser ce rendez-vous ?">
                                                    <i class="fas fa-times"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${apt.status == 'confirmed'}">
                                                <a href="${pageContext.request.contextPath}/barber/appointments?action=complete&id=${apt.appointmentId}"
                                                   class="btn btn-sm btn-accent"
                                                   data-confirm="Marquer comme terminé ?">
                                                    <i class="fas fa-check-double"></i> Terminer
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <c:set var="baseUrl" value="${pageContext.request.contextPath}/barber/appointments" />
                <c:set var="itemName" value="rendez-vous" />
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="baseUrl" value="${baseUrl}" />
                    <jsp:param name="totalItems" value="${totalAppointments}" />
                    <jsp:param name="itemName" value="${itemName}" />
                </jsp:include>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-calendar-times"></i>
                    </div>
                    <h3 class="empty-state-title">Aucun rendez-vous</h3>
                    <p class="empty-state-description">
                        Vous n'avez aucun rendez-vous pour le moment.
                    </p>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
