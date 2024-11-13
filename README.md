The prerequisites for this project are:
- Java 8
- Maven
- IntelliJ IDEA

To generate the project, use the following command:
`mvn clean compile`

To generate the report, use the following command:
`mvn allure:report`

Plugins used:
- Allure
- graphql-codegen-maven-plugin

GraphQL schema file that defines the queries and mutations that can be executed against the SpaceX API
- `src/main/resources/SpaceX.graphql`

Functional testing approach:
- `SpaceXService.java` utility class designed to provide a singleton instance of a GraphQlClient configured to interact with the SpaceX GraphQL API. The class is structured to ensure that the GraphQlClient is only instantiated once, following the singleton design pattern.
- `DataUtils.java` utility class designed to provide a set of helper methods to facilitate checks for dates
- `MyCustomException.java` custom exception class designed to provide a custom exception that can be thrown when an error occurs during the execution of the test suite
- `PocSuite.java` test suite class designed to provide a set of test cases that validate the SpaceX GraphQL API.

Behavior-Driven Development (BDD) approach
- `SpringConfig.java` configuration class designed to provide a set of beans that can be used to configure the Spring context for the test suite
- `CucumberRunner.java` test runner class designed to provide a set of test cases that validate the SpaceX GraphQL API using the Cucumber framework
- `CompanySteps.java` step definition class designed to provide a set of step definitions that can be used to define the behavior of the test cases in the Cucumber feature files
- `CucumberSpringConfiguration.java` configuration class designed to provide a set of beans that can be used to configure the Spring context for the Cucumber test suite
- `poc.feature` File designed to provide a set of scenarios that validate the SpaceX GraphQL API using the Cucumber framework