# Maxx Toy Shop Backend

Spring Boot + MySQL backend for the Angular Maxx Toy Shop.

## Requirements
- Java 21
- Maven 3.9+
- MySQL 8+

## Database
Create the database once:

```sql
CREATE DATABASE maxx_toy_shop;
```

Do not create tables manually. Spring Data JPA/Hibernate creates them.

If your MySQL root password is not `root`, set it before starting:

```powershell
$env:DB_PASSWORD="YourPassword"
```

or edit `src/main/resources/application.properties`.

## Start

```powershell
mvn spring-boot:run
```

Backend runs on:

http://localhost:8080

Health:

http://localhost:8080/api/health

Products:

http://localhost:8080/api/products

## Data stored in MySQL
The application creates:
- users
- categories
- products
- orders
- order_items

The first startup seeds your 10 Maxx Toy Shop products.

When checkout is connected to this API, the order and customer details are stored in MySQL and product stock is reduced.

## API
GET /api/products
GET /api/products/{id}
GET /api/products?category=Educational
GET /api/products?search=lego
POST /api/products
PUT /api/products/{id}
DELETE /api/products/{id}
GET /api/categories
POST /api/users/register
POST /api/orders
GET /api/orders
GET /api/orders/customer?email=...
GET /api/orders/{id}

## Next step
The current Angular app still uses local product/cart data. Connect Angular to these APIs next with an Angular service and HttpClient so registration and checkout write to MySQL.
