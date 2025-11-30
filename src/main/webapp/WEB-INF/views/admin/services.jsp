<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gérer les Services - Barbershop</title>
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
                        <i class="fas fa-scissors"></i>
                    </div>
                    <span>Barbershop Admin</span>
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
                    <h1 class="page-title">Catalogue de Services</h1>
                    <p class="page-subtitle">Gérez les services proposés dans votre salon</p>
                </div>
                <div class="page-actions">
                    <a href="${pageContext.request.contextPath}/admin/services?action=add" class="btn btn-primary">
                        <i class="fas fa-plus"></i>
                        Ajouter un Service
                    </a>
                </div>
            </div>

            <c:if test="${param.success == 'created'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Service créé avec succès!</div>
                        Le nouveau service a été ajouté au catalogue.
                    </div>
                </div>
            </c:if>
            <c:if test="${param.success == 'updated'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Service mis à jour avec succès!</div>
                        Les modifications ont été enregistrées.
                    </div>
                </div>
            </c:if>
            <c:if test="${param.success == 'deleted'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle alert-icon"></i>
                    <div class="alert-content">
                        <div class="alert-title">Service supprimé avec succès!</div>
                        Le service a été retiré du catalogue.
                    </div>
                </div>
            </c:if>

            <div class="table-actions-bar">
                <div class="table-actions-left">
                    <div class="search-input">
                        <i class="fas fa-search"></i>
                        <input type="text" class="form-input" placeholder="Rechercher un service..." id="searchInput">
                    </div>
                </div>
                <div class="table-actions-right">
                    <select class="form-select filter-select" id="statusFilter">
                        <option value="all">Tous les services</option>
                        <option value="active">Actifs</option>
                        <option value="inactive">Inactifs</option>
                    </select>
                </div>
            </div>

            <div class="table-container with-actions">
                <table class="table" id="servicesTable">
                    <thead>
                        <tr>
                            <th><i class="fas fa-hashtag"></i> ID</th>
                            <th><i class="fas fa-cut"></i> Nom du Service</th>
                            <th><i class="fas fa-align-left"></i> Description</th>
                            <th><i class="fas fa-clock"></i> Durée</th>
                            <th><i class="fas fa-money-bill-wave"></i> Prix</th>
                            <th><i class="fas fa-toggle-on"></i> Statut</th>
                            <th><i class="fas fa-cog"></i> Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="service" items="${services}">
                            <tr data-status="${service.active ? 'active' : 'inactive'}">
                                <td>
                                    <span class="font-semibold text-neutral-700">#${service.serviceId}</span>
                                </td>
                                <td>
                                    <div class="flex items-center gap-3">
                                        <div class="service-card-icon" style="width: 32px; height: 32px; font-size: 14px;">
                                            <i class="fas fa-cut"></i>
                                        </div>
                                        <span class="font-semibold">${service.name}</span>
                                    </div>
                                </td>
                                <td>
                                    <span class="text-neutral-600">${service.description}</span>
                                </td>
                                <td>
                                    <div class="flex items-center gap-2">
                                        <i class="fas fa-hourglass-half text-neutral-400"></i>
                                        <span class="font-medium">${service.duration} min</span>
                                    </div>
                                </td>
                                <td>
                                    <span class="font-bold text-lg">${service.price} MAD</span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${service.active}">
                                            <span class="badge badge-success">
                                                <i class="fas fa-check-circle"></i>
                                                Actif
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-neutral">
                                                <i class="fas fa-times-circle"></i>
                                                Inactif
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="table-actions-group">
                                        <a href="${pageContext.request.contextPath}/admin/services?action=edit&id=${service.serviceId}"
                                           class="btn btn-ghost btn-sm"
                                           title="Modifier">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/services?action=toggle&id=${service.serviceId}"
                                           class="btn btn-ghost btn-sm"
                                           title="${service.active ? 'Désactiver' : 'Activer'}">
                                            <i class="fas ${service.active ? 'fa-toggle-off' : 'fa-toggle-on'}"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/services?action=delete&id=${service.serviceId}"
                                           class="btn btn-ghost btn-sm"
                                           title="Supprimer"
                                           onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce service ? Cette action est irréversible.')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div id="noResultsMessage" style="display: none; padding: 2rem; text-align: center; color: var(--color-neutral-500);">
                    <i class="fas fa-search" style="font-size: 2rem; margin-bottom: 1rem;"></i>
                    <p style="font-size: 1.1rem; font-weight: 500;">Aucun service trouvé</p>
                    <p style="font-size: 0.9rem;">Essayez de modifier vos critères de recherche</p>
                </div>
            </div>

            <c:set var="baseUrl" value="${pageContext.request.contextPath}/admin/services" />
            <c:set var="itemName" value="service(s)" />
            <jsp:include page="../components/pagination.jsp">
                <jsp:param name="currentPage" value="${currentPage}" />
                <jsp:param name="totalPages" value="${totalPages}" />
                <jsp:param name="baseUrl" value="${baseUrl}" />
                <jsp:param name="totalItems" value="${totalServices}" />
                <jsp:param name="itemName" value="${itemName}" />
            </jsp:include>

            <c:if test="${empty services && totalServices == 0}">
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-scissors"></i>
                    </div>
                    <h3 class="empty-state-title">Aucun service trouvé</h3>
                    <p class="empty-state-description">
                        Commencez par ajouter votre premier service au catalogue pour que vos clients puissent réserver des rendez-vous.
                    </p>
                    <a href="${pageContext.request.contextPath}/admin/services?action=add" class="btn btn-primary">
                        <i class="fas fa-plus"></i>
                        Ajouter le premier service
                    </a>
                </div>
            </c:if>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        function updateTableDisplay() {
            const rows = document.querySelectorAll('tbody tr');
            const noResultsMessage = document.getElementById('noResultsMessage');
            const table = document.getElementById('servicesTable');

            let visibleCount = 0;
            rows.forEach(row => {
                if (row.style.display !== 'none') {
                    visibleCount++;
                }
            });

            if (visibleCount === 0) {
                table.style.display = 'none';
                noResultsMessage.style.display = 'block';
            } else {
                table.style.display = 'table';
                noResultsMessage.style.display = 'none';
            }
        }

        document.getElementById('searchInput').addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const rows = document.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });

            updateTableDisplay();
        });

        document.getElementById('statusFilter').addEventListener('change', function(e) {
            const filterValue = e.target.value;
            const rows = document.querySelectorAll('tbody tr');

            rows.forEach(row => {
                const status = row.getAttribute('data-status');
                if (filterValue === 'all') {
                    row.style.display = '';
                } else {
                    row.style.display = status === filterValue ? '' : 'none';
                }
            });

            updateTableDisplay();
        });
    </script>
</body>
</html>
