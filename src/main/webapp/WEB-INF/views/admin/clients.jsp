<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Clients - GestionCoiffure</title>

    <!-- CSS Files -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="dashboard-page">
        <!-- Navigation Bar -->
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
                        <a href="${pageContext.request.contextPath}/admin/clients" class="navbar-link active">
                            <i class="fas fa-users"></i>
                            <span>Clients</span>
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
                    <h1 class="page-title">Base de données clients</h1>
                    <p class="page-subtitle">Gérez et suivez tous vos clients</p>
                </div>
            </div>

            <!-- Metrics Row -->
            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-primary-50); color: var(--color-primary);">
                        <i class="fas fa-users"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Total Clients</div>
                        <div class="stat-card-value">${clients.size()}</div>
                        <div class="stat-card-change">
                            <i class="fas fa-user-plus"></i>
                            <span>Clients enregistrés</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-warning-light); color: var(--color-warning-dark);">
                        <i class="fas fa-crown"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Clients Fidèles</div>
                        <div class="stat-card-value">
                            <c:set var="fideleCount" value="0"/>
                            <c:forEach var="client" items="${clients}">
                                <c:if test="${client.loyaltyStatus == 'fidele'}">
                                    <c:set var="fideleCount" value="${fideleCount + 1}"/>
                                </c:if>
                            </c:forEach>
                            ${fideleCount}
                        </div>
                        <div class="stat-card-change">
                            <i class="fas fa-star"></i>
                            <span>Statut fidélité</span>
                        </div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light); color: var(--color-success);">
                        <i class="fas fa-coins"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Points Totaux</div>
                        <div class="stat-card-value">
                            <c:set var="totalPoints" value="0"/>
                            <c:forEach var="client" items="${clients}">
                                <c:set var="totalPoints" value="${totalPoints + client.pointsBalance}"/>
                            </c:forEach>
                            ${totalPoints}
                        </div>
                        <div class="stat-card-change">
                            <i class="fas fa-gift"></i>
                            <span>Points de fidélité</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Table Section -->
            <div>
                <!-- Table Actions Bar -->
                <div class="table-actions-bar">
                    <div class="table-actions-left">
                        <div class="search-input">
                            <i class="fas fa-search"></i>
                            <input type="text"
                                   id="searchInput"
                                   class="form-input"
                                   placeholder="Rechercher un client...">
                        </div>
                    </div>
                    <div class="table-actions-right">
                        <select class="form-select filter-select" id="statusFilter">
                            <option value="">Tous les statuts</option>
                            <option value="fidele">Clients Fidèles</option>
                            <option value="standard">Standard</option>
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
                                <th>Points</th>
                                <th>Statut</th>
                                <th>RDV Complétés</th>
                                <th>Date création</th>
                            </tr>
                        </thead>
                        <tbody id="clientsTableBody">
                            <c:choose>
                                <c:when test="${not empty clients}">
                                    <c:forEach var="client" items="${clients}">
                                        <tr data-status="${client.loyaltyStatus}">
                                            <td>
                                                <span class="font-medium">#${client.clientId}</span>
                                            </td>
                                            <td>
                                                <div style="display: flex; align-items: center; gap: var(--space-3);">
                                                    <div style="width: 32px; height: 32px; border-radius: var(--radius-full); background: linear-gradient(135deg, var(--color-primary), var(--color-secondary)); display: flex; align-items: center; justify-content: center; color: white; font-weight: var(--font-weight-bold); font-size: var(--text-sm);">
                                                        ${client.name.substring(0, 1).toUpperCase()}
                                                    </div>
                                                    <span class="font-medium" style="color: var(--color-neutral-900);">${client.name}</span>
                                                </div>
                                            </td>
                                            <td>
                                                <div style="display: flex; align-items: center; gap: var(--space-2);">
                                                    <i class="fas fa-envelope" style="color: var(--color-neutral-400); font-size: var(--text-xs);"></i>
                                                    ${client.email}
                                                </div>
                                            </td>
                                            <td>
                                                <div style="display: flex; align-items: center; gap: var(--space-2);">
                                                    <i class="fas fa-phone" style="color: var(--color-neutral-400); font-size: var(--text-xs);"></i>
                                                    ${client.phone}
                                                </div>
                                            </td>
                                            <td>
                                                <span class="badge badge-primary">
                                                    <i class="fas fa-coins"></i>
                                                    ${client.pointsBalance} pts
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${client.loyaltyStatus == 'fidele'}">
                                                        <span class="badge badge-warning">
                                                            <i class="fas fa-crown"></i>
                                                            Fidèle
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge badge-neutral">
                                                            <i class="fas fa-user"></i>
                                                            Standard
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="font-semibold" style="color: var(--color-neutral-900);">
                                                    ${appointmentCounts[client.clientId]}
                                                </span>
                                                <span style="color: var(--color-neutral-500); font-size: var(--text-xs);"> RDV</span>
                                            </td>
                                            <td>
                                                <div style="display: flex; align-items: center; gap: var(--space-2);">
                                                    <i class="fas fa-calendar" style="color: var(--color-neutral-400); font-size: var(--text-xs);"></i>
                                                    ${client.createdAt}
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="8">
                                            <div class="empty-state" style="padding: var(--space-12) var(--space-6);">
                                                <div class="empty-state-icon">
                                                    <i class="fas fa-users"></i>
                                                </div>
                                                <div class="empty-state-title">Aucun client</div>
                                                <div class="empty-state-description">
                                                    La liste des clients est vide pour le moment.
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <c:set var="baseUrl" value="${pageContext.request.contextPath}/admin/clients" />
                <c:set var="itemName" value="client(s)" />
                <jsp:include page="../components/pagination.jsp">
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="baseUrl" value="${baseUrl}" />
                    <jsp:param name="totalItems" value="${totalClients}" />
                    <jsp:param name="itemName" value="${itemName}" />
                </jsp:include>
            </div>
        </main>
    </div>

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Search functionality
        document.getElementById('searchInput').addEventListener('input', function(e) {
            const searchTerm = e.target.value.toLowerCase();
            const rows = document.querySelectorAll('#clientsTableBody tr');

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                if (text.includes(searchTerm)) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });

        // Status filter functionality
        document.getElementById('statusFilter').addEventListener('change', function(e) {
            const filterValue = e.target.value;
            const rows = document.querySelectorAll('#clientsTableBody tr');

            rows.forEach(row => {
                const status = row.dataset.status;
                if (filterValue === '' || status === filterValue) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html>
