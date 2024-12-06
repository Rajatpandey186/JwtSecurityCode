# JwtSecurityCode


Helper Classes:
1. JwtUtils
* Contains utility methods for generating, parsing and validating JWTs.
* Include generating a token from a username, validating a JWT and extracting the username from a token.



2. AuthTokenFilter
* Filters incoming requests to check for a valid JWT in the header, setting the authentication context if the token is valid.
* Extracts JWT from the request header, validates it and configure =s the security context with user details if the token is valid.



3. AuthEntryPointJwt
* Provides custom handling for unauthorized requests, typically when authentication is required but not supplied or valid.
* When an unauthorized request is detected, it logs the error and returns a JSON response with an error message, status code and the path attempted.



Dependencies Required For JWT:

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
