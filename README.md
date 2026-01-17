# ChâTop - API Backend Spring Boot

API REST sécurisée pour la gestion de locations immobilières, développée avec Spring Boot 3, Spring Security et authentification JWT.

## 🛠 Technologies utilisées

- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Security** avec authentification JWT
- **Spring Data JPA** (Hibernate)
- **MySQL 8.0+**
- **Lombok** pour la réduction du code boilerplate
- **SpringDoc OpenAPI 3** (Swagger UI)
- **BCrypt** pour le hashage des mots de passe
- **Maven** pour la gestion des dépendances

---

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Java JDK 17** ou supérieur
  ```bash
  java -version
  ```

- **Maven 3.6+** (ou utilisez le wrapper Maven inclus `./mvnw`)
  ```bash
  mvn -version
  ```

- **MySQL 8.0+**
  ```bash
  mysql --version
  ```

- **Git**
  ```bash
  git --version
  ```

---

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone https://github.com/votre-username/chatop.git
cd chatop
```

---

### 2. Installation de la base de données MySQL

#### Option A : Via la ligne de commande

**Étape 1 : Démarrer MySQL**

Sur macOS :
```bash
# Via Homebrew
brew services start mysql

# Ou via les Préférences Système
# Préférences Système > MySQL > Start MySQL Server

# Ou manuellement
sudo /usr/local/mysql/support-files/mysql.server start
```

Sur Linux :
```bash
sudo systemctl start mysql
```

Sur Windows :
```bash
# Via les Services Windows ou
net start MySQL80
```

**Étape 2 : Se connecter à MySQL**

```bash
mysql -u root -p
```
*Entrez votre mot de passe MySQL root*

**Étape 3 : Créer la base de données**

```sql
-- Créer la base de données
CREATE DATABASE IF NOT EXISTS chatop 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Vérifier que la base est créée
SHOW DATABASES;

-- Utiliser la base de données
USE chatop;

-- Quitter MySQL
exit;
```

**Note importante** : Les tables seront créées automatiquement au premier lancement de l'application grâce à Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

### 4. Compilation et lancement

**Option 1 : Avec Maven wrapper (recommandé)**

```bash
# Nettoyer et compiler
./mvnw clean install

# Lancer l'application
./mvnw spring-boot:run
```

**Option 2 : Avec Maven installé globalement**

```bash
# Nettoyer et compiler
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

**Option 3 : Depuis IntelliJ IDEA**

1. Ouvrez le projet dans IntelliJ
2. Attendez que Maven télécharge les dépendances
3. Cliquez sur le bouton (Run) ou utilisez `Shift + F10`

---

**Vérification du démarrage réussi** 

Si l'application démarre correctement, vous devriez voir dans les logs :

```
INFO  c.c.a.ChatopApplication - Started ChatopApplication in X.XXX seconds
INFO  o.h.e.t.j.p.i.JtaPlatform - HHH000204: Processing PersistenceUnitInfo
INFO  o.h.boot.model.process.spi.Scanner - HHH000412: Hibernate Core
INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed
```

L'application est maintenant accessible sur : **http://localhost:3001**

---

## Documentation de l'API

### Accéder à Swagger UI

Une fois l'application lancée, la documentation interactive de l'API est disponible à l'adresse :

**🔗 [http://localhost:3001/swagger-ui.html](http://localhost:3001/swagger-ui.html)**

Vous pouvez également accéder à la documentation au format JSON :

**🔗 [http://localhost:3001/api-docs](http://localhost:3001/api-docs)**


**Pour tester les routes protégées via swagger :**
1. Inscrivez-vous ou connectez-vous via `/api/auth/register` ou `/api/auth/login`
2. Copiez le token JWT retourné
3. Cliquez sur le bouton **"Authorize"**  en haut à droite
4. Collez le token (sans "Bearer") dans le champ
5. Cliquez sur **"Authorize"**
6. Testez les routes protégées !
