<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
    Pagination Component

    Required parameters:
    - currentPage: Current page number
    - totalPages: Total number of pages
    - baseUrl: Base URL for pagination links
    - totalItems: Total number of items (optional)
    - itemName: Name of items (e.g., "coiffeurs", "services", "clients") (optional)
--%>

<c:if test="${totalPages > 1}">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-top: var(--space-6); padding: var(--space-4); background: white; border-radius: var(--radius-lg); border: 1px solid var(--color-neutral-200);">
        <div style="color: var(--color-neutral-700); font-size: var(--text-sm);">
            Page <strong>${currentPage}</strong> sur <strong>${totalPages}</strong>
            <c:if test="${not empty totalItems && not empty itemName}">
                <span style="margin-left: var(--space-3); color: var(--color-neutral-500);">
                    (${totalItems} ${itemName} au total)
                </span>
            </c:if>
        </div>
        <div style="display: flex; gap: var(--space-2);">
            <!-- First Page -->
            <c:if test="${currentPage > 1}">
                <a href="${baseUrl}?page=1"
                   class="btn btn-secondary btn-sm"
                   title="Première page">
                    <i class="fas fa-angle-double-left"></i>
                </a>
            </c:if>

            <!-- Previous Page -->
            <c:if test="${currentPage > 1}">
                <a href="${baseUrl}?page=${currentPage - 1}"
                   class="btn btn-secondary btn-sm"
                   title="Page précédente">
                    <i class="fas fa-angle-left"></i>
                    Précédent
                </a>
            </c:if>

            <!-- Page Numbers -->
            <c:forEach var="i" begin="${currentPage - 2 > 0 ? currentPage - 2 : 1}"
                       end="${currentPage + 2 < totalPages ? currentPage + 2 : totalPages}">
                <a href="${baseUrl}?page=${i}"
                   class="btn ${i == currentPage ? 'btn-primary' : 'btn-secondary'} btn-sm"
                   style="min-width: 40px;">
                    ${i}
                </a>
            </c:forEach>

            <!-- Next Page -->
            <c:if test="${currentPage < totalPages}">
                <a href="${baseUrl}?page=${currentPage + 1}"
                   class="btn btn-secondary btn-sm"
                   title="Page suivante">
                    Suivant
                    <i class="fas fa-angle-right"></i>
                </a>
            </c:if>

            <!-- Last Page -->
            <c:if test="${currentPage < totalPages}">
                <a href="${baseUrl}?page=${totalPages}"
                   class="btn btn-secondary btn-sm"
                   title="Dernière page">
                    <i class="fas fa-angle-double-right"></i>
                </a>
            </c:if>
        </div>
    </div>
</c:if>
