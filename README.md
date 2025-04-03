"# javaproject" 
# README - Application de Gestion d'École

## Description
Cette application est une solution de gestion des écoles développée en Java en utilisant **JavaFX** pour l'interface graphique, conçue avec **Scene Builder**. L'application propose trois interfaces utilisateur distinctes : 

- **Secrétaire** : Gestion administrative des inscriptions et des dossiers.
- **Professeur** : Gestion des cours et saisie des notes.
- **Administrateur** : Gestion des utilisateurs, des matières et de l'organisation scolaire.

## Fonctionnalités
### 1. Interface Secrétaire
- Gestion des inscriptions
- Organisation des dossiers administratifs
- Communication avec les étudiants et professeurs

### 2. Interface Professeur
- Attribution et modification des notes des étudiants
- Gestion des matières enseignées
- Consultation des listes d'étudiants

### 3. Interface Administrateur
- Création et gestion des comptes (secrétaires, professeurs)
- Gestion des matières et des emplois du temps
- Administration des données de l'école

## Technologies Utilisées
- **Langage** : Java (JDK 11+)
- **Framework UI** : JavaFX avec Scene Builder
- **Base de Données** : PostgreSQL
- **Outils** : IntelliJ IDEA / Eclipse, Scene Builder

## Installation et Exécution
### Prérequis
- Java Development Kit (JDK) 11+
- JavaFX SDK
- PostgreSQL installé et configuré
- Scene Builder installé

### Étapes d'installation
1. **Cloner le dépôt**
   ```sh
   git clone https://github.com/votre-repo/gestion-ecole-javafx.git
   cd gestion-ecole-javafx
   ```
2. **Configurer la base de données PostgreSQL** :
   - Importer le script `database.sql` disponible dans le projet.
3. **Compiler et exécuter l'application**
   - Avec Maven :
     ```sh
     mvn clean install
     mvn javafx:run
     ```
   - Ou exécuter directement depuis votre IDE (Eclipse/IntelliJ IDEA)

## Structure du Projet
```
/gestion-ecole-javafx
│── src/main/java/
│   ├── controller/        # Contrôleurs des interfaces JavaFX
│   │   ├── Admin/
│   │   ├── Professeur/
│   │   ├── secretaire/
│   │   ├── BaseController.java
│   │   ├── LoginController.java
│   │   ├── ProfesseurController.java
│   ├── dao/               # Couche d'accès aux données
│   │   ├── EtudiantImp.java
│   │   ├── GererEtudiantIMP.java
│   │   ├── InscriptionImp.java
│   │   ├── ModuleImp.java
│   │   ├── ProfesseurImp.java
│   │   ├── SecretaireImp.java
│   │   ├── UserDAO.java
│   │   ├── UserImp.java
│   ├── main/              # Point d'entrée de l'application
│   │   ├── main.java
│   │   ├── MainTest.java
│   │   ├── test.java
│   ├── model/             # Modèles de données (Secrétaire, Professeur, Admin, etc.)
│   ├── util/              # Configuration et utilitaires
│   │   ├── module-info.java
│── src/main/resources/
│   ├── vues/              # Fichiers FXML pour l'interface
│── .gitignore
│── mvnw
│── mvnw.cmd
│── pom.xml                 # Fichier Maven pour gestion des dépendances
│── README.md               # Documentation du projet
```

## Auteurs
- **Souhail Chaiberras**

## Licence
Ce projet est sous licence MIT. Vous êtes libre de l'utiliser et de le modifier.

