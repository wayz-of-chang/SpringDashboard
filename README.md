# SpringDashboard
This project started more with the intention of exploring Spring Boot, making web applications with Java, while building something useful, extensible, maintainable and scalable at the same time, than anything else.  It hasn't turned into a monster... yet.

The idea is to have a dashboard that would monitor and display system statistics across multiple remote machines.  You have the basic system client that you run on the system you want to monitor, which will return JSON via REST web service calls.  Then, you have the client (not yet implemented) that will run any script you want, and return back any custom JSON you want.  The server will read the JSON responses from the client, and give the user a UI from parsing the results.

To run the client: mvn spring-boot:run -Dtype=CLIENT -Dname={system_name}
To run the server: mvn spring-boot:run -Dtype=SERVER -Dname={system_name}
{system_name} is any custom string.  It is used in the JSON response to tell which system the response is monitoring.

Prerequisites (version used in parenthesis)
- Java JDK (1.8)
- MySQL (5.7)
- MongoDB (3.2)
- Erlang (18.3)
- RabbitMQ (3.6)
