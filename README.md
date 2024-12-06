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
