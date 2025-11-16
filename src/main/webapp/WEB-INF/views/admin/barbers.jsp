<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Coiffeurs - Admin Dashboard</title>

    <!-- Design System CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Navigation Bar -->
    <nav class="navbar role-admin">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon">
                    <i class="fas fa-cut"></i>
                </div>
                <span>Gestion Coiffure</span>
            </div>

            <ul class="navbar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link">
                        <i class="fas fa-arrow-left"></i>
                        <span>Tableau de bord</span>
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/barbers" class="navbar-link active">
                        <i class="fas fa-user-tie"></i>
                        <span>Coiffeurs</span>
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

    <!-- Main Content -->
    <main class="dashboard-main">
        <!-- Page Header -->
        <div class="page-header-row">
            <div class="page-header-info">
                <h1 class="page-title">Gestion des Coiffeurs</h1>
                <p class="page-subtitle">Gérez les profils et disponibilités des coiffeurs</p>
            </div>
            <div class="page-actions">
                <a href="${pageContext.request.contextPath}/admin/barbers?action=add" class="btn btn-primary">
                    <i class="fas fa-plus"></i>
                    <span>Ajouter un Coiffeur</span>
                </a>
            </div>
        </div>

        <!-- Success/Error Alerts -->
        <c:if test="${param.success == 'created'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Coiffeur créé avec succès!</div>
                    Le nouveau coiffeur a été ajouté au système.
                </div>
            </div>
        </c:if>

        <c:if test="${param.success == 'updated'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Mise à jour réussie</div>
                    Les informations du coiffeur ont été mises à jour.
                </div>
            </div>
        </c:if>

        <c:if test="${param.success == 'deleted'}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Coiffeur supprimé</div>
                    Le coiffeur a été supprimé du système.
                </div>
            </div>
        </c:if>

        <c:if test="${param.error == 'deleteFailed'}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle alert-icon"></i>
                <div class="alert-content">
                    <div class="alert-title">Erreur de suppression</div>
                    Une erreur est survenue lors de la suppression du coiffeur.
                </div>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-triangle alert-icon"></i>
                <div class="alert-content">
                    ${error}
                </div>
            </div>
        </c:if>

        <!-- Statistics Cards -->
        <c:if test="${not empty barbers}">
            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-primary-50); color: var(--color-primary);">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Total Coiffeurs</div>
                        <div class="stat-card-value">${barbers.size()}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light); color: var(--color-success);">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Coiffeurs Actifs</div>
                        <div class="stat-card-value">
                            <c:set var="activeCount" value="0" />
                            <c:forEach var="barber" items="${barbers}">
                                <c:if test="${barber.status == 'active'}">
                                    <c:set var="activeCount" value="${activeCount + 1}" />
                                </c:if>
                            </c:forEach>
                            ${activeCount}
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-warning-light); color: var(--color-warning);">
                        <i class="fas fa-pause-circle"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Coiffeurs Inactifs</div>
                        <div class="stat-card-value">
                            <c:set var="inactiveCount" value="0" />
                            <c:forEach var="barber" items="${barbers}">
                                <c:if test="${barber.status != 'active'}">
                                    <c:set var="inactiveCount" value="${inactiveCount + 1}" />
                                </c:if>
                            </c:forEach>
                            ${inactiveCount}
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Barbers Table -->
        <c:choose>
            <c:when test="${empty barbers}">
                <div class="card">
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <i class="fas fa-user-slash"></i>
                        </div>
                        <h3 class="empty-state-title">Aucun coiffeur enregistré</h3>
                        <p class="empty-state-description">
                            Commencez par ajouter votre premier coiffeur pour gérer les réservations et les services.
                        </p>
                        <a href="${pageContext.request.contextPath}/admin/barbers?action=add" class="btn btn-primary">
                            <i class="fas fa-plus"></i>
                            Ajouter le premier coiffeur
                        </a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Table Actions Bar -->
                <div class="table-actions-bar">
                    <div class="table-actions-left">
                        <div class="search-input">
                            <i class="fas fa-search"></i>
                            <input type="text" class="form-input" placeholder="Rechercher un coiffeur..." id="searchInput">
                        </div>
                    </div>
                    <div class="table-actions-right">
                        <select class="form-select filter-select" id="statusFilter">
                            <option value="">Tous les statuts</option>
                            <option value="active">Actifs</option>
                            <option value="inactive">Inactifs</option>
                        </select>
                    </div>
                </div>

                <!-- Table Container -->
                <div class="table-container with-actions">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nom</th>
                                <th>Email</th>
                                <th>Téléphone</th>
                                <th>Statut</th>
                                <th>Date création</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="barbersTableBody">
                            <c:forEach var="barber" items="${barbers}">
                                <tr data-status="${barber.status}">
                                    <td>
                                        <span class="font-medium text-neutral-900">#${barber.barberId}</span>
                                    </td>
                                    <td>
                                        <div class="flex items-center gap-3">
                                            <div style="width: 32px; height: 32px; border-radius: var(--radius-lg); background: var(--color-primary-50); color: var(--color-primary); display: flex; align-items: center; justify-content: center; font-weight: var(--font-weight-semibold); font-size: var(--text-sm);">
                                                ${barber.name.substring(0, 1).toUpperCase()}
                                            </div>
                                            <span class="font-medium text-neutral-900">${barber.name}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="text-neutral-700">${barber.email}</span>
                                    </td>
                                    <td>
                                        <span class="text-neutral-700">${barber.phone}</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${barber.status == 'active'}">
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
                                    </td>
                                    <td>
                                        <span class="text-neutral-600 text-sm">${barber.createdAt}</span>
                                    </td>
                                    <td>
                                        <div class="table-actions-group">
                                            <a href="${pageContext.request.contextPath}/admin/barbers?action=edit&id=${barber.barberId}"
                                               class="btn btn-ghost btn-sm"
                                               title="Modifier">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/barbers?action=toggleStatus&id=${barber.barberId}"
                                               class="btn btn-ghost btn-sm"
                                               title="${barber.status == 'active' ? 'Désactiver' : 'Activer'}">
                                                <i class="fas fa-toggle-${barber.status == 'active' ? 'on' : 'off'}"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/barbers?action=delete&id=${barber.barberId}"
                                               class="btn btn-ghost btn-sm"
                                               title="Supprimer"
                                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce coiffeur?\n\nCette action est irréversible.')">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </main>

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Search functionality
        const searchInput = document.getElementById('searchInput');
        const statusFilter = document.getElementById('statusFilter');
        const tableBody = document.getElementById('barbersTableBody');

        if (searchInput && tableBody) {
            searchInput.addEventListener('input', filterTable);
        }

        if (statusFilter && tableBody) {
            statusFilter.addEventListener('change', filterTable);
        }

        function filterTable() {
            const searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
            const statusValue = statusFilter ? statusFilter.value : '';
            const rows = tableBody.getElementsByTagName('tr');

            for (let row of rows) {
                const name = row.querySelector('td:nth-child(2)') ? row.querySelector('td:nth-child(2)').textContent.toLowerCase() : '';
                const email = row.querySelector('td:nth-child(3)') ? row.querySelector('td:nth-child(3)').textContent.toLowerCase() : '';
                const status = row.getAttribute('data-status') || '';

                const matchesSearch = name.includes(searchTerm) || email.includes(searchTerm);
                const matchesStatus = statusValue === '' || status === statusValue;

                row.style.display = matchesSearch && matchesStatus ? '' : 'none';
            }
        }
    </script>
</body>
</html>
