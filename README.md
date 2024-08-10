# Cafeteria manangment system - Backend part

Frontend for the project is here: https://github.com/piratinskii/SCECafe-frontend

## Project Description

This is my college project and my first ever fullstack RESTFul application. I wrote it using JAVA and Spring for the backend and React for the frontend. As database I used PostgreSQL.

In this project you can:
- Add/remove/change items into your menu as admin
- Order the items as user
- Mark items and orders as done as barista.

## Installation 

1. Clone the repository (use these commands in the terminal):    
   ```git clone https://github.com/piratinskii/SCECafe-backend.git```
   
   ```cd SCECafe-backend``` 

3. Set the database information (This project works with PostgreSQL):
   For linux:
   ```
   export DB_URL=jdbc:postgresql://localhost:5432/postgres
   export DB_USERNAME=postgres
   export DB_PASSWORD=postgres
   ```

    For Windows:
   ```
   $env:DB_URL="jdbc:postgresql://localhost:5432/postgres"
   $env:DB_USERNAME="postgres"
   $env:DB_PASSWORD="admin"
   ```

   Of course, change all information to your credentials.
   
5. Build the project using gradle:
   Linux:
   ```./gradelew build```

   Windows:
   ```Just start gradelew.bat file```

## Usage 

Run the server: 

```./gradelew bootrun```

and use this frontend: https://github.com/piratinskii/SCECafe-frontend
