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
