# GestionCoiffure - Système de Gestion de Salon de Coiffure

Application web complète de gestion de salon de coiffure développée en Java EE, offrant une solution moderne pour gérer les rendez-vous, les coiffeurs, les services et la fidélisation des clients.

## Table des Matières

- [Fonctionnalités](#fonctionnalités)
- [Technologies Utilisées](#technologies-utilisées)
- [Architecture](#architecture)
- [Pages d'Interface Utilisateur](#pages-dinterface-utilisateur)
- [Structure du Projet](#structure-du-projet)
- [Schéma de Base de Données](#schéma-de-base-de-données)
- [Installation](#installation)
- [Configuration](#configuration)
- [Workflow Utilisateur](#workflow-utilisateur)

## Fonctionnalités

### 1. Gestion Multi-Rôles

L'application supporte trois types d'utilisateurs avec des permissions différentes :

#### Administrateur
- Gestion complète des coiffeurs (création, modification, activation/désactivation)
- Gestion du catalogue de services (prix, durée, description)
- Gestion des offres de fidélité (création, modification, activation)
- Configuration des horaires de disponibilité des coiffeurs
- Visualisation de tous les rendez-vous
- Accès aux statistiques et analyses
- Gestion des clients et historique

#### Coiffeur
- Tableau de bord personnalisé avec statistiques
- Gestion des rendez-vous (confirmation, refus, complétion)
- Création manuelle de rendez-vous pour les clients
- Consultation de son planning et disponibilités
- Visualisation de sa liste de clients

#### Client
- Réservation de rendez-vous en ligne
- Visualisation de l'historique des rendez-vous
- Annulation de rendez-vous avec motif
- Programme de fidélité avec points
- Consultation et utilisation des offres fidélité
- Profil avec statut de fidélité

### 2. Système de Réservation Intelligent

- Sélection du coiffeur, service, date et heure
- Vérification automatique des disponibilités
- Affichage des horaires disponibles par coiffeur
- Prévention des conflits de réservation
- Calcul automatique de la durée et du prix
- Confirmation en temps réel

### 3. Programme de Fidélité

- Accumulation de points à chaque rendez-vous complété
- Statuts de fidélité (Standard/Fidèle)
- Création d'offres promotionnelles par l'administrateur
- Échange de points contre des offres
- Application de réductions lors de la réservation
- Historique des offres utilisées

### 4. Gestion des Horaires

- Configuration des disponibilités par coiffeur
- Planification par jour de la semaine
- Plages horaires personnalisables
- Gestion des créneaux occupés/disponibles

### 5. Statistiques et Rapports

- Nombre de rendez-vous par période
- Taux de confirmation/annulation
- Clients actifs et fidèles
- Revenus et tendances
- Performance par coiffeur

## Technologies Utilisées

### Backend

- **Java 21** - Langage de programmation
- **Java EE (Jakarta EE)** - Plateforme d'entreprise
- **Servlets 6.0.0** - Gestion des requêtes HTTP
- **JSP (JavaServer Pages) 3.1.0** - Génération de vues dynamiques
- **JSTL 3.0.0** - Tag library pour JSP
- **Apache Tomcat 10.1.x** - Conteneur de servlets

### Base de Données

- **MySQL 8.0.33** - Système de gestion de base de données
- **MySQL Connector/J 8.0.33** - Driver JDBC
- **JDBC** - API de connectivité à la base de données

### Sécurité

- **jBCrypt 0.4** - Hachage sécurisé des mots de passe
- **Salted Password Hashing** - Protection contre les attaques par rainbow tables
- **PreparedStatements** - Prévention des injections SQL
- **Session Management** - Gestion sécurisée des sessions utilisateur

### Frontend

- **HTML5** - Structure des pages
- **CSS3** - Stylisation et mise en page
- **JavaScript (Vanilla)** - Interactivité côté client
- **Font Awesome 6.4.0** - Bibliothèque d'icônes
- **SVG** - Illustrations vectorielles personnalisées

### Build & Dépendances

- **Apache Maven 3.x** - Gestion de projet et dépendances
- **Maven WAR Plugin 3.3.2** - Construction du fichier WAR
- **Maven Compiler Plugin 3.11.0** - Compilation Java

## Architecture

### Pattern MVC (Model-View-Controller)

L'application est structurée selon le pattern architectural MVC qui permet une séparation claire des responsabilités et facilite la maintenance et l'évolutivité du code.

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│             │ Request │              │  Query  │             │
│   Browser   │────────>│  Controller  │────────>│     DAO     │
│   (View)    │         │  (Servlet)   │         │   (Model)   │
│             │<────────│              │<────────│             │
└─────────────┘ Response└──────────────┘  Data   └─────────────┘
                         │
                         │ Forward
                         ↓
                   ┌──────────┐
                   │   JSP    │
                   │  (View)  │
                   └──────────┘
```

### Architecture en Couches

L'application est organisée en 5 couches distinctes :

#### 1. Couche Présentation (View Layer)

**Responsabilité** : Affichage de l'interface utilisateur et interaction avec l'utilisateur.

**Technologies** : JSP, JSTL, HTML5, CSS3, JavaScript

**Composants** :
- **Pages JSP** : Génèrent dynamiquement le HTML
- **CSS Modules** : Stylisation modulaire et responsive
- **JavaScript** : Validation côté client et interactivité

**Organisation** :
- Vues organisées par rôle (admin, barber, client)
- Composants réutilisables (header, footer, menus)
- Design system cohérent avec variables CSS

#### 2. Couche Contrôleur (Controller Layer)

**Responsabilité** : Réception des requêtes HTTP, orchestration de la logique métier, et retour des réponses.

**Technologies** : Servlets Java EE

**Composants** : 20 Servlets organisés par fonctionnalité

**Flux de traitement** :
1. Réception de la requête HTTP (GET/POST)
2. Validation des données d'entrée
3. Vérification de l'authentification et des autorisations
4. Appel des services métier
5. Interaction avec la couche DAO
6. Préparation des données pour la vue
7. Forwarding vers la JSP appropriée ou redirection

**Mapping des URLs** :
- `/login` → LoginServlet
- `/logout` → LogoutServlet
- `/register` → RegisterServlet
- `/admin/*` → Servlets administrateur
- `/barber/*` → Servlets coiffeur
- `/client/*` → Servlets client

#### 3. Couche Service (Business Logic Layer)

**Responsabilité** : Logique métier complexe et validation des règles d'affaires.

**Composants** :
- **AuthenticationService** :
  - Authentification multi-rôles (Admin, Barber, Client)
  - Vérification des mots de passe hachés
  - Validation des statuts (barber actif, etc.)
  - Prévention des doublons d'email
  - Enregistrement sécurisé des nouveaux clients

**Principes** :
- Réutilisabilité du code métier
- Indépendance vis-à-vis de la couche présentation
- Centralisation des règles de validation
- Transaction management

#### 4. Couche DAO (Data Access Layer)

**Responsabilité** : Abstraction de l'accès aux données et interaction avec la base de données.

**Pattern** : Data Access Object (DAO)

**8 Classes DAO** :

1. **AdminDAO** :
   - `findByEmail(String email)` : Recherche par email
   - `findById(int id)` : Recherche par ID
   - `create(Admin admin)` : Création

2. **BarberDAO** :
   - `findByEmail(String email)` : Recherche par email
   - `findById(int id)` : Recherche par ID
   - `findAll()` : Liste complète
   - `findAllActive()` : Liste des coiffeurs actifs
   - `create(Barber barber)` : Création
   - `update(Barber barber)` : Mise à jour
   - `delete(int id)` : Suppression
   - `updateStatus(int id, String status)` : Changement de statut

3. **ClientDAO** :
   - `findByEmail(String email)` : Recherche par email
   - `findById(int id)` : Recherche par ID
   - `findAll()` : Liste complète
   - `create(Client client)` : Création
   - `update(Client client)` : Mise à jour
   - `updatePoints(int id, int points)` : Mise à jour des points
   - `updateLoyaltyStatus(int id, String status)` : Changement de statut fidélité
   - `getCompletedAppointmentCount(int id)` : Comptage des RDV complétés

4. **AppointmentDAO** (le plus complexe - 470+ lignes) :
   - `create(Appointment appointment)` : Création avec fallback
   - `findById(int id)` : Recherche par ID
   - `findByClientId(int clientId)` : RDV d'un client
   - `findByBarberId(int barberId)` : RDV d'un coiffeur
   - `findByBarberAndDate(int barberId, LocalDate date)` : RDV par jour
   - `findUpcomingByClientId(int clientId)` : RDV à venir d'un client
   - `updateStatus(int id, String status)` : Changement de statut
   - `cancel(int id, String reason)` : Annulation
   - `isBarberAvailable(...)` : Vérification de disponibilité complexe
   - `isTimeSlotAvailable(...)` : Vérification de conflit de créneaux
   - `getDayOfWeekAbbreviation(LocalDate date)` : Helper pour jours

5. **ServiceDAO** :
   - `findById(int id)` : Recherche par ID
   - `findAll()` : Liste complète
   - `findAllActive()` : Liste des services actifs
   - `create(Service service)` : Création
   - `update(Service service)` : Mise à jour
   - `delete(int id)` : Suppression
   - `toggleActive(int id)` : Activation/Désactivation

6. **OfferDAO** (300+ lignes - gestion fidélité) :
   - `findById(int id)` : Recherche par ID
   - `findAll()` : Liste complète
   - `findAllActive()` : Liste des offres actives
   - `findAffordableOffers(int points)` : Offres accessibles
   - `create(Offer offer)` : Création
   - `update(Offer offer)` : Mise à jour
   - `delete(int id)` : Suppression
   - `redeemOffer(int clientId, int offerId)` : Échange de points
   - `findRedeemedOffersByClient(int clientId)` : Offres échangées
   - `findUnusedRedeemedOffersByClient(int clientId)` : Offres non utilisées
   - `markRedemptionAsUsed(int redemptionId, int appointmentId)` : Utilisation
   - `findRedemptionByAppointmentId(int appointmentId)` : Recherche par RDV

7. **AvailabilityDAO** :
   - `findByBarberId(int barberId)` : Disponibilités d'un coiffeur
   - `findByBarberAndDay(int barberId, String day)` : Par jour spécifique
   - `create(Availability availability)` : Création
   - `update(Availability availability)` : Mise à jour
   - `delete(int id)` : Suppression
   - `deleteByBarberId(int barberId)` : Suppression en cascade

**Principes DAO** :
- Une classe DAO par entité métier
- Méthodes CRUD standardisées
- Gestion des ressources JDBC (try-with-resources)
- PreparedStatements pour toutes les requêtes
- Gestion des exceptions SQL
- Mapping ResultSet → Objets métier

#### 5. Couche Modèle (Model/Domain Layer)

**Responsabilité** : Représentation des entités métier et des données.

**Pattern** : Plain Old Java Objects (POJOs) / JavaBeans

**8 Classes Modèle** :

Chaque classe suit le pattern JavaBean :
- Attributs privés
- Constructeur sans paramètres
- Constructeur avec paramètres
- Getters et Setters
- Méthode toString() pour debug
- Méthodes helper (isActive(), isPending(), etc.)

**Entités** :
1. `Admin` : Administrateur système
2. `Barber` : Coiffeur avec statut et bio
3. `Client` : Client avec points et statut fidélité
4. `Appointment` : Rendez-vous avec statuts multiples
5. `Service` : Service avec prix et durée
6. `Offer` : Offre fidélité
7. `OfferRedemption` : Échange de points contre offre
8. `Availability` : Disponibilité hebdomadaire des coiffeurs

### Couche Utilitaire

**Responsabilité** : Fonctionnalités transversales réutilisables.

**3 Classes Utilitaires** :

1. **DatabaseUtil** :
   - Singleton pour la gestion des connexions
   - Chargement du driver JDBC
   - Factory pour les connexions
   - Fermeture sécurisée des ressources
   - Test de connectivité

2. **PasswordUtil** :
   - Hachage BCrypt avec salt (10 rounds)
   - Vérification des mots de passe
   - Validation de la force (minimum 6 caractères)
   - Messages de feedback en français

3. **DatabaseMigration** :
   - Migration automatique du schéma
   - Création de tables manquantes
   - Ajout de colonnes (final_price, redemption_id)
   - Création d'index de performance
   - Gestion des erreurs de migration

### Flux de Données

**Exemple : Création d'un rendez-vous par un client**

```
1. Client soumet le formulaire de réservation
   ↓
2. BookAppointmentServlet.doPost() reçoit la requête
   ↓
3. Validation des paramètres (barberId, serviceId, date, time)
   ↓
4. Récupération du service via ServiceDAO
   ↓
5. Calcul de l'heure de fin (startTime + duration)
   ↓
6. Vérification de disponibilité via AppointmentDAO.isBarberAvailable()
   - Vérifie les horaires dans Availability
   - Vérifie les conflits dans Appointment
   ↓
7. Si disponible : Création de l'Appointment
   ↓
8. Si offre utilisée : Mise à jour de OfferRedemption via OfferDAO
   ↓
9. Redirection vers la liste des rendez-vous avec message de succès
   ↓
10. JSP affiche la confirmation
```

### Gestion des Sessions

**Mécanisme** :
- Session HTTP standard Java EE
- Timeout : 30 minutes d'inactivité
- Stockage des informations utilisateur :
  - `userId` : Identifiant unique
  - `userType` : Rôle (admin/barber/client)
  - `userName` : Nom pour affichage
  - `user` : Objet complet (Admin/Barber/Client)

**Sécurité** :
- Validation du rôle à chaque requête
- Vérification de la présence de session
- Redirection vers login si session invalide
- Invalidation complète au logout

## Pages d'Interface Utilisateur

### Pages Publiques

- **`index.jsp`** - Page d'accueil avec présentation
  - Hero section avec illustration SVG
  - Grille de fonctionnalités (6 features)
  - Comptes de démonstration
  - Section call-to-action
  - Footer

- **`login.jsp`** - Authentification
  - Sélection du rôle (Admin/Barber/Client)
  - Formulaire email/mot de passe
  - Toggle visibilité du mot de passe
  - Messages d'erreur/succès
  - Design split-screen

- **`register.jsp`** - Inscription client
  - Formulaire d'inscription complet
  - Validation côté client
  - Indicateur de force du mot de passe
  - Confirmation de mot de passe
  - Vérification d'unicité de l'email

### Espace Administrateur (12 pages)

- **`dashboard.jsp`** - Tableau de bord
  - Statistiques clés (coiffeurs, services, clients, RDV)
  - Graphiques et métriques
  - Accès rapide aux fonctionnalités

- **`barbers.jsp`** - Gestion des coiffeurs
  - Liste complète des coiffeurs
  - Filtrage et recherche
  - Boutons d'action (Modifier, Activer/Désactiver, Supprimer)
  - Badge de statut (Actif/Inactif)

- **`barber-form.jsp`** - Formulaire coiffeur
  - Création/Modification de coiffeur
  - Champs : nom, email, téléphone, bio, mot de passe
  - Validation des données

- **`services.jsp`** - Gestion des services
  - Liste des services avec prix et durée
  - Toggle actif/inactif
  - Actions CRUD

- **`service-form.jsp`** - Formulaire service
  - Création/Modification de service
  - Champs : nom, description, durée (minutes), prix
  - Checkbox actif/inactif

- **`offers.jsp`** - Gestion des offres fidélité
  - Liste des offres
  - Points requis affichés
  - Statut actif/inactif

- **`offer-form.jsp`** - Formulaire offre
  - Création/Modification d'offre
  - Champs : titre, description, points requis
  - Toggle actif

- **`availability.jsp`** - Gestion des disponibilités
  - Liste par coiffeur
  - Horaires par jour de semaine
  - Actions de modification/suppression

- **`availability-form.jsp`** - Formulaire disponibilité
  - Sélection du coiffeur
  - Jour de la semaine
  - Heure début/fin

- **`appointments.jsp`** - Tous les rendez-vous
  - Vue globale avec filtres
  - Informations complètes (client, coiffeur, service, date, statut)
  - Actions d'annulation

- **`clients.jsp`** - Gestion des clients
  - Liste complète
  - Points et statut fidélité
  - Historique des rendez-vous

- **`statistics.jsp`** - Statistiques et analyses
  - Rapports détaillés
  - Graphiques de performance
  - Métriques par période

### Espace Coiffeur (5 pages)

- **`dashboard.jsp`** - Tableau de bord coiffeur
  - Statistiques personnelles (RDV aujourd'hui, confirmés, en attente, clients du mois)
  - Rendez-vous du jour
  - Actions rapides
  - Calendrier personnel

- **`appointments.jsp`** - Gestion des rendez-vous
  - Liste des RDV (en attente, confirmés, complétés)
  - Filtres par statut et date
  - Actions : Confirmer, Refuser, Compléter
  - Détails client et service

- **`create-appointment.jsp`** - Création de RDV
  - Sélection du client
  - Choix du service
  - Date et heure
  - Création rapide avec statut "confirmé"

- **`clients.jsp`** - Liste des clients
  - Clients ayant réservé avec ce coiffeur
  - Historique des rendez-vous par client
  - Informations de contact

- **`schedule.jsp`** - Planning personnel
  - Disponibilités configurées
  - Vue hebdomadaire
  - Créneaux occupés/libres

### Espace Client (4 pages)

- **`dashboard.jsp`** - Tableau de bord client
  - Vue d'ensemble personnelle
  - Statut fidélité (Standard/Fidèle)
  - Solde de points
  - Prochain rendez-vous
  - Statistiques personnelles

- **`appointments.jsp`** - Mes rendez-vous
  - Rendez-vous à venir
  - Historique complet
  - Filtres par statut
  - Action d'annulation avec motif
  - Affichage des offres appliquées

- **`book-appointment.jsp`** - Réserver un RDV
  - Sélection du service
  - Choix du coiffeur
  - Sélection de la date
  - **Affichage des horaires disponibles du coiffeur sélectionné**
  - Choix de l'heure
  - Application optionnelle d'une offre fidélité (réduction 10%)
  - Calcul automatique du prix final
  - Confirmation

- **`offers.jsp`** - Programme fidélité
  - Solde de points actuel
  - Offres disponibles (filtrées par points)
  - Offres échangées (utilisées/non utilisées)
  - Action d'échange de points
  - Historique des échanges

## Structure du Projet

```
GestionCoiffure/
│
├── src/
│   └── main/
│       ├── java/
│       │   │
│       │   ├── controller/                    # Couche Contrôleur (20 Servlets)
│       │   │   ├── LoginServlet.java          # Authentification (POST /login)
│       │   │   ├── LogoutServlet.java         # Déconnexion (GET /logout)
│       │   │   ├── RegisterServlet.java       # Inscription client (GET/POST /register)
│       │   │   │
│       │   │   ├── AdminDashboardServlet.java           # Dashboard admin
│       │   │   ├── ManageBarbersServlet.java            # CRUD coiffeurs
│       │   │   ├── ManageServicesServlet.java           # CRUD services
│       │   │   ├── ManageOffersServlet.java             # CRUD offres
│       │   │   ├── ManageAvailabilityServlet.java       # CRUD disponibilités
│       │   │   ├── AdminAppointmentsServlet.java        # Vue tous RDV
│       │   │   ├── ViewClientsServlet.java              # Liste clients
│       │   │   └── AdminStatisticsServlet.java          # Statistiques
│       │   │   │
│       │   │   ├── BarberDashboardServlet.java          # Dashboard coiffeur
│       │   │   ├── BarberAppointmentsServlet.java       # Gestion RDV coiffeur
│       │   │   ├── BarberCreateAppointmentServlet.java  # Création RDV manuel
│       │   │   ├── BarberClientsServlet.java            # Clients du coiffeur
│       │   │   └── BarberScheduleServlet.java           # Planning coiffeur
│       │   │   │
│       │   │   ├── ClientDashboardServlet.java          # Dashboard client
│       │   │   ├── ClientAppointmentsServlet.java       # RDV du client
│       │   │   ├── BookAppointmentServlet.java          # Réservation RDV
│       │   │   └── ClientOffersServlet.java             # Gestion offres client
│       │   │
│       │   ├── dao/                           # Couche DAO (8 classes)
│       │   │   ├── AdminDAO.java              # Accès données Admin
│       │   │   ├── BarberDAO.java             # Accès données Barber (CRUD + findAllActive)
│       │   │   ├── ClientDAO.java             # Accès données Client (CRUD + points)
│       │   │   ├── AppointmentDAO.java        # Accès données Appointment (complexe - 470 lignes)
│       │   │   ├── ServiceDAO.java            # Accès données Service
│       │   │   ├── OfferDAO.java              # Accès données Offer + Redemption (300 lignes)
│       │   │   └── AvailabilityDAO.java       # Accès données Availability
│       │   │
│       │   ├── model/                         # Couche Modèle (8 POJOs)
│       │   │   ├── Admin.java                 # Entité administrateur
│       │   │   ├── Barber.java                # Entité coiffeur (+ status, bio)
│       │   │   ├── Client.java                # Entité client (+ points, loyalty_status)
│       │   │   ├── Appointment.java           # Entité RDV (+ status, redemption, final_price)
│       │   │   ├── Service.java               # Entité service (+ duration, price, is_active)
│       │   │   ├── Offer.java                 # Entité offre fidélité
│       │   │   ├── OfferRedemption.java       # Entité échange offre
│       │   │   └── Availability.java          # Entité disponibilité (+ day_of_week, time)
│       │   │
│       │   ├── service/                       # Couche Service (2 classes)
│       │   │   ├── AuthenticationService.java # Service d'authentification multi-rôles
│       │   │   └── Service.java               # Classe service métier (note: même nom que model)
│       │   │
│       │   └── util/                          # Couche Utilitaire (3 classes)
│       │       ├── DatabaseUtil.java          # Gestion connexions DB (singleton)
│       │       ├── PasswordUtil.java          # Hachage BCrypt et validation
│       │       └── DatabaseMigration.java     # Migration de schéma automatique
│       │
│       └── webapp/                            # Ressources Web
│           │
│           ├── css/                           # Feuilles de style (4 fichiers)
│           │   ├── design-system.css          # Variables CSS, typographie, grille (23 KB)
│           │   ├── components.css             # Composants UI réutilisables (25 KB)
│           │   ├── dashboard.css              # Styles des dashboards (15 KB)
│           │   └── auth.css                   # Styles authentification (6 KB)
│           │
│           ├── js/                            # Scripts JavaScript
│           │   └── app.js                     # Scripts globaux et utilitaires
│           │
│           ├── WEB-INF/                       # Ressources protégées
│           │   │
│           │   ├── views/                     # Pages JSP (21 fichiers)
│           │   │   │
│           │   │   ├── admin/                 # Vues Administrateur (12 JSP)
│           │   │   │   ├── dashboard.jsp      # Dashboard avec stats
│           │   │   │   ├── barbers.jsp        # Liste coiffeurs
│           │   │   │   ├── barber-form.jsp    # Form création/édition coiffeur
│           │   │   │   ├── services.jsp       # Liste services
│           │   │   │   ├── service-form.jsp   # Form création/édition service
│           │   │   │   ├── offers.jsp         # Liste offres fidélité
│           │   │   │   ├── offer-form.jsp     # Form création/édition offre
│           │   │   │   ├── availability.jsp   # Liste disponibilités
│           │   │   │   ├── availability-form.jsp # Form disponibilité
│           │   │   │   ├── appointments.jsp   # Liste tous RDV
│           │   │   │   ├── clients.jsp        # Liste clients
│           │   │   │   └── statistics.jsp     # Statistiques et rapports
│           │   │   │
│           │   │   ├── barber/                # Vues Coiffeur (5 JSP)
│           │   │   │   ├── dashboard.jsp      # Dashboard coiffeur
│           │   │   │   ├── appointments.jsp   # Gestion RDV (confirm/refuse/complete)
│           │   │   │   ├── create-appointment.jsp # Création manuelle RDV
│           │   │   │   ├── clients.jsp        # Clients du coiffeur
│           │   │   │   └── schedule.jsp       # Planning et disponibilités
│           │   │   │
│           │   │   ├── client/                # Vues Client (4 JSP)
│           │   │   │   ├── dashboard.jsp      # Dashboard avec points et statut
│           │   │   │   ├── appointments.jsp   # Liste RDV (historique + à venir)
│           │   │   │   ├── book-appointment.jsp # Réservation avec disponibilités
│           │   │   │   └── offers.jsp         # Programme fidélité
│           │   │   │
│           │   │   ├── login.jsp              # Page de connexion
│           │   │   └── register.jsp           # Page d'inscription
│           │   │
│           │   └── web.xml                    # Configuration Servlet (deployment descriptor)
│           │
│           └── index.jsp                      # Page d'accueil publique
│
├── target/                                    # Dossier de build (généré par Maven)
│   ├── classes/                               # Fichiers .class compilés
│   ├── gestionCoiffure/                       # Application déployée (exploded WAR)
│   └── gestionCoiffure.war                    # Archive WAR déployable
│
├── .settings/                                 # Configuration IDE Eclipse
├── .classpath                                 # Classpath Eclipse
├── .project                                   # Project Eclipse
│
├── pom.xml                                    # Configuration Maven (dépendances, plugins)
└── README.md                                  # Documentation complète

```

### Organisation des Packages Java

**Nombre total de fichiers Java : 39**

- `controller` : 20 servlets
- `dao` : 8 classes DAO
- `model` : 8 entités
- `service` : 2 classes service
- `util` : 3 utilitaires

### Organisation des Ressources Web

**Pages JSP : 21 fichiers**
- Admin : 12 pages
- Barber : 5 pages
- Client : 4 pages

**CSS : 4 feuilles de style (~70 KB total)**
- Design system avec variables
- Composants réutilisables
- Dashboards responsive
- Styles d'authentification

**JavaScript : Scripts modulaires**
- Validation côté client
- Interactivité dynamique
- Affichage conditionnel
- Calculs en temps réel

## Schéma de Base de Données

### Tables Principales

#### Table `Admin`
```sql
CREATE TABLE Admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `Barber`
```sql
CREATE TABLE Barber (
    barber_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    bio TEXT,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `Client`
```sql
CREATE TABLE Client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    points_balance INT DEFAULT 0,
    loyalty_status VARCHAR(20) DEFAULT 'standard',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `Service`
```sql
CREATE TABLE Service (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    duration INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `Appointment`
```sql
CREATE TABLE Appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    barber_id INT NOT NULL,
    service_id INT NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    redemption_id INT NULL,
    final_price DECIMAL(10,2) NULL,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES Client(client_id),
    FOREIGN KEY (barber_id) REFERENCES Barber(barber_id),
    FOREIGN KEY (service_id) REFERENCES Service(service_id),
    FOREIGN KEY (redemption_id) REFERENCES OfferRedemption(redemption_id)
);
```

#### Table `Availability`
```sql
CREATE TABLE Availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    barber_id INT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (barber_id) REFERENCES Barber(barber_id) ON DELETE CASCADE
);
```

#### Table `Offer`
```sql
CREATE TABLE Offer (
    offer_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    points_required INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `OfferRedemption`
```sql
CREATE TABLE OfferRedemption (
    redemption_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    offer_id INT NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    appointment_id INT NULL,
    redeemed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP NULL,
    FOREIGN KEY (client_id) REFERENCES Client(client_id) ON DELETE CASCADE,
    FOREIGN KEY (offer_id) REFERENCES Offer(offer_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id)
);
```

### Relations

- **Client ↔ Appointment** : Un client peut avoir plusieurs rendez-vous (1:N)
- **Barber ↔ Appointment** : Un coiffeur peut avoir plusieurs rendez-vous (1:N)
- **Service ↔ Appointment** : Un service peut être réservé plusieurs fois (1:N)
- **Barber ↔ Availability** : Un coiffeur a plusieurs plages de disponibilité (1:N)
- **Client ↔ OfferRedemption** : Un client peut échanger plusieurs offres (1:N)
- **Offer ↔ OfferRedemption** : Une offre peut être échangée plusieurs fois (1:N)
- **Appointment ↔ OfferRedemption** : Une offre peut être appliquée à un rendez-vous (1:1)

### Index de Performance

```sql
CREATE INDEX idx_redemption_client ON OfferRedemption(client_id, is_used);
CREATE INDEX idx_appointment_redemption ON Appointment(redemption_id);
```

## Installation

### Prérequis

- Java Development Kit (JDK) 21 ou supérieur
- Apache Maven 3.6+
- MySQL Server 8.0+
- Apache Tomcat 10.1.x
- IDE (Eclipse, IntelliJ IDEA, ou VS Code)

### Étapes d'Installation

1. **Cloner le projet**
   ```bash
   git clone <repository-url>
   cd GestionCoiffure
   ```

2. **Créer la base de données**
   ```sql
   CREATE DATABASE barbershop_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE barbershop_db;
   ```

3. **Exécuter les scripts SQL**
   - Créer les tables principales (Admin, Barber, Client, Service, Appointment, Availability, Offer, OfferRedemption)
   - Insérer les données de test

4. **Configurer la connexion à la base de données**

   Éditer `src/main/java/util/DatabaseUtil.java` :
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/barbershop_db";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "votre_mot_de_passe";
   ```

5. **Exécuter la migration (optionnel)**
   ```bash
   mvn compile exec:java -Dexec.mainClass="util.DatabaseMigration"
   ```

6. **Compiler le projet**
   ```bash
   mvn clean package
   ```

7. **Déployer sur Tomcat**
   - Copier `target/gestionCoiffure.war` dans le dossier `webapps` de Tomcat
   - Démarrer Tomcat
   - Accéder à `http://localhost:8080/gestionCoiffure`

## Configuration

### Configuration Maven (`pom.xml`)

Le projet utilise les dépendances suivantes :
- Jakarta Servlet API 6.0.0
- Jakarta JSP API 3.1.0
- JSTL 3.0.0
- MySQL Connector/J 8.0.33
- jBCrypt 0.4

### Configuration Web (`web.xml`)

- Session timeout: 30 minutes
- Page d'accueil: `index.jsp`
- Encodage: UTF-8

### Structure CSS

Le système de design utilise :
- Variables CSS pour les couleurs et espacements
- Grille responsive
- Composants réutilisables
- Thèmes par rôle (admin, barber, client)

## Workflow Utilisateur

### Workflow Client

1. **Inscription/Connexion**
   - Création de compte client via le formulaire d'inscription
   - Connexion avec email et mot de passe

2. **Réservation de Rendez-vous**
   - Sélection du service souhaité
   - Choix du coiffeur
   - Sélection de la date
   - Visualisation des horaires disponibles du coiffeur
   - Sélection de l'heure
   - Application optionnelle d'une offre fidélité (réduction de 10%)
   - Confirmation de la réservation

3. **Gestion des Rendez-vous**
   - Visualisation des rendez-vous à venir
   - Consultation de l'historique
   - Annulation avec motif si nécessaire

4. **Programme Fidélité**
   - Consultation du solde de points
   - Visualisation des offres disponibles
   - Échange de points contre des offres
   - Application des offres lors de réservations

### Workflow Coiffeur

1. **Connexion**
   - Authentification avec email et mot de passe

2. **Tableau de Bord**
   - Visualisation des rendez-vous du jour
   - Statistiques personnelles
   - Clients du mois

3. **Gestion des Rendez-vous**
   - Consultation des rendez-vous en attente
   - Confirmation ou refus des demandes
   - Marquage des rendez-vous comme complétés
   - Création manuelle de rendez-vous

4. **Consultation du Planning**
   - Visualisation des disponibilités configurées
   - Consultation du calendrier de réservations

### Workflow Administrateur

1. **Connexion**
   - Authentification avec identifiants administrateur

2. **Gestion des Coiffeurs**
   - Ajout de nouveaux coiffeurs
   - Modification des informations
   - Activation/Désactivation de comptes
   - Suppression de coiffeurs

3. **Gestion des Services**
   - Création de nouveaux services
   - Modification des prix et durées
   - Activation/Désactivation de services

4. **Gestion des Disponibilités**
   - Configuration des horaires par coiffeur
   - Définition des plages horaires hebdomadaires

5. **Gestion des Offres**
   - Création d'offres fidélité
   - Définition du nombre de points requis
   - Activation/Désactivation d'offres

6. **Suivi et Statistiques**
   - Consultation de tous les rendez-vous
   - Visualisation des clients
   - Analyses et rapports

## Comptes de Démonstration

Pour tester l'application, utilisez les comptes suivants :

### Administrateur
- Email: `admin@barbershop.com`
- Mot de passe: `admin123`

### Coiffeur
- Email: `ahmed@barbershop.com`
- Mot de passe: `barber123`

### Client
- Email: `hassan@email.com`
- Mot de passe: `client123`

## Sécurité

### Mesures de Sécurité Implémentées

1. **Hachage des Mots de Passe**
   - Utilisation de BCrypt avec salting
   - Rounds de hachage : 10
   - Validation de la force des mots de passe

2. **Prévention des Injections SQL**
   - Utilisation exclusive de PreparedStatements
   - Paramétrage de toutes les requêtes

3. **Gestion des Sessions**
   - Timeout de session : 30 minutes
   - Validation des rôles à chaque requête
   - Déconnexion sécurisée

4. **Validation des Données**
   - Validation côté serveur de tous les inputs
   - Format des emails vérifié
   - Longueur minimale des mots de passe : 6 caractères

## Licence

Ce projet est développé à des fins éducatives et de démonstration.

## Support

Pour toute question ou problème, veuillez consulter la documentation ou contacter l'équipe de développement.
