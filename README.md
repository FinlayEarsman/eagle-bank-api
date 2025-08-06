# Eagle Bank REST API

### REST API for Eagle Bank, which allows authorised users to create and manage accounts and transactions.
### Stack: Java Spring Boot

### Features:
- User authentication
- User creation and management (create, fetch, update, delete)
- Account management (create, fetch, update, delete)
- Transaction management (create, fetch)
- Integration tests for API endpoints


### Decision-Making
1. **Authentication Endpoint**: Auth endpoint created at `/api/auth` for user authentication following creating a user. Implemented using Spring Security with JWT for stateless authentication.
2. **Data Modeling**: Used JPA to manage entities like User, Account, and Transaction within an H2 database to reduce time to set up.
3. **Testing**: Employed JUnit and Mockito for tests to ensure reliability of key controller endpoints, more unit testing required but due to time constraints I have prioritised full integration tests.
4. **Documentation**: OpenAPI documentation to facilitate easy understanding and usage of the API endpoints.
5. **Prod Security**: Due to time constraints, I have not implemented production security measures such as external secrets, or advanced logging.


### Difficulties
1. **Time Constraints**: Limited time to implement all features, leading to prioritisation of core functionalities and integration tests over extensive unit tests.
4. **Security**: Implementing JWT authentication and ensuring secure handling of sensitive data within the constraints of the project timeline.


### Still to Do
1. **Unit Tests**: Add more unit tests for individual components to ensure robustness.
2. **Robust Error Handling**: Improve error handling through implementing further response types, and give more accurate error messages, to provide more informative responses.
3. **Input Validation**: Implement further input validation such as checking formats and apply regex where applicable.