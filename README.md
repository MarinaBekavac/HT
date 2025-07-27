# Spring Boot JPA H2 example with Maven

## Database

This project uses H2 in memory database.
Initial data will be loaded on startup.




## Run Spring Boot application

### Testing with existing data


#### Get cart

Gets active shopping cart for userID.

URL: cart/getShoppingCart<br />
Method: GET<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
        "sourceId": "ITEM-001",
        "name": "Laptop",
        "description": "High-performance laptop",
        "userId": "user123",
        "transactionId": null,
        "quantity": 1,
            "price": {
            "itemId": null,
            "oneTimePrice": 1300
        }
    }
```
Response<br />
```json
{
    "items": [
        {
            "identifier": "Laptop",
            "itemPrice": {
                "itemId": null,
                "oneTimePrice": 1300,
                "recurringPrice": null
            },
            "action": "add",
            "quantity": 2
        }
    ]
}
```

#### Get statistics


URL: cart/statistics<br />
Method: GET<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
      "sourceId": "ITEM-001",
      "actionId": 1,
      "dateFrom": "2025-01-01",
      "dateTo": "2026-12-31"
    }
```
Response<br />
```json
    {
      "count": 5
    }
```


#### Delete item from cart


URL: cart/deleteItem<br />
Method: DELETE<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
      "userId": "user123",
      "sourceId": "ITEM-001"
    }
```
Response<br />
```json
    {
      "count": 5
    }
```

#### Add item

URL: cart/addItem<br />
Method: POST<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
        "sourceId": "ITEM-001",
        "name": "Laptop",
        "description": "High-performance laptop",
        "userId": "user123",
        "transactionId": null,
        "quantity": 1,
            "price": {
            "itemId": null,
            "oneTimePrice": 1300
        }
    }
```
Response<br />
```json
{
    "items": [
        {
            "identifier": "Laptop",
            "itemPrice": {
                "itemId": null,
                "oneTimePrice": 1300,
                "recurringPrice": null
            },
            "action": "add",
            "quantity": 2
        }
    ]
}
```

#### Buy

Buys all items from cart. Returns bought cart.

URL: cart/buy<br />
Method: POST<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
      "userId": "user123"
    }
```
Response<br />
```json
    {
      "items": [
        {
          "identifier": "Laptop",
          "itemPrice": {
            "itemId": null,
            "oneTimePrice": 1300,
            "recurringPrice": null
          },
          "action": "add",
          "quantity": 2
        }
      ]
    }
```


#### Evict cart

Removes all items from cart

URL: cart/evictCart<br />
Method: POST<br />
Consumes: application/json<br />
Produces: application/json<br />

Request<br />
```json
    {
      "userId": "user123"
    }
```
Response<br />
```json
    {
      "items": null
    }
```



```
mvn spring-boot:run
```

