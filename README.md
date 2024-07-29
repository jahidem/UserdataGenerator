## UserdataGenerator: Fake User Data Generation Web App

This project implements a Spring Boot web application for generating realistic fake user data with various customization options.

### Features

* Select from multiple regions (e.g., USA, Poland, France)
* Specify the number of errors per record (0-1000) with a slider and linked input field.
* Generate random seed or enter a custom one for reproducible results.
* View generated data in a table with infinite scrolling (20 records per page).
* Generated data includes:
    * Name (first, middle, last) based on chosen region
    * Address (formatted according to region)
    * Phone number (formatted according to region)
    * Optionally introduce random errors (deletion, insertion, swap) in generated data.
* Export generated data to CSV format.
### Demonstration

### Requirements

* Java 11+
* Docker (for deployment)

### Running the application

**Using Docker:**

1. Build the docker image:

```
docker build -t userdatagenerator .
```

2. Run the application:

```
docker run -p 8000:8000 userdatagenerator
```

**Without Docker:**

1. Ensure you have Maven installed.
2. Navigate to the project directory.
3. Build the application:

```
mvn clean package
```

4. Run the application:

```
java -jar target/UserdataGenerator-0.0.1-SNAPSHOT.jar
```

The application will be accessible at http://localhost:8000/home by default.

### Usage

1. Select a desired region.
2. Choose the number of errors per record using the slider or input field.
3. Optionally set a custom seed or generate a random one.
4. Click the "Generate" button.
5. The table will display 20 records of generated user data. Scroll down to automatically load additional pages.
6. You can export the displayed data to CSV format using the "Export to CSV" button (optional).

### Deployment

The included Dockerfile allows easy deployment to a container platform like Heroku or Render.  You can configure your chosen platform to use the `userdatagenerator` image built by the Dockerfile. 
