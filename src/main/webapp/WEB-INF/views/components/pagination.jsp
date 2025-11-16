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
    <div style="display: flex; justify-content: center; align-items: center; gap: 1rem; margin-top: 2rem; padding: 1rem;">
        <!-- Previous Button -->
        <c:choose>
            <c:when test="${currentPage > 1}">
                <a href="${baseUrl}?page=${currentPage - 1}" class="btn btn-ghost btn-sm">
                    <i class="fas fa-chevron-left"></i> Précédent
                </a>
            </c:when>
            <c:otherwise>
                <button class="btn btn-ghost btn-sm" disabled>
                    <i class="fas fa-chevron-left"></i> Précédent
                </button>
            </c:otherwise>
        </c:choose>

        <!-- Page Info -->
        <span style="font-weight: 500; color: var(--color-neutral-700);">
            Page ${currentPage} sur ${totalPages}
            <c:if test="${not empty totalItems && not empty itemName}">
                (${totalItems} ${itemName})
            </c:if>
        </span>

        <!-- Next Button -->
        <c:choose>
            <c:when test="${currentPage < totalPages}">
                <a href="${baseUrl}?page=${currentPage + 1}" class="btn btn-ghost btn-sm">
                    Suivant <i class="fas fa-chevron-right"></i>
                </a>
            </c:when>
            <c:otherwise>
                <button class="btn btn-ghost btn-sm" disabled>
                    Suivant <i class="fas fa-chevron-right"></i>
                </button>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>
