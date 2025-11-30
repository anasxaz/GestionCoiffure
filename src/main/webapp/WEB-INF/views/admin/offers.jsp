<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Offres - Barbershop</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-page">
        
        <nav class="navbar role-admin">
            <div class="navbar-container">
                <div class="navbar-brand">
                    <div class="navbar-brand-icon">
                        <i class="fas fa-gift"></i>
                    </div>
                    <span>Offres de Fidélité</span>
                </div>
                <ul class="navbar-menu">
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link">
                            <i class="fas fa-arrow-left"></i>
                            <span>Retour au Dashboard</span>
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
                    <h1 class="page-title">Gestion des Offres</h1>
                    <p class="page-subtitle">Gérez les offres de fidélité et récompenses pour vos clients</p>
                </div>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/admin/offers?action=add" class="btn btn-primary">
                        <i class="fas fa-plus-circle"></i>
                        Ajouter une Offre
                    </a>
                </div>
            </div>

            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Offre créée avec succès</div>
                        La nouvelle offre a été ajoutée au programme de fidélité.
                    </div>
                </div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Offre mise à jour</div>
                        Les modifications ont été enregistrées avec succès.
                    </div>
                </div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Offre supprimée</div>
                        L'offre a été retirée du programme de fidélité.
                    </div>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty offers}">
                    
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <i class="fas fa-gift"></i>
                        </div>
                        <h3 class="empty-state-title">Aucune offre disponible</h3>
                        <p class="empty-state-description">
                            Commencez à créer des offres de fidélité pour récompenser vos clients réguliers.
                        </p>
                        <a href="${pageContext.request.contextPath}/admin/offers?action=add" class="btn btn-primary">
                            <i class="fas fa-plus-circle"></i>
                            Créer votre première offre
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    
                    <div class="grid grid-auto-fill">
                        <c:forEach var="offer" items="${offers}">
                            <div class="offer-card">
                                <div class="offer-card-header">
                                    <div style="display: flex; align-items: center; justify-content: space-between;">
                                        <h3 class="offer-card-title">${offer.title}</h3>
                                        <c:choose>
                                            <c:when test="${offer.active}">
                                                <span class="badge badge-success">
                                                    <span class="status-dot status-dot-success"></span>
                                                    Actif
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral">
                                                    <span class="status-dot status-dot-neutral"></span>
                                                    Inactif
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <p class="offer-card-description">
                                    <c:choose>
                                        <c:when test="${not empty offer.description}">
                                            ${offer.description}
                                        </c:when>
                                        <c:otherwise>
                                            Aucune description disponible
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <div class="offer-card-meta">
                                    <div class="offer-card-meta-item">
                                        <i class="fas fa-coins"></i>
                                        <span><strong>${offer.pointsRequired}</strong> points requis</span>
                                    </div>
                                    <div class="offer-card-meta-item">
                                        <i class="fas fa-tag"></i>
                                        <span>Réduction de <strong>10%</strong></span>
                                    </div>
                                    <div class="offer-card-meta-item">
                                        <i class="fas fa-hashtag"></i>
                                        <span>ID: <strong>${offer.offerId}</strong></span>
                                    </div>
                                </div>

                                <div class="offer-card-actions">
                                    <div style="display: flex; gap: var(--space-2); width: 100%;">
                                        <a href="${pageContext.request.contextPath}/admin/offers?action=edit&id=${offer.offerId}"
                                           class="btn btn-secondary btn-sm" style="flex: 1;">
                                            <i class="fas fa-edit"></i>
                                            Modifier
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/offers?action=toggle&id=${offer.offerId}"
                                           class="btn ${offer.active ? 'btn-warning' : 'btn-success'} btn-sm" style="flex: 1;">
                                            <i class="fas fa-toggle-${offer.active ? 'off' : 'on'}"></i>
                                            ${offer.active ? 'Désactiver' : 'Activer'}
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/offers?action=delete&id=${offer.offerId}"
                                           class="btn btn-error btn-sm btn-icon"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette offre ?')">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                </c:otherwise>
            </c:choose>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
