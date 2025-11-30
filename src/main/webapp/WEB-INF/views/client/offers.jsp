<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Offres Fidélité - GestionCoiffure</title>
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
                <li><a href="${pageContext.request.contextPath}/client/appointments" class="navbar-link"><i class="fas fa-calendar-check"></i><span>Mes Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/book-appointment" class="navbar-link"><i class="fas fa-calendar-plus"></i><span>Réserver</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/offers" class="navbar-link active"><i class="fas fa-gift"></i><span>Offres</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Offres de Fidélité</h1>
                <p class="page-subtitle">Échangez vos points contre des offres exclusives</p>
            </div>
        </div>

        <div class="stat-card" style="margin-bottom: var(--space-8); max-width: 400px;">
            <div class="stat-card-icon" style="background: var(--color-client-light);">
                <i class="fas fa-coins" style="color: var(--color-client);"></i>
            </div>
            <div class="stat-card-content">
                <div class="stat-card-label">Points de Fidélité</div>
                <div class="stat-card-value">${client.pointsBalance}</div>
            </div>
        </div>

        <c:if test="${param.success == 'redeemed'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Offre récupérée !</div>
                    <div class="alert-message">L'offre a été ajoutée à votre compte. Utilisez-la lors de votre prochaine réservation.</div>
                </div>
            </div>
        </c:if>

        <c:if test="${param.error == 'notEnoughPoints'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Points insuffisants</div>
                    <div class="alert-message">Vous n'avez pas assez de points pour récupérer cette offre.</div>
                </div>
            </div>
        </c:if>

        <c:if test="${param.error == 'offerNotAvailable'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <div class="alert-content">
                    <div class="alert-title">Offre indisponible</div>
                    <div class="alert-message">Cette offre n'est plus disponible.</div>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty redeemedOffers}">
            <h2 class="section-title" style="margin-bottom: var(--space-4); font-size: var(--text-xl); font-weight: var(--font-weight-semibold);">
                Mes Offres Disponibles
            </h2>
            <div class="grid grid-auto-fill" style="margin-bottom: var(--space-8);">
                <c:forEach var="redemption" items="${redeemedOffers}">
                    <div class="card" style="border: 2px solid var(--color-success); background: var(--color-success-light);">
                        <div class="card-body">
                            <div style="display: flex; align-items: center; gap: var(--space-2); margin-bottom: var(--space-3);">
                                <i class="fas fa-check-circle" style="color: var(--color-success); font-size: var(--text-lg);"></i>
                                <h3 class="card-title" style="margin: 0;">${redemption.offerTitle}</h3>
                            </div>
                            <p style="color: var(--color-neutral-700); font-size: var(--text-sm); margin-bottom: var(--space-4);">
                                ${redemption.offerDescription}
                            </p>
                            <div style="display: flex; align-items: center; gap: var(--space-2); padding: var(--space-3); background: white; border-radius: var(--radius-md);">
                                <i class="fas fa-info-circle" style="color: var(--color-client);"></i>
                                <span style="font-size: var(--text-sm);">Prête à être utilisée lors de votre prochain rendez-vous</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <h2 class="section-title" style="margin-bottom: var(--space-4); font-size: var(--text-xl); font-weight: var(--font-weight-semibold);">
            Offres Disponibles
        </h2>

        <c:choose>
            <c:when test="${empty offers}">
                <div class="empty-state">
                    <div class="empty-state-icon"><i class="fas fa-gift"></i></div>
                    <h3 class="empty-state-title">Aucune offre disponible</h3>
                    <p class="empty-state-message">
                        Aucune offre de fidélité n'est disponible pour le moment.
                        Continuez à réserver pour gagner des points !
                    </p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid grid-auto-fill">
                    <c:forEach var="offer" items="${offers}">
                        <div class="card">
                            <div class="card-body">
                                <h3 class="card-title">${offer.title}</h3>
                                <p style="color: var(--color-neutral-600); font-size: var(--text-sm); margin-bottom: var(--space-4);">
                                    ${offer.description}
                                </p>
                                <div style="display: flex; align-items: center; gap: var(--space-2); margin-bottom: var(--space-4); padding: var(--space-3); background: var(--color-neutral-50); border-radius: var(--radius-md);">
                                    <i class="fas fa-coins" style="color: var(--color-warning);"></i>
                                    <span style="font-size: var(--text-sm);"><strong>${offer.pointsRequired}</strong> points requis</span>
                                </div>
                                <c:choose>
                                    <c:when test="${client.pointsBalance >= offer.pointsRequired}">
                                        <button onclick="if(confirm('Voulez-vous échanger ${offer.pointsRequired} points contre cette offre?')) location.href='${pageContext.request.contextPath}/client/offers?action=redeem&id=${offer.offerId}'"
                                                class="btn btn-primary" style="width: 100%;">
                                            <i class="fas fa-gift"></i>
                                            Récupérer l'offre
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <div style="padding: var(--space-3); background: var(--color-neutral-100); border-radius: var(--radius-md); text-align: center;">
                                            <span style="font-size: var(--text-sm); color: var(--color-neutral-600);">
                                                <i class="fas fa-lock"></i>
                                                Il vous manque ${offer.pointsRequired - client.pointsBalance} points
                                            </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    alert.style.animation = 'slideOut 0.3s ease-out forwards';
                    setTimeout(function() {
                        alert.remove();
                    }, 300);
                }, 5000);
            });
        });
    </script>
</body>
</html>
