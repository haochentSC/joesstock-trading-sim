# JoesStock Trading Simulator

JoesStock is a Java servlet-based web application that simulates stock trading. It lets users register and log in, retrieve real-time quotes through the [Finnhub API](https://finnhub.io), buy and sell shares, and review their holdings.

## Features
- **User authentication** – register new accounts and log in via HTTP sessions.
- **Real-time quote search** powered by the Finnhub public API.
- **Buy and sell orders** with balance checks and portfolio tracking.
- **Portfolio overview** displaying aggregated holdings by ticker.
- **Session management** for secure logout and access control.

## Technologies
- Java Servlets (Jakarta Servlet API)
- MySQL database
- Gson for JSON serialization
- Finnhub public API
- Vanilla HTML, CSS, and JavaScript front end

## Project Structure
```
src/
  main/
    java/assignment4/      # Servlets, models, JDBC connector
    webapp/                # Static HTML and client-side JS
build/                     # Compiled classes
```

## Setup
1. **Prerequisites**
   - JDK 11+
   - MySQL server
   - Servlet container such as Apache Tomcat 10
   - Finnhub API key

2. **Database**
   Create a database named `assignment4database` with tables `users` and `portfolio`. Example schema:
   ```sql
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(255) UNIQUE NOT NULL,
       uPassword VARCHAR(255) NOT NULL,
       email VARCHAR(255) UNIQUE NOT NULL,
       balance DOUBLE NOT NULL
   );

   CREATE TABLE portfolio (
       trade_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT NOT NULL,
       ticker VARCHAR(10) NOT NULL,
       numStock INT NOT NULL,
       price DOUBLE NOT NULL,
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );
   ```

3. **Build**
   - Place `gson-2.9.1.jar`, `mysql-connector-j-8.3.0.jar`, and the Servlet API JAR in `src/main/webapp/WEB-INF/lib/`.
   - Compile the Java sources:
     ```bash
     javac -cp "gson-2.9.1.jar:mysql-connector-j-8.3.0.jar:servlet-api.jar" \
           -d build/classes $(find src/main/java -name "*.java")
     ```

4. **Deploy**
   - Package the compiled classes and web resources into a WAR file or copy them into a servlet container's webapps directory.
   - Start the container and navigate to `http://localhost:8080/Home.html`.

## API Endpoints
| Servlet | Method | Path | Purpose |
|---------|--------|------|---------|
| `RegisterServlet` | `POST` | `/RegisterServlet` | Register a new user |
| `RegisterServlet` | `GET` | `/RegisterServlet` | Log in a user |
| `TradeServlet` | `GET` | `/TradeServlet` | Fetch user portfolio |
| `TradeServlet` | `POST` | `/TradeServlet` | Buy shares |
| `TradeSellServlet` | `GET` | `/TradeSellServlet` | Retrieve cash balance |
| `TradeSellServlet` | `POST` | `/TradeSellServlet` | Sell shares |
| `LogoutServlet` | `POST` | `/LogoutServlet` | End the session |

## Usage
1. Open `LoginRegister.html` to create an account or log in.
2. After logging in, use `LoggedinHome.html` to search for quotes and place buy orders.
3. Visit `PortfolioPage.html` to review holdings and execute additional buy or sell actions.
4. Click **Logout** to terminate the session.

---
This project is intended for educational purposes and does not represent financial advice.
