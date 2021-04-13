# Sensor API
## Startup
*In everything described below, replace the <brackets\> with what's described in them.*

The application is built with Java 11, you'll need that JDK to run it.</br>
To build the app with maven you will also need to have your JAVA_HOME set to that JDK's folder.</br>
The application communicates through port 8080. It will not work if said port is occupied.

**Please note:** the URL to the configuration file has to be the address of the file itself. A github link, like the one given <br/>
in the task, **will not work**. If you want to use a file from github, click the "raw" button on the site to display the file itself,<br/>
then use that URL.

The */api* folder is the root of an IntelliJ project. If you want to look at the code using that IDE, you can simply open it as a project.

The app can be run in three ways:
* from the source code, by manually running the main method in the ApiApplication class
* using the executable .jar, via the command *java -jar <path-to-jar\> <config-url\>*
* with **Docker** - described below.

### Running with Docker
In the */api* folder, open up a terminal and execute the following:
* *mvnw package -DskipTests* - generates an executable .jar file in */api/src/target*. Remove the *-DskipTests* option if you want to also run tests.
* *docker build -t api .* - creates a Docker image called "api" from the .jar file
* *docker run -p8080:8080 -d --name="Sensor_API" --rm api <config-url\>* - starts the application and makes it available on port 8080 of the host machine.
  This command starts the container in a detached state, so no console output will be visible. If you want to see the console, remove the *-d* option from the command.

When you're done, execute *docker stop Sensor_API* to shut down the application. The container will be automatically removed.

## Tests
There are two ways to run the tests for the application:
* from the source code, by manually running the methods in the */api/src/test/...* directory
* with maven, via the *mvnw test* command

As mentioned above, *mvnw package* without the *-DskipTests* option will also execute all the tests.

To briefly comment on the tests themselves, they only cover non-trivial functionality. Things like getters, setters
and functions which only call other logic and return the result are not tested, as their outcome is easily predictable.
That last group includes the API endpoint methods.

## API
The final API provides four endpoints. Firstly, there are the two specified in the task description:
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

If something goes wrong, the appropriate error message will be seen in the response body. For example, this will be the case when we pass a non-existent ID through the update/get sensor requests, or when the update request would result in a value that lies outside of the sensor's boundaries.



