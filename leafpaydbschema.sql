CREATE DATABASE IF NOT EXISTS leafpay CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE leafpay;

CREATE TABLE role (
  id_role INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50) NOT NULL
);

CREATE TABLE utilisateur (
  id_user INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  mot_de_passe VARCHAR(255) NOT NULL,
  date_naissance DATE,
  type_justificatif_age VARCHAR(255),
  statut VARCHAR(50),
  date_creation DATETIME,
  id_role INT,
  FOREIGN KEY (id_role) REFERENCES role(id_role)
);

CREATE TABLE compte (
  id_compte INT AUTO_INCREMENT PRIMARY KEY,
  type_compte VARCHAR(50),
  solde DECIMAL(12,2),
  plafond_transaction DECIMAL(12,2),
  limite_retraits_mensuels INT,
  taux_interet DECIMAL(5,2),
  date_ouverture DATETIME,
  date_fermeture DATETIME,
  statut VARCHAR(50),
  iban VARCHAR(34) UNIQUE
);

CREATE TABLE utilisateur_compte (
  id_user INT,
  id_compte INT,
  role_utilisateur_sur_ce_compte VARCHAR(100),
  PRIMARY KEY (id_user, id_compte),
  FOREIGN KEY (id_user) REFERENCES utilisateur(id_user),
  FOREIGN KEY (id_compte) REFERENCES compte(id_compte)
);

CREATE TABLE transaction (
  id_transaction INT AUTO_INCREMENT PRIMARY KEY,
  id_compte_source INT,
  id_compte_destination INT,
  montant DECIMAL(12,2),
  type_transaction VARCHAR(50),
  date_transaction DATETIME,
  statut VARCHAR(50),
  moyen_validation VARCHAR(255),
  justificatif TEXT,
  FOREIGN KEY (id_compte_source) REFERENCES compte(id_compte)
);

CREATE TABLE log (
  id_log INT AUTO_INCREMENT PRIMARY KEY,
  id_user INT,
  action VARCHAR(255),
  timestamp DATETIME,
  ip_utilisateur VARCHAR(50),
  resultat VARCHAR(50),
  description TEXT,
  FOREIGN KEY (id_user) REFERENCES utilisateur(id_user)
);

CREATE TABLE alerte_securite (
  id_alerte INT AUTO_INCREMENT PRIMARY KEY,
  id_user INT,
  type_alerte VARCHAR(100),
  niveau_severite VARCHAR(50),
  timestamp DATETIME,
  est_traitee BOOLEAN,
  FOREIGN KEY (id_user) REFERENCES utilisateur(id_user)
);
