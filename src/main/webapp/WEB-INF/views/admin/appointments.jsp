<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tous les Rendez-vous - Barbershop Admin</title>

    <!-- Design System CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">

    <!-- Font Awesome 6.4.0 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-page">
    <!-- Admin Navbar -->
    <nav class="navbar role-admin">
        <div class="navbar-container">
            <div class="navbar-brand">
                <div class="navbar-brand-icon"><i class="fas fa-cut"></i></div>
                <div>
                    <div style="font-weight: 700;">GestionCoiffure</div>
                    <div style="font-size: 0.75rem; opacity: 0.9;">Administration</div>
                </div>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-link"><i class="fas fa-home"></i><span>Accueil</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/barbers" class="navbar-link"><i class="fas fa-user-tie"></i><span>Coiffeurs</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/services" class="navbar-link"><i class="fas fa-scissors"></i><span>Services</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/appointments" class="navbar-link active"><i class="fas fa-calendar-check"></i><span>Rendez-vous</span></a></li>
                <li><a href="${pageContext.request.contextPath}/admin/clients" class="navbar-link"><i class="fas fa-users"></i><span>Clients</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <!-- Dashboard Main -->
    <main class="dashboard-main">

            <!-- Success/Error Messages -->
            <c:if test="${param.success == 'cancelled'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    <div class="alert-content">
                        <div class="alert-title">Succès</div>
                        <div class="alert-message">Le rendez-vous a été annulé avec succès.</div>
                    </div>
                </div>
            </c:if>

            <c:if test="${param.error == 'failed'}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    <div class="alert-content">
                        <div class="alert-title">Erreur</div>
                        <div class="alert-message">Une erreur s'est produite lors de l'annulation du rendez-vous.</div>
                    </div>
                </div>
            </c:if>

            <!-- Page Header -->
            <div class="page-header-row">
                <div class="page-header-info">
                    <h1 class="page-title">Tous les Rendez-vous</h1>
                    <p class="page-subtitle">Gérez tous les rendez-vous du salon</p>
                </div>
            </div>

            <!-- Statistics Row -->
            <c:set var="pendingCount" value="0"/>
            <c:set var="confirmedCount" value="0"/>
            <c:set var="completedCount" value="0"/>
            <c:forEach var="apt" items="${appointments}">
                <c:if test="${apt.status == 'pending'}">
                    <c:set var="pendingCount" value="${pendingCount + 1}"/>
                </c:if>
                <c:if test="${apt.status == 'confirmed'}">
                    <c:set var="confirmedCount" value="${confirmedCount + 1}"/>
                </c:if>
                <c:if test="${apt.status == 'completed'}">
                    <c:set var="completedCount" value="${completedCount + 1}"/>
                </c:if>
            </c:forEach>

            <div class="metrics-row">
                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-admin-light);">
                        <i class="fas fa-calendar-alt" style="color: var(--color-admin);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Total</div>
                        <div class="stat-card-value">${appointments.size()}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-warning-light);">
                        <i class="fas fa-hourglass-half" style="color: var(--color-warning);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">En attente</div>
                        <div class="stat-card-value">${pendingCount}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-primary-light);">
                        <i class="fas fa-check-circle" style="color: var(--color-primary);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Confirmés</div>
                        <div class="stat-card-value">${confirmedCount}</div>
                    </div>
                </div>

                <div class="stat-card">
                    <div class="stat-card-icon" style="background: var(--color-success-light);">
                        <i class="fas fa-check-double" style="color: var(--color-success);"></i>
                    </div>
                    <div class="stat-card-content">
                        <div class="stat-card-label">Terminés</div>
                        <div class="stat-card-value">${completedCount}</div>
                    </div>
                </div>
            </div>

            <!-- Appointments Table -->
            <div class="table-actions-bar">
                <h2 class="table-title">Liste des rendez-vous</h2>
                <div class="table-actions-right">
                    <input type="text" id="searchInput" class="search-input" placeholder="Rechercher..." onkeyup="searchTable()">
                    <select id="statusFilter" class="filter-select" onchange="filterByStatus()">
                        <option value="">Tous les statuts</option>
                        <option value="pending">En attente</option>
                        <option value="confirmed">Confirmés</option>
                        <option value="completed">Terminés</option>
                        <option value="refused">Refusés</option>
                        <option value="cancelled">Annulés</option>
                    </select>
                </div>
            </div>

            <c:choose>
                <c:when test="${empty appointments}">
                    <div class="table-container with-actions">
                        <div class="empty-state">
                            <div class="empty-state-icon">
                                <i class="fas fa-calendar-times"></i>
                            </div>
                            <h3 class="empty-state-title">Aucun rendez-vous</h3>
                            <p class="empty-state-message">Il n'y a aucun rendez-vous à afficher pour le moment.</p>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-container with-actions">
                        <table class="table" id="appointmentsTable">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Date</th>
                                        <th>Heure</th>
                                        <th>Client</th>
                                        <th>Coiffeur</th>
                                        <th>Service</th>
                                        <th>Statut</th>
                                        <th class="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="appointment" items="${appointments}">
                                        <tr data-status="${appointment.status}">
                                            <td>#${appointment.appointmentId}</td>
                                            <td>${appointment.date}</td>
                                            <td>${appointment.startTime}</td>
                                            <td>${appointment.clientName}</td>
                                            <td>${appointment.barberName}</td>
                                            <td>${appointment.serviceName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${appointment.status == 'pending'}">
                                                        <span class="badge badge-warning">En attente</span>
                                                    </c:when>
                                                    <c:when test="${appointment.status == 'confirmed'}">
                                                        <span class="badge badge-info">Confirmé</span>
                                                    </c:when>
                                                    <c:when test="${appointment.status == 'completed'}">
                                                        <span class="badge badge-success">Terminé</span>
                                                    </c:when>
                                                    <c:when test="${appointment.status == 'refused'}">
                                                        <span class="badge badge-error">Refusé</span>
                                                    </c:when>
                                                    <c:when test="${appointment.status == 'cancelled'}">
                                                        <span class="badge badge-error">Annulé</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <c:if test="${appointment.status != 'cancelled' && appointment.status != 'completed' && appointment.status != 'refused'}">
                                                    <button onclick="if(confirm('Êtes-vous sûr de vouloir annuler ce rendez-vous?')) location.href='${pageContext.request.contextPath}/admin/appointments?action=cancel&id=${appointment.appointmentId}'"
                                                            class="btn btn-sm btn-danger">
                                                        Annuler
                                                    </button>
                                                </c:if>
                                                <c:if test="${appointment.status == 'cancelled' || appointment.status == 'completed' || appointment.status == 'refused'}">
                                                    <span class="text-muted">-</span>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                        </table>

                        <!-- Pagination -->
                        <div id="pagination" style="display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 2rem; padding: 1rem;">
                            <button id="prevBtn" onclick="changePage(-1)" class="btn btn-ghost btn-sm" disabled>
                                <i class="fas fa-chevron-left"></i> Précédent
                            </button>
                            <span id="pageInfo" style="font-weight: 500; color: var(--color-neutral-700);"></span>
                            <button id="nextBtn" onclick="changePage(1)" class="btn btn-ghost btn-sm">
                                Suivant <i class="fas fa-chevron-right"></i>
                            </button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

    </main>

    <!-- JavaScript -->
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Pagination variables
        let currentPage = 1;
        const rowsPerPage = 10;
        let allRows = [];

        // Initialize pagination on page load
        document.addEventListener('DOMContentLoaded', function() {
            const table = document.getElementById('appointmentsTable');
            if (table) {
                const tbody = table.getElementsByTagName('tbody')[0];
                allRows = Array.from(tbody.getElementsByTagName('tr'));
                showPage(1);
            }
        });

        // Show specific page
        function showPage(page) {
            currentPage = page;
            const start = (page - 1) * rowsPerPage;
            const end = start + rowsPerPage;

            // Hide all rows first
            allRows.forEach(row => row.style.display = 'none');

            // Show only rows for current page
            const visibleRows = allRows.filter(row => row.style.display !== 'none' || row.getAttribute('data-hidden') !== 'true');
            for (let i = start; i < end && i < visibleRows.length; i++) {
                visibleRows[i].style.display = '';
            }

            updatePaginationUI(visibleRows.length);
        }

        // Change page
        function changePage(direction) {
            const visibleRows = allRows.filter(row => row.getAttribute('data-hidden') !== 'true');
            const totalPages = Math.ceil(visibleRows.length / rowsPerPage);

            const newPage = currentPage + direction;
            if (newPage >= 1 && newPage <= totalPages) {
                showPage(newPage);
            }
        }

        // Update pagination UI
        function updatePaginationUI(totalVisibleRows) {
            const totalPages = Math.ceil(totalVisibleRows / rowsPerPage);

            document.getElementById('prevBtn').disabled = currentPage === 1;
            document.getElementById('nextBtn').disabled = currentPage === totalPages || totalPages === 0;
            document.getElementById('pageInfo').textContent = totalPages > 0 ?
                'Page ' + currentPage + ' sur ' + totalPages + ' (' + totalVisibleRows + ' rendez-vous)' :
                'Aucun rendez-vous';
        }

        // Search functionality
        function searchTable() {
            const input = document.getElementById('searchInput');
            const filter = input.value.toLowerCase();
            const table = document.getElementById('appointmentsTable');
            const rows = table.getElementsByTagName('tr');

            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const cells = row.getElementsByTagName('td');
                let found = false;

                for (let j = 0; j < cells.length; j++) {
                    const cell = cells[j];
                    if (cell) {
                        const textValue = cell.textContent || cell.innerText;
                        if (textValue.toLowerCase().indexOf(filter) > -1) {
                            found = true;
                            break;
                        }
                    }
                }

                if (found) {
                    row.removeAttribute('data-hidden');
                } else {
                    row.setAttribute('data-hidden', 'true');
                }
            }

            showPage(1);
        }

        // Filter by status
        function filterByStatus() {
            const select = document.getElementById('statusFilter');
            const filterValue = select.value.toLowerCase();
            const table = document.getElementById('appointmentsTable');
            const rows = table.getElementsByTagName('tr');

            for (let i = 1; i < rows.length; i++) {
                const row = rows[i];
                const status = row.getAttribute('data-status');

                if (filterValue === '' || status === filterValue) {
                    row.removeAttribute('data-hidden');
                } else {
                    row.setAttribute('data-hidden', 'true');
                }
            }

            showPage(1);
        }

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
