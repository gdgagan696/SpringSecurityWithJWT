# SpringSecurityWithJWT
Implementing Spring Security with JWT and Secure Rest APIs.

Before running please create database names jwtdemo.
Start application with port 8020.

Open postman and go to following endpoints: 

1)http://localhost:8020/users/createUser for creating a new user.

![alt text](./Screenshots/Signup.png?raw=true "Create New User")


2)http://localhost:8020/users/authenticate for authenticating existing user and get JWT token.

![alt text](./Screenshots/LoginandJwt.png?raw=true "Authenticate Existing User")


3)http://localhost:8020/users/getUser for getting existing user info.Pass JWT token in authorization header.

![alt text](./Screenshots/GetUser.png?raw=true "User Info


4)http://localhost:8020/users/getUser for getting existing user info.If JWT not passed then error will be thrown.

![alt text](./Screenshots/Unauthorized.png?raw=true "Unauthorized User")


