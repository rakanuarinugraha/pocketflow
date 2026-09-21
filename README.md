# 💰 PocketFlow API

A robust personal finance and budget tracking RESTful backend built with **Java 21**, **Spring Boot 4**, and **PostgreSQL**. Designed with strict **3-Tier Layered Architecture**, relational entity mapping, declarative bean validation, automated financial analytics, and dynamic threshold-based budget alerts.

---

## 🌟 Key Features & Engineering Highlights

* **3-Tier Layered Architecture:** Clear decoupling of responsibilities across **Web Presentation** (`@RestController`), **Domain & Business Logic** (`@Service`), and **Data Access Layer** (`JpaRepository`).
* **JPA Relational Mapping (`@ManyToOne`):** Modeled relational database associations linking `Transaction` records and `Budget` caps back to parent `Category` entities using lazy fetching strategies.
* **Declarative Input Validation:** Enforces strict data hygiene at the controller boundary using Bean Validation (`@Valid`, `@NotBlank`, `@Positive`, `@Min`, `@Max`, `@Size`).
* **Financial Analytics & Cashflow Aggregation:** Dynamically calculates total income, total expenses, net balance, and expense breakdown percentages per category using functional Java Streams (`Collectors.groupingBy`, `reduce`, `BigDecimal` arithmetic).
* **Threshold Alert System (Budgeting):** Evaluates real-time monthly spending against configured budget caps to automatically compute status indicators:
    * 🟢 **`SAFE`** (Spending $\le 80\%$)
    * 🟡 **`WARNING`** (Spending between $80\%$ and $100\%$)
    * 🔴 **`EXCEEDED`** (Over-budget / Spending $> 100\%$)
* **Containerized Persistence:** Managed PostgreSQL 16 instance orchestrated via **Docker Compose**.

---

## 🛠️ Tech Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Java 21 | Modern LTS Java runtime |
| **Framework** | Spring Boot 3.3+ | Spring Web MVC, Spring Data JPA, Spring Validation |
| **Database** | PostgreSQL 16 | Relational database (ACID compliant) |
| **ORM** | Hibernate ORM | Automated schema generation and relational mapping |
| **Libraries & Tooling** | Lombok, Maven | Boilerplate reduction and dependency build lifecycle |
| **Infrastructure** | Docker & Docker Compose | Isolated local database environment |

---

## 🏛️ System Architecture

```
[ Client / Postman / Mobile / Frontend ]
                   │
                   ▼ HTTP REST (JSON)
┌─────────────────────────────────────────────────────────────┐
│  Spring Boot Backend Application                            │
│                                                             │
│  1. Controller Layer  ──► Route Mapping & Request Validation│
│  2. Service Layer     ──► Business Logic & Financial Calc   │
│  3. Repository Layer  ──► Spring Data JPA Data Access       │
└──────────────────────────────┬──────────────────────────────┘
                               │
                       (Spring Data JPA)
                               ▼
                    ┌──────────────────────┐
                    │  PostgreSQL (Docker) │
                    │  Port: 5434          │
                    │  [pocketflow_db]     │
                    └──────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK 17 or 21)**
* **Docker Desktop** (Engine running)
* **Git**

### 1. Clone the Repository
```bash
git clone https://github.com/rakanuarinugraha/pocketflow.git
cd pocketflow
```

### 2. Start PostgreSQL Container
Run the Docker Compose service in background:
```bash
docker compose up -d
```
Verify the container is healthy:
```bash
docker compose ps
```

### 3. Run the Spring Boot Application
Navigate to the `backend` directory and run via the Maven wrapper:
```bash
cd backend
./mvnw spring-boot:run
```
The application will start on: `http://localhost:8081`

---

## 📡 API Reference

### 1. Category Management (`/api/categories`)
* `GET /api/categories` - Fetch all categories.
* `GET /api/categories?type=EXPENSE` - Filter categories by type (`INCOME` or `EXPENSE`).
* `GET /api/categories/{id}` - Fetch single category by ID.
* `POST /api/categories` - Create a new category.
```json
{
  "name": "Food & Beverages",
  "description": "Daily meals and coffee",
  "type": "EXPENSE"
}
```
* `DELETE /api/categories/{id}` - Delete category by ID.

---

### 2. Transaction Management (`/api/transactions`)
* `GET /api/transactions` - Fetch all recorded transactions with category details.
* `GET /api/transactions/{id}` - Fetch single transaction by ID.
* `POST /api/transactions` - Record a new transaction.
```json
{
  "amount": 25000.00,
  "description": "Lunch Nasi Padang",
  "categoryId": 1
}
```
* `DELETE /api/transactions/{id}` - Delete transaction by ID.

---

### 3. Financial Reports & Analytics (`/api/reports`)
* `GET /api/reports/summary` - Get aggregated balance, total income, total expense, and category expense percentage breakdown.
* *Optional query parameters for date range:* `/api/reports/summary?startDate=2026-09-01&endDate=2026-09-30`

**Sample Response (`200 OK`):**
```json
{
  "totalIncome": 8000000.00,
  "totalExpense": 125000.00,
  "netBalance": 7875000.00,
  "totalTransactions": 4,
  "categoryBreakdown": [
    {
      "categoryId": 1,
      "categoryName": "Food & Beverages",
      "totalAmount": 50000.00,
      "percentage": 40.0
    },
    {
      "categoryId": 3,
      "categoryName": "Transportation",
      "totalAmount": 75000.00,
      "percentage": 60.0
    }
  ]
}
```

---

### 4. Budget Tracking (`/api/budgets`)
* `GET /api/budgets?month=9&year=2026` - View all budgets for a given month/year with actual spent, remaining balance, and status alert.
* `POST /api/budgets` - Set or update budget limit for an expense category.
```json
{
  "categoryId": 1,
  "monthlyLimit": 500000.00,
  "month": 9,
  "year": 2026
}
```

**Sample Response (`201 Created`):**
```json
{
  "id": 1,
  "categoryId": 1,
  "categoryName": "Food & Beverages",
  "month": 9,
  "year": 2026,
  "monthlyLimit": 500000.00,
  "actualSpent": 50000.00,
  "remainingBudget": 450000.00,
  "usagePercentage": 10.0,
  "status": "SAFE"
}
```

---

## 👤 Author

* **Rakanuari Dwi Nugraha**
    * LinkedIn: [linkedin.com/in/rakanuari-dwi-nugraha](https://linkedin.com/in/rakanuari-dwi-nugraha)
    * GitHub: [@rakanuarinugraha](https://github.com/rakanuarinugraha)