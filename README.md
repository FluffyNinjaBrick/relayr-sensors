# Sensor API
## Startup
*In everything described below, replace the <brackets\> with what's described in them.*

The application is built with Java 11, you'll need that JDK to run it.</br>
To build the app with maven you will also need to have your JAVA_HOME set to that JDK's folder.</br>
The application communicates through port 8080. It will not work if said port is occupied.

The program can be run in three ways:
* from the source code, by manually running the main method in the ApiApplication class
* using the executable .jar, via the command java -jar <path-to-jar\> <config-url\>
* with **Docker** - described below.

### Running with Docker
In the */src* folder, open up a terminal and execute the following:
* *mvnw package -DskipTests* - generates an executable .jar file in */src/target*. Remove the *-DskipTests* option if you want to also run tests.
* *docker build -t api .* - creates a Docker image called "api" from the .jar file
* *docker run -p8080:8080 -d --name="Sensor_API" --rm api <config-url\>* - starts the application and makes it available on port 8080 of the host machine.
  This command starts the container in a detached state, so no console output will be visible. If you want to see the console, remove the *-d* option from the command.

When you're done, execute *docker stop Sensor_API* to shut down the application. The container will be automatically removed.

## Tests
There are two ways to run the tests for the application:
* from the source code, by manually running the methods in the */src/test* directory
* with maven, via the *mvnw test* command

As mentioned above, *mvnw package* without the *-DskipTests* option will also execute all the tests.

To briefly comment on the tests themselves, they only cover non-trivial functionality. Things like getters, setters
and functions which only call other logic and return the result are not tested, as their outcome is easily predictable.
That last group includes the API endpoint methods.

## API
The final API provides four methods. Firstly, there are the two specified in the task description:
* *GET http://localhost:8080/engines?minPressure=<value\>&maxTemperature=<value\>* - returns a list of malfunctioning engines, in the form of a list of engine IDs
* *POST http://localhost:8080/sensors/<sensor-id\>* - updates the sensor specified by *<sensor-id\>* according to the instructions in the body. The body is a small JSON file, as follows:
<pre>
{
    "operation": "SET",
    "value": 60
}
</pre>
Where *"operation"* is *SET*, *INCREMENT* or *DECREMENT*. The field **is case sensitive**, so make sure it's all caps. The *"value"* field is simply an integer.

The other two methods were added for ease of verification and debugging:
* *GET http://localhost:8080/sensors* - returns a list of all the sensors in the system
* *GET http://localhost:8080/sensors/<sensor-id\>* - returns the sensor specified by *<sensor-id\>*



