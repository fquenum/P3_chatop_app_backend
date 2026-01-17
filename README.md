# ChâTop - Spring Boot Backend API

Secure REST API for rental property management, developed with Spring Boot 3, Spring Security and JWT authentication.

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.x**
- **Spring Security** with JWT authentication
- **Spring Data JPA** (Hibernate)
- **MySQL 8.0+**
- **Lombok** for boilerplate code reduction
- **SpringDoc OpenAPI 3** (Swagger UI)
- **BCrypt** for password hashing
- **Maven** for dependency management

---

## Prerequisites

Before starting, make sure you have installed:

- **Java JDK 17** or higher
  ```bash
  java -version
  ```

- **Maven 3.6+** (or use the included Maven wrapper `./mvnw`)
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

## Installation

### 1. Clone the project

```bash
git clone https://github.com/your-username/chatop.git
cd chatop
```

---

### 2. MySQL Database Installation

#### Option A: Via command line

**Step 1: Start MySQL**

On macOS:
```bash
# Via Homebrew
brew services start mysql

# Or via System Preferences
# System Preferences > MySQL > Start MySQL Server

# Or manually
sudo /usr/local/mysql/support-files/mysql.server start
```

On Linux:
```bash
sudo systemctl start mysql
```

On Windows:
```bash
# Via Windows Services or
net start MySQL80
```

**Step 2: Connect to MySQL**

```bash
mysql -u root -p
```
*Enter your MySQL root password*

**Step 3: Create the database**

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS chatop 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify the database is created
SHOW DATABASES;

-- Use the database
USE chatop;

-- Exit MySQL
exit;
```

**Important note**: Tables will be created automatically on first application launch thanks to Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

---

### 4. Compilation and Launch

**From IntelliJ IDEA**

1. Open the project in IntelliJ
2. Wait for Maven to download dependencies
3. Click the  (Run) button or use `Shift + F10`

---

**Successful startup verification** 

If the application starts correctly, you should see in the logs:

```
INFO  c.c.a.ChatopApplication - Started ChatopApplication in X.XXX seconds
INFO  o.h.e.t.j.p.i.JtaPlatform - HHH000204: Processing PersistenceUnitInfo
INFO  o.h.boot.model.process.spi.Scanner - HHH000412: Hibernate Core
INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed
```

The application is now accessible at: **http://localhost:3001**

---

## API Documentation

### Access Swagger UI

Once the application is launched, the interactive API documentation is available at:

**🔗 [http://localhost:3001/swagger-ui.html](http://localhost:3001/swagger-ui.html)**

You can also access the documentation in JSON format:

**🔗 [http://localhost:3001/api-docs](http://localhost:3001/api-docs)**

**To test protected routes via Swagger:**

1. Sign up or log in via `/api/auth/register` or `/api/auth/login`
2. Copy the returned JWT token
3. Click the **"Authorize"** button in the top right
4. Paste the token (without "Bearer") in the field
5. Click **"Authorize"**
6. Test the protected routes!
