# users
Microservice to manage users. The main functions of this Service is to register and validate the users.

# Access to the service
The service can be accessed as follows:

## REGISTER: 
- URL : http://localhost:8082/api/users/register

```
Sample Request:
{
    "name": "Gaurav Kumar",
    "email": "gaurav@gmail.com",
    "password": "Hello"
}
```

## Validate:
- URL: http://localhost:8082/api/users/validate
```
Sample Request:
{
    "token": "f621f84b-d3f1-4dab-bcf5-6cbd7746a8bf"
}

```
## Set up

- Clone the project on you local machine IDE(Eclipse or IntelliJ)
- Build the Project
- Start the project

## More Information
[READ ME FOR DETAILS](https://github.com/gaurravkumar/staticArtifacts/blob/main/BID%20SYSTEM.docx)

