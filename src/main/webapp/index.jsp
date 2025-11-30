<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GestionCoiffure - Système de Gestion Professionnel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/design-system.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
         
        .hero-section {
            min-height: 100vh;
            display: flex;
            align-items: center;
            position: relative;
            overflow: hidden;
        }

        .hero-background {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            z-index: 0;
            opacity: 0.03;
        }

        .hero-background-circle {
            position: absolute;
            border-radius: 50%;
            background: var(--color-neutral-900);
        }

        .circle-1 {
            width: 600px;
            height: 600px;
            top: -200px;
            right: -100px;
        }

        .circle-2 {
            width: 400px;
            height: 400px;
            bottom: -100px;
            left: -100px;
        }

        .hero-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: var(--space-16);
            align-items: center;
            position: relative;
            z-index: 1;
        }

        .hero-content h1 {
            font-size: 3.5rem;
            font-weight: 800;
            line-height: 1.1;
            margin-bottom: var(--space-6);
            color: var(--color-neutral-900);
            letter-spacing: -0.03em;
        }

        .hero-content p {
            font-size: var(--text-lg);
            color: var(--color-neutral-600);
            line-height: var(--line-height-relaxed);
            margin-bottom: var(--space-8);
        }

        .hero-buttons {
            display: flex;
            gap: var(--space-4);
            flex-wrap: wrap;
        }

        .hero-buttons .btn {
            padding: var(--space-4) var(--space-8);
            font-size: var(--text-base);
        }

        .hero-illustration {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .salon-illustration {
            width: 100%;
            max-width: 500px;
            height: auto;
        }

        .features-section {
            padding: var(--space-20) 0;
            background: white;
        }

        .section-header {
            text-align: center;
            max-width: 600px;
            margin: 0 auto var(--space-16);
        }

        .section-header h2 {
            font-size: var(--text-4xl);
            font-weight: 800;
            margin-bottom: var(--space-4);
            color: var(--color-neutral-900);
        }

        .section-header p {
            font-size: var(--text-lg);
            color: var(--color-neutral-600);
        }

        .features-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: var(--space-8);
        }

        .feature-item {
            text-align: center;
            padding: var(--space-8);
            border-radius: var(--radius-xl);
            transition: all var(--transition-base);
        }

        .feature-item:hover {
            background: var(--color-neutral-50);
            transform: translateY(-4px);
        }

        .feature-icon-wrapper {
            width: 80px;
            height: 80px;
            margin: 0 auto var(--space-6);
            border-radius: var(--radius-xl);
            background: var(--color-neutral-100);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: var(--text-4xl);
            transition: all var(--transition-base);
        }

        .feature-item:hover .feature-icon-wrapper {
            background: var(--color-neutral-900);
            transform: rotate(-5deg) scale(1.1);
        }

        .feature-item:hover .feature-icon-wrapper i {
            color: white;
        }

        .feature-icon-wrapper i {
            transition: color var(--transition-base);
        }

        .feature-item h3 {
            font-size: var(--text-xl);
            font-weight: 700;
            margin-bottom: var(--space-3);
            color: var(--color-neutral-900);
        }

        .feature-item p {
            font-size: var(--text-base);
            color: var(--color-neutral-600);
            line-height: var(--line-height-relaxed);
        }

        .demo-section {
            padding: var(--space-20) 0;
            background: var(--color-neutral-50);
        }

        .demo-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: var(--space-6);
        }

        .demo-card {
            background: white;
            border: 1px solid var(--color-neutral-200);
            border-radius: var(--radius-xl);
            padding: var(--space-6);
            transition: all var(--transition-base);
        }

        .demo-card:hover {
            transform: translateY(-4px);
            box-shadow: var(--shadow-lg);
        }

        .demo-card-header {
            display: flex;
            align-items: center;
            gap: var(--space-3);
            margin-bottom: var(--space-4);
            padding-bottom: var(--space-4);
            border-bottom: 1px solid var(--color-neutral-200);
        }

        .demo-card-icon {
            width: 48px;
            height: 48px;
            border-radius: var(--radius-lg);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: var(--text-2xl);
            color: white;
        }

        .demo-card-icon.admin {
            background: linear-gradient(135deg, var(--color-admin), var(--color-admin-light));
        }

        .demo-card-icon.barber {
            background: linear-gradient(135deg, var(--color-barber), var(--color-barber-light));
        }

        .demo-card-icon.client {
            background: linear-gradient(135deg, var(--color-client), var(--color-client-light));
        }

        .demo-card-title {
            font-size: var(--text-lg);
            font-weight: 700;
            color: var(--color-neutral-900);
            margin-bottom: var(--space-1);
        }

        .demo-card-subtitle {
            font-size: var(--text-sm);
            color: var(--color-neutral-500);
        }

        .demo-credentials {
            display: flex;
            flex-direction: column;
            gap: var(--space-3);
        }

        .demo-credential-item {
            display: flex;
            align-items: center;
            gap: var(--space-2);
            padding: var(--space-2) var(--space-3);
            background: var(--color-neutral-50);
            border-radius: var(--radius-md);
            font-size: var(--text-sm);
            font-family: var(--font-family-mono);
            color: var(--color-neutral-700);
        }

        .demo-credential-item i {
            color: var(--color-neutral-400);
            width: 16px;
        }

        .cta-section {
            padding: var(--space-20) 0;
            background: var(--color-neutral-900);
            color: white;
            text-align: center;
        }

        .cta-section h2 {
            font-size: var(--text-4xl);
            font-weight: 800;
            margin-bottom: var(--space-4);
            color: white;
        }

        .cta-section p {
            font-size: var(--text-lg);
            margin-bottom: var(--space-8);
            color: rgba(255, 255, 255, 0.8);
        }

        .cta-buttons {
            display: flex;
            gap: var(--space-4);
            justify-content: center;
            flex-wrap: wrap;
        }

        .cta-buttons .btn {
            padding: var(--space-4) var(--space-8);
            font-size: var(--text-base);
        }

        .btn-white {
            background: white;
            color: var(--color-neutral-900);
        }

        .btn-white:hover {
            background: var(--color-neutral-100);
            transform: translateY(-2px);
            box-shadow: var(--shadow-lg);
        }

        .btn-outline-white {
            background: transparent;
            color: white;
            border: 2px solid white;
        }

        .btn-outline-white:hover {
            background: white;
            color: var(--color-neutral-900);
        }

        .footer {
            padding: var(--space-12) 0;
            background: white;
            border-top: 1px solid var(--color-neutral-200);
            text-align: center;
        }

        .footer-content {
            max-width: 600px;
            margin: 0 auto;
        }

        .footer-brand {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: var(--space-2);
            font-size: var(--text-xl);
            font-weight: 700;
            margin-bottom: var(--space-4);
            color: var(--color-neutral-900);
        }

        .footer-brand-icon {
            font-size: var(--text-2xl);
        }

        .footer-text {
            font-size: var(--text-sm);
            color: var(--color-neutral-600);
            margin-bottom: var(--space-2);
        }

        .footer-tech {
            font-size: var(--text-xs);
            color: var(--color-neutral-500);
        }

        @media (max-width: 1024px) {
            .hero-grid {
                grid-template-columns: 1fr;
                gap: var(--space-12);
            }

            .hero-content h1 {
                font-size: 2.5rem;
            }

            .hero-illustration {
                order: -1;
            }

            .features-grid {
                grid-template-columns: 1fr;
            }

            .demo-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 768px) {
            .hero-content h1 {
                font-size: 2rem;
            }

            .hero-buttons,
            .cta-buttons {
                flex-direction: column;
            }

            .hero-buttons .btn,
            .cta-buttons .btn {
                width: 100%;
            }

            .section-header h2 {
                font-size: var(--text-3xl);
            }
        }
    </style>
</head>
<body>
    
    <section class="hero-section">
        <div class="hero-background">
            <div class="hero-background-circle circle-1"></div>
            <div class="hero-background-circle circle-2"></div>
        </div>
        <div class="container">
            <div class="hero-grid">
                <div class="hero-content">
                    <h1>Gestion de Salon<br>Simple & Efficace</h1>
                    <p>
                        Une solution complète pour gérer vos rendez-vous, vos coiffeurs,
                        vos services et fidéliser vos clients. Moderne, intuitive et puissante.
                    </p>
                    <div class="hero-buttons">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">
                            <i class="fas fa-sign-in-alt"></i>
                            Se Connecter
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary btn-lg">
                            <i class="fas fa-user-plus"></i>
                            Créer un Compte
                        </a>
                    </div>
                </div>
                <div class="hero-illustration">
                    
                    <svg class="salon-illustration" viewBox="0 0 500 500" fill="none" xmlns="http://www.w3.org/2000/svg">
                        
                        <circle cx="250" cy="250" r="200" fill="#f5f5f5"/>

                        <rect x="180" y="220" width="140" height="160" rx="20" fill="#262626"/>
                        <rect x="190" y="230" width="120" height="100" rx="15" fill="#404040"/>
                        <rect x="200" y="340" width="20" height="60" fill="#171717"/>
                        <rect x="280" y="340" width="20" height="60" fill="#171717"/>
                        <circle cx="190" cy="410" r="15" fill="#525252"/>
                        <circle cx="310" cy="410" r="15" fill="#525252"/>

                        <g transform="translate(100, 150)">
                            <circle cx="0" cy="0" r="12" fill="none" stroke="#2563eb" stroke-width="3"/>
                            <circle cx="25" cy="25" r="12" fill="none" stroke="#2563eb" stroke-width="3"/>
                            <line x1="8" y1="8" x2="17" y2="17" stroke="#2563eb" stroke-width="3"/>
                            <line x1="8" y1="-8" x2="-5" y2="-25" stroke="#2563eb" stroke-width="3" stroke-linecap="round"/>
                            <line x1="33" y1="33" x2="45" y2="45" stroke="#2563eb" stroke-width="3" stroke-linecap="round"/>
                        </g>

                        <g transform="translate(360, 160)">
                            <rect x="0" y="0" width="50" height="15" rx="3" fill="#0891b2"/>
                            <rect x="5" y="15" width="3" height="25" fill="#0891b2"/>
                            <rect x="12" y="15" width="3" height="25" fill="#0891b2"/>
                            <rect x="19" y="15" width="3" height="25" fill="#0891b2"/>
                            <rect x="26" y="15" width="3" height="25" fill="#0891b2"/>
                            <rect x="33" y="15" width="3" height="25" fill="#0891b2"/>
                            <rect x="40" y="15" width="3" height="25" fill="#0891b2"/>
                        </g>

                        <rect x="210" y="80" width="80" height="110" rx="40" fill="none" stroke="#a3a3a3" stroke-width="8"/>
                        <rect x="220" y="90" width="60" height="90" rx="30" fill="#e5e5e5" opacity="0.3"/>

                        <circle cx="120" cy="280" r="4" fill="#f59e0b"/>
                        <circle cx="380" cy="300" r="4" fill="#f59e0b"/>
                        <circle cx="140" cy="350" r="3" fill="#2563eb"/>
                        <circle cx="360" cy="250" r="3" fill="#0891b2"/>
                    </svg>
                </div>
            </div>
        </div>
    </section>

    <section class="features-section">
        <div class="container">
            <div class="section-header">
                <h2>Tout ce dont vous avez besoin</h2>
                <p>
                    Une plateforme complète conçue pour simplifier la gestion
                    quotidienne de votre salon de coiffure
                </p>
            </div>
            <div class="features-grid">
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-calendar-check"></i>
                    </div>
                    <h3>Réservation Intelligente</h3>
                    <p>
                        Système de réservation en ligne avec gestion automatique
                        des disponibilités et confirmations instantanées
                    </p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-users"></i>
                    </div>
                    <h3>Gestion d'Équipe</h3>
                    <p>
                        Gérez vos coiffeurs, leurs horaires, leurs spécialités
                        et suivez leurs performances en temps réel
                    </p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-gift"></i>
                    </div>
                    <h3>Programme Fidélité</h3>
                    <p>
                        Système de points automatique avec offres personnalisées
                        pour récompenser vos clients réguliers
                    </p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-clock"></i>
                    </div>
                    <h3>Gestion Horaires</h3>
                    <p>
                        Définissez les disponibilités de chaque coiffeur et
                        gérez les créneaux horaires en toute simplicité
                    </p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-chart-line"></i>
                    </div>
                    <h3>Statistiques Détaillées</h3>
                    <p>
                        Tableaux de bord complets avec métriques clés, revenus,
                        et analyses pour optimiser votre activité
                    </p>
                </div>
                <div class="feature-item">
                    <div class="feature-icon-wrapper">
                        <i class="fas fa-cut"></i>
                    </div>
                    <h3>Catalogue Services</h3>
                    <p>
                        Gérez votre catalogue de services avec descriptions,
                        prix, durées et assignation aux coiffeurs
                    </p>
                </div>
            </div>
        </div>
    </section>

    <section class="demo-section">
        <div class="container">
            <div class="section-header">
                <h2>Testez la Plateforme</h2>
                <p>
                    Explorez toutes les fonctionnalités avec nos comptes de démonstration
                </p>
            </div>
            <div class="demo-grid">
                
                <div class="demo-card">
                    <div class="demo-card-header">
                        <div class="demo-card-icon admin">
                            <i class="fas fa-user-shield"></i>
                        </div>
                        <div>
                            <div class="demo-card-title">Administrateur</div>
                            <div class="demo-card-subtitle">Accès complet</div>
                        </div>
                    </div>
                    <div class="demo-credentials">
                        <div class="demo-credential-item">
                            <i class="fas fa-envelope"></i>
                            <span>admin@barbershop.com</span>
                        </div>
                        <div class="demo-credential-item">
                            <i class="fas fa-lock"></i>
                            <span>admin123</span>
                        </div>
                    </div>
                </div>

                <div class="demo-card">
                    <div class="demo-card-header">
                        <div class="demo-card-icon barber">
                            <i class="fas fa-cut"></i>
                        </div>
                        <div>
                            <div class="demo-card-title">Coiffeur</div>
                            <div class="demo-card-subtitle">Gestion rendez-vous</div>
                        </div>
                    </div>
                    <div class="demo-credentials">
                        <div class="demo-credential-item">
                            <i class="fas fa-envelope"></i>
                            <span>ahmed@barbershop.com</span>
                        </div>
                        <div class="demo-credential-item">
                            <i class="fas fa-lock"></i>
                            <span>barber123</span>
                        </div>
                    </div>
                </div>

                <div class="demo-card">
                    <div class="demo-card-header">
                        <div class="demo-card-icon client">
                            <i class="fas fa-user"></i>
                        </div>
                        <div>
                            <div class="demo-card-title">Client</div>
                            <div class="demo-card-subtitle">Réservation en ligne</div>
                        </div>
                    </div>
                    <div class="demo-credentials">
                        <div class="demo-credential-item">
                            <i class="fas fa-envelope"></i>
                            <span>hassan@email.com</span>
                        </div>
                        <div class="demo-credential-item">
                            <i class="fas fa-lock"></i>
                            <span>client123</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="cta-section">
        <div class="container">
            <h2>Prêt à Commencer ?</h2>
            <p>
                Rejoignez les salons qui ont choisi notre solution pour simplifier leur gestion
            </p>
            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-white btn-lg">
                    <i class="fas fa-sign-in-alt"></i>
                    Se Connecter
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-white btn-lg">
                    <i class="fas fa-user-plus"></i>
                    Créer un Compte Client
                </a>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-brand">
                    <span class="footer-brand-icon"><i class="fas fa-cut"></i></span>
                    <span>GestionCoiffure</span>
                </div>
                <p class="footer-text">
                    Système de gestion professionnel pour salons de coiffure
                </p>
            </div>
        </div>
    </footer>
</body>
</html>
