# Spring Boot JPA H2 example with Maven

## Database

This project uses H2 in memory database.
Initial data will be loaded on startup.


Relationship Details:
Item to Prices:

item 1:1 → one_time_price (one item has exactly one one-time price)

item 1:N → recurring_price (one item can have multiple recurring prices)

Cart Relationships:

cart 1:N → cart_content (one cart can have multiple items)

cart_content N:1 → item (each content entry points to one item)

Reference Tables:

actions - Defines possible cart actions

time_frame - Defines duration units (day/month/year) for recurring prices

## Run Spring Boot application
```
mvn spring-boot:run
```

