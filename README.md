# Recipe Management Application

This is a Recipe Management application that allows users to manage their favorite recipes. It provides features such as adding, updating, removing, and fetching recipes. Users can also filter available recipes based on various criteria such as vegetarian/non-vegetarian, number of servings, specific ingredients, and text search within the instructions.

## Technologies Used


-Java\
-Spring Boot\
-PostgreSQL\
-Hibernate\
-Swagger\
-Docker

## Getting Started

### Prerequisites
- Java JDK (version X.X.X)
- PostgreSQL (version X.X.X)
- Docker (version X.X.X)

## Installation

- clone the repository

```bash
git clone https://github.com/zoran-repos/recipes.git
```

- navigate to the proper directory

```bash
cd recipe-management
```
- build the project using Maven

```bash
mvn clean install
```

- start PostgreSQL in Docker folder

```bash
docker-compose up -d
```

- run the application

```bash
java -jar target/recipe-management.jar
```
## Usage



```java
Open your web browser and go to http://localhost:8080
```

API documentation (Swagger UI): http://localhost:8080/swagger-ui.html\
\
Use the API endpoints to manage recipes, including adding, updating, removing, and fetching recipes. You can also apply various filters to search for specific recipes.

## License

[MIT](https://choosealicense.com/licenses/mit/)