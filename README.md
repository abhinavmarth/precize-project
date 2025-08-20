# precize-project

# 🛒 Order Management System

A simple **Java 17** project that demonstrates event-driven order processing using the **Observer Pattern**.  
The system reads events from a JSON file, updates order states, and notifies registered observers such as **Logger** and **Alert**.

---

## 📌 Features
- Event-driven architecture using `EventProcessor`.
- Order lifecycle management with predefined states:
  - `PENDING`, `PARTIALLY_PAID`, `PAID`, `SHIPPED`, `CANCELLED`.
- Observer pattern implementation for notifications:
  - `LoggerObserver` – logs status changes.
  - `AlertObserver` – raises alerts on important updates.
- JSON-based event input (`events.json`).
- Uses **SLF4J + Logback** for structured logging.

---

## 📂 Project Structure
com.precize.orders
│
├── OrdersApplication.java # Main entry point
├── observer/
│ ├── OrderObserver.java # Observer interface
│ ├── LoggerObserver.java # Logs order updates
│ └── AlertObserver.java # Alerts on critical events
├── processor/
│ └── EventProcessor.java # Processes events and notifies observers
├── model/
│ ├── Order.java # Order entity
│ └── OrderStatus.java # Enum for order states
└── resources/
└── events.json # Input event file

yaml
Copy
Edit

---

## ⚙️ How It Works
1. The application starts with `OrdersApplication`.
2. `EventProcessor` loads events from `events.json`.
3. Orders are created or updated based on the event type.
4. Observers (`LoggerObserver`, `AlertObserver`) are notified of state changes.
5. Logs are written using **SLF4J** with **Logback**.

---

## 🚀 Running the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/order-management-system.git
Build and run using Maven/Gradle or directly from an IDE:

bash
Copy
Edit
mvn clean install
mvn exec:java -Dexec.mainClass="com.precize.orders.OrdersApplication"
Ensure src/main/resources/events.json exists with sample order events.

🧪 Testing
Includes JUnit 5 tests for:

Order lifecycle: create → pay → ship → cancel.

Observer notification checks.

Handling unknown event types.

Run tests:

bash
Copy
Edit
mvn test
✨ Notes
This is a learning-oriented project to showcase Java event-driven design.

Demonstrates Observer Pattern and structured logging.

---
