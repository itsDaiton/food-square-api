# Food Square
Social network for food lovers.

Food Square was developed as an open source application as a part of bachelor thesis titled ***Design and implementation of an open source web application about cooking*** by David Poslušný at [***Faculty of Informatics and Statistics of Prague University of Economics and Business***](https://fis.vse.cz).

This repository contains source code of the **server side** of the application. 

The other repository that contains the **client side** of the application can be found [here](https://github.com/itsDaiton/food-square).

Live demo of the application can be found here: **https://food-square.site**

## Features

- **Account**
  - create account in the application
  - sign in from multiple devices
  - edit your account information
  - upload a profile picture
  - follow other users
- **Recipes**
  - create your own recipes
    - add ingredients
    - choose different categories
  - display details about recipe
    - meal type
    - preparation/cooking time
    - instructions
    - categories
    - ingredients and their nutritional values
  - share your recipe between other users
  - add recipes to your favorites
  - filter between recipes based on desired criteria
- **Reviews**
  - review other user's recipes
  - display all existing reviews
  - sort between best/worst rated recipes
  - show reviews for a specific recipe
- **Comments**
  - add multiple comments to recipes
- **Ingredients**
  - display all available ingredients
  - look up specific ingredients
- **Liking**
  - like a comment or a review
- **Meal planning**
  - get a generated meal plan based on:
    - categories
    - amount of meals
    - desired calories
  - save your meal plan
- **Theming**
  - switch between light and dark mode
## Built With

### Programming language
- [Java 8](https://www.oracle.com/cz/java/technologies/javase/javase8-archive-downloads.html)

### Framework
- [Spring Boot](https://spring.io/projects/spring-boot)

### Dependency management
- [Maven](https://maven.apache.org)

### Additional dependencies
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - for building JPA based repositories
- [Spring Web](https://mvnrepository.com/artifact/org.springframework/spring-web) - for setting up http client and other web oriented features
- [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql) - for connecting to PostgreSQL database
- [Lombok](https://projectlombok.org) - for automated class building and removing boilerplate code
- [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test) - for testing and debugging
- [Spring Security](https://spring.io/projects/spring-security) - for securing various points of the application
- [JSON Web Token](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt) - for JWT support for JVM
- [Spring Boot Configuration Processor](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-configuration-processor) - for building configuration classes
- [Spring Boot Starter Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation) - for class and API input validation
- [Spring Batch](https://spring.io/projects/spring-batch) - for reading and processing external files
- [springdoc](https://springdoc.org) - for automatic generation of API documentation


## Get Started

### Prerequisites

- [Maven](https://maven.apache.org)
- IDE (e.g. [IntelliJ IDEA](https://www.jetbrains.com/idea/)) or terminal

### Installation

Clone the project

```
git clone https://github.com/itsDaiton/food-square-api.git
```

Open the project

```
cd food-square-api
```

Configure required variables located in `src/main/resources/application.properties`

```
spring.datasource.url=''
spring.datasource.username=''
spring.datasource.password=''

app.jwt.secret-key=''
app.jwt.expiration-time=''
app.jwt.cookie-name=''
app.jwt.secure=''
app.jwt.sameSite=''
```

#### spring.datasource.url

- URL to your database, which the application is going to connect to

#### spring.datasource.username

- your username in the database

#### spring.datasource.password

- your password to the database

#### app.jwt.secret-key

- secret used to sign JWT tokens

#### app.jwt.expiration-time

- expiration time on JWT token in milliseconds
- it is recommended to choose at least 1 day (86400000)

#### app.jwt.cookie-name

- name of the cookie used for user authentication
- example: 'examplecookiename'

#### app.jwt.secure

- defines whether cookies will have secure attribute or not
- choose 'false' for local development
- must be set to 'true' in production!

#### app.jwt.sameSite

- defines whether cookies will have samesite attribute or not
- choose 'lax' for local development
- must be set to 'none' in production!

Run the application in terminal

```
mvnw spring-boot:run
```

Run the service with curl (in a separate terminal window)

```
curl localhost:8080
```

If you wish, you can create a executable JAR file

```
mvnw clean install
```

### Deployment
- Create a feature branch for your changes
- Deploy your code
- After your development is done, create a pull request

## Contributting

Contributions, issues, and feature requests are welcome. If you would like to contribute to this project, please see `CONTRIBUTING`.

## License

Project is distributed under the MIT License. See `LICENSE` for more information.

## Testing

Application testing was carried out using the [***Postman***](https://www.postman.com) software. Collection of all tests can be found under this directory
```
docs/tests
```

## Contact

You can contact the author of the project under this [e-mail](mailto:david.poslusny@gmail.com).

## Acknowledgments

- Ing. Vojtěch Růžička 
