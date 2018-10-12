#Hotel Room Management Coding Task

## Implementation details
The application is built as spring-boot service.
There are 2 main parts in the project: the logic of room usage calculation and REST API for getting the usage results.

### Room Usage Calculation Service
The algorithm for usage calculation is straightforward:
 * Fill the premium rooms
 * Check if there is place for the upgrades
 * If yes, add the upgraded users from economy to premium rooms
 * Fill the economy rooms
 
I used maps and list as the data structures for input and output, to make the service API extandable: if the service should support the 3rd room type (say, Luxury), it may be achieved by update of service logic, but without changing the API signature

### REST API
The API is versioned as "v1" for future extendability.
The RoomUsageController defines the API, and responsible for parsing the input parameters and building the output structure. The business logic is handled in AvailabilityCalculationService. The input parameters are provided as URL query parameters, the response is built into JSON structure.

It's open question, if the list of willingToPay amounts should the input of the API or should be stored as internal data. I decided to provide the willing to pay amounts as the input to the API. In order to send the list as query parameters, you should use willingToPay parameter multiple times, once for each guest (see example below)

## Testing
I created the unit tests for Service level, using the test data provided as part of the assignment, adding some of additional scenarios for the edge cases. 
Also created unit tests for non-trivial methods in model classes. The integration tests are needed, but I didn't have time to implement them properly.
As all the business logic is in service class, that has unit tests, it should be enough for functionality tests.

## Used frameworks and libraries
I implemented the task, as the spring-boot service.
slf4j is used for logging and lombock library for easy logging setup.
Maven is used for build and running the service

## Build and Execution
To build the application run "mvn clean install" command.
To start it, run "mvn spring-boot:run", after the successful start up, you can issue API calls as follows:
* GET http://localhost:8080/rooms/usage?freeEconomy={freeEconomyRooms}&freePremium={freePremiumRooms}&willingToPay={paymen1}&willingToPay={payment2}...&willingToPay={paymentN}

Examples:

   INPUT: http://localhost:8080/rooms/usage?freeEconomy=1&freePremium=2&willingToPay=10&willingToPay=110  
   RESPONSE: [{"roomType":"PREMIUM","roomsUsage":1,"income":110},{"roomType":"ECONOMY","roomsUsage":1,"income":10}]
 
   INPUT: http://localhost:8080/rooms/usage?freeEconomy=0&freePremium=2&willingToPay=10&willingToPay=110  
   RESPONSE: [{"roomType":"PREMIUM","roomsUsage":2,"income":120},{"roomType":"ECONOMY","roomsUsage":0,"income":0}]
   