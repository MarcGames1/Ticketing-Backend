# Java Backend Documentation

## Users

- FName
- LName
- Department
- Email
- Role => ENUM
- Password
- (AWS Cognito) - Service for authentication

### User Roles:

- SuperUser
   - Has access to all app-level functions:
      - Can CRUD:
         - Departments
         - Users
         - Tickets

- Supervisor
   - Has access only to his department users
   - Can CRUD users on his department level

- Assignee (Worker)

  Can change status for the ticket

## Ticket Entity

- Created By => UserId
- Assigned to => UserId
- AttachId: [] (Allow NULL)
- Title => String
- Content => String
- Status - ENUM {**new**, **assigned**, **started**, **in progress**, **checked**, **DONE** }


 ### TODO
 1. Setup AWS SDK for Java 2.x ( https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html)
