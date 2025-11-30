<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réserver un Rendez-vous - GestionCoiffure</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        .price-summary {
            background: var(--color-neutral-50);
            border: 1px solid var(--color-neutral-200);
            border-radius: var(--radius-lg);
            padding: var(--space-5);
            margin-top: var(--space-6);
        }

        .price-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: var(--space-3) 0;
            border-bottom: 1px solid var(--color-neutral-200);
        }

        .price-row:last-child {
            border-bottom: none;
            padding-top: var(--space-4);
            margin-top: var(--space-2);
            border-top: 2px solid var(--color-neutral-300);
            font-weight: var(--font-weight-bold);
            font-size: var(--text-lg);
        }

        .price-label {
            color: var(--color-neutral-700);
            font-size: var(--text-sm);
        }

        .price-value {
            color: var(--color-neutral-900);
            font-weight: var(--font-weight-semibold);
        }

        .discount-value {
            color: var(--color-success);
        }
    </style>
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
                <li><a href="${pageContext.request.contextPath}/client/book-appointment" class="navbar-link active"><i class="fas fa-calendar-plus"></i><span>Réserver</span></a></li>
                <li><a href="${pageContext.request.contextPath}/client/offers" class="navbar-link"><i class="fas fa-gift"></i><span>Offres</span></a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="navbar-link navbar-logout"><i class="fas fa-sign-out-alt"></i><span>Déconnexion</span></a></li>
            </ul>
        </div>
    </nav>

    <main class="dashboard-main">
        <div class="container-narrow">
            <div class="page-header-row">
                <div class="page-header-info">
                    <h1 class="page-title">Réserver un Rendez-vous</h1>
                    <p class="page-subtitle">Sélectionnez votre service, coiffeur et créneau horaire</p>
                </div>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    <div class="alert-content">
                        <div class="alert-title">Erreur</div>
                        <div class="alert-message">${error}</div>
                    </div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/client/book-appointment" method="post" class="form-section">
                <div class="form-group">
                    <label for="serviceId" class="form-label form-label-required">Choisir un Service</label>
                    <select name="serviceId" id="serviceId" class="form-select" required onchange="updatePrice()">
                        <option value="">Sélectionnez un service</option>
                        <c:forEach var="service" items="${services}">
                            <option value="${service.serviceId}" data-price="${service.price}" data-duration="${service.duration}">
                                ${service.name} - ${service.price} MAD (${service.duration} min)
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="barberId" class="form-label form-label-required">Choisir un Coiffeur</label>
                    <select name="barberId" id="barberId" class="form-select" required onchange="updateAvailability()">
                        <option value="">Sélectionnez un coiffeur</option>
                        <c:forEach var="barber" items="${barbers}">
                            <option value="${barber.barberId}">${barber.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="date" class="form-label form-label-required">Date</label>
                    <input type="date" id="date" name="date" class="form-input" required onchange="updateAvailability()">
                </div>

                <div id="availabilityInfo" style="display: none; margin-bottom: var(--space-5);">
                    <div style="background: var(--color-info-light); border: 1px solid var(--color-info); border-radius: var(--radius-lg); padding: var(--space-4);">
                        <div style="display: flex; align-items: center; gap: var(--space-2); margin-bottom: var(--space-2);">
                            <i class="fas fa-clock" style="color: var(--color-info);"></i>
                            <strong style="color: var(--color-info);">Horaires disponibles</strong>
                        </div>
                        <p id="availabilityText" style="margin: 0; color: var(--color-neutral-700); font-size: var(--text-sm);"></p>
                    </div>
                </div>

                <div class="form-group">
                    <label for="startTime" class="form-label form-label-required">Heure de début</label>
                    <input type="time" id="startTime" name="startTime" class="form-input" required>
                    <p class="form-help">Choisissez une heure pendant les horaires d'ouverture du coiffeur</p>
                </div>

                <c:if test="${not empty redeemedOffers}">
                    <div class="form-group">
                        <label for="redemptionId" class="form-label">
                            <i class="fas fa-gift" style="color: var(--color-success);"></i>
                            Utiliser une Offre
                        </label>
                        <select name="redemptionId" id="redemptionId" class="form-select" onchange="updatePrice()">
                            <option value="">Aucune offre</option>
                            <c:forEach var="redemption" items="${redeemedOffers}">
                                <option value="${redemption.redemptionId}" data-discount="10">
                                    ${redemption.offerTitle} (-10%)
                                </option>
                            </c:forEach>
                        </select>
                        <p class="form-help">Vous avez <strong>${redeemedOffers.size()}</strong> offre(s) disponible(s)</p>
                    </div>
                </c:if>

                <c:if test="${empty redeemedOffers}">
                    <div class="alert alert-info" style="margin-bottom: var(--space-5);">
                        <i class="fas fa-info-circle"></i>
                        <div class="alert-content">
                            <div class="alert-message">
                                Vous n'avez pas d'offres disponibles.
                                <a href="${pageContext.request.contextPath}/client/offers" style="text-decoration: underline; font-weight: var(--font-weight-semibold);">
                                    Récupérez une offre
                                </a> pour bénéficier de réductions.
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="price-summary" id="priceSummary" style="display: none;">
                    <h3 style="margin: 0 0 var(--space-4) 0; font-size: var(--text-lg); font-weight: var(--font-weight-semibold);">
                        Récapitulatif du Prix
                    </h3>

                    <div class="price-row">
                        <span class="price-label">Prix du service</span>
                        <span class="price-value" id="basePrice">0 MAD</span>
                    </div>

                    <div class="price-row" id="discountRow" style="display: none;">
                        <span class="price-label">
                            <i class="fas fa-gift" style="color: var(--color-success);"></i>
                            Réduction offre (<span id="discountPercent">0</span>%)
                        </span>
                        <span class="price-value discount-value" id="discountAmount">-0 MAD</span>
                    </div>

                    <div class="price-row">
                        <span class="price-label">Total à payer</span>
                        <span class="price-value" id="finalPrice" style="color: var(--color-client);">0 MAD</span>
                    </div>
                </div>

                <div class="form-buttons">
                    <a href="${pageContext.request.contextPath}/client/dashboard" class="btn btn-secondary">Annuler</a>
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-check"></i> Confirmer la Réservation
                    </button>
                </div>
            </form>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script>
        // Set minimum date to today
        document.getElementById('date').setAttribute('min', new Date().toISOString().split('T')[0]);

        // Barber availability data
        const barberAvailability = {
            <c:forEach var="entry" items="${barberAvailability}" varStatus="status">
                ${entry.key}: [
                    <c:forEach var="avail" items="${entry.value}" varStatus="availStatus">
                        {
                            dayOfWeek: '${avail.dayOfWeek}',
                            startTime: '${avail.startTime}',
                            endTime: '${avail.endTime}'
                        }<c:if test="${!availStatus.last}">,</c:if>
                    </c:forEach>
                ]<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        };

        // Day of week mapping
        const dayOfWeekMap = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

        // Update availability display when barber or date changes
        function updateAvailability() {
            const barberSelect = document.getElementById('barberId');
            const dateInput = document.getElementById('date');
            const availabilityInfo = document.getElementById('availabilityInfo');
            const availabilityText = document.getElementById('availabilityText');

            const barberId = barberSelect.value;
            const selectedDate = dateInput.value;

            // Hide if barber or date not selected
            if (!barberId || !selectedDate) {
                availabilityInfo.style.display = 'none';
                return;
            }

            // Get day of week from date
            const date = new Date(selectedDate + 'T00:00:00');
            const dayOfWeek = dayOfWeekMap[date.getDay()];

            // Find availability for this barber on this day
            const availability = barberAvailability[barberId];
            if (!availability) {
                availabilityInfo.style.display = 'none';
                return;
            }

            const dayAvailability = availability.find(a => a.dayOfWeek === dayOfWeek);

            if (dayAvailability) {
                availabilityText.innerHTML = '<i class="fas fa-check-circle"></i> Le coiffeur est disponible de <strong>' +
                    dayAvailability.startTime + '</strong> à <strong>' +
                    dayAvailability.endTime + '</strong> ce jour-là.';
                availabilityInfo.style.display = 'block';
            } else {
                availabilityText.innerHTML = '<i class="fas fa-times-circle"></i> Le coiffeur n\'est pas disponible ce jour-là. Veuillez choisir une autre date.';
                availabilityInfo.style.display = 'block';
            }
        }

        function updatePrice() {
            const serviceSelect = document.getElementById('serviceId');
            const redemptionSelect = document.getElementById('redemptionId');
            const priceSummary = document.getElementById('priceSummary');
            const basePriceEl = document.getElementById('basePrice');
            const discountRow = document.getElementById('discountRow');
            const discountPercentEl = document.getElementById('discountPercent');
            const discountAmountEl = document.getElementById('discountAmount');
            const finalPriceEl = document.getElementById('finalPrice');

            const selectedOption = serviceSelect.options[serviceSelect.selectedIndex];
            if (!selectedOption || !selectedOption.value) {
                priceSummary.style.display = 'none';
                return;
            }

            const basePrice = parseFloat(selectedOption.getAttribute('data-price'));
            let finalPrice = basePrice;
            let discountPercent = 0;

            if (redemptionSelect) {
                const selectedRedemption = redemptionSelect.options[redemptionSelect.selectedIndex];
                if (selectedRedemption && selectedRedemption.value) {
                    discountPercent = parseFloat(selectedRedemption.getAttribute('data-discount'));
                }
            }

            const discountAmount = (basePrice * discountPercent) / 100;
            finalPrice = basePrice - discountAmount;

            basePriceEl.textContent = basePrice.toFixed(2) + ' MAD';

            if (discountPercent > 0) {
                discountRow.style.display = 'flex';
                discountPercentEl.textContent = discountPercent;
                discountAmountEl.textContent = '-' + discountAmount.toFixed(2) + ' MAD';
            } else {
                discountRow.style.display = 'none';
            }

            finalPriceEl.textContent = finalPrice.toFixed(2) + ' MAD';
            priceSummary.style.display = 'block';
        }

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
