# SpringDashboard
This project started more with the intention of exploring Spring Boot, making web applications with Java, while building something useful, extensible, maintainable and scalable at the same time, than anything else.  It hasn't turned into a monster... yet.

The idea is to have a dashboard that would monitor and display system statistics across multiple remote machines.  You have the basic system client that you run on the system you want to monitor, which will return JSON via REST web service calls.  Then, you have the client (not yet implemented) that will run any script you want, and return back any custom JSON you want.  The server will read the JSON responses from the client, and give the user a UI from parsing the results.

To run the client: mvn spring-boot:run -Dtype=CLIENT -Dname={system_name}
To run the server: mvn spring-boot:run -Dtype=SERVER -Dname={system_name}
{system_name} is any custom string.  It is used in the JSON response to tell which system the response is monitoring.

## Setup 

### Prerequisites (version used in parenthesis)
- Java JDK (1.8) - for supporting the application
- MySQL (5.7) - for persisting the data
- MongoDB (3.2) - for storing user settings
- Erlang (18.3) - for supporting RabbitMQ
- RabbitMQ (3.6) - for supporting message queue
- Maven (3.0.5) - for building the Java project

### Instructions
- Install prerequisites.
- Download this project
- Build the project by running "mvn clean install" from the terminal
- Extract the distribution zip file from the target directory (should have been generated in the project from running mvn clean install) to the directory where you want to run the dashboard (rundir)
- Make sure RabbitMQ and MySQL are running; they should be automatically running when installed
- Create a new MySQL schema called "dashboard", a new user "dashboard", with password "dashboard"
- Start up MongoDB by running "mongod -dbpath=rundir", where "rundir" is the directory the distribution zip file was extracted to
- Run "java -jar rundir/urim-xxx.jar --type=TYPE --name=NAME", where xxx is the version number, TYPE is either "CLIENT" or "SERVER", and NAME can be any string
- In a browser, navigate to "http://localhost:8080"

### Troubleshooting
- "Access denied to user 'dashboard'@'localhost'" - run "FLUSH PRIVILEGES;" in a MySQL console
 
## Architecture
