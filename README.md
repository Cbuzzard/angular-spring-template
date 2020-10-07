# Angular Spring Template

Template that sets up a project with angular frontend and spring boot backend. Allows GET requests to all API endpoints and prevents POST requests to "/rest" enpoints unless the frontend sends a valid jwt. Prebuilt with login button.

## Prerequisite

- Must have a Google project with OAuth Client ID 

## Setup

- cd into "main/frontend" and run
```bash
npm i
```
- set env variables
    - g_id (google client id)
    - g_secret (google secret id)
    - db_url (url to database schema)
    - db_user (database username)
    - db_pass (database password)
    - token_secret (secret to encrypt and decrypt jwt)

- set client_id in main/frontend/src/app/service/user.service.ts on line 67

- (optional) set security options in SecurityConfig

- (optional) set angular routes in MvcConfig line 16

- (optional) set filters to verify jwt for desired endpoints

## Usage

- (Spring Boot) "localhost:5000"
```bash
gradlew bootRun
```
- (Angular) "localhost:4200"
```bash
ng serve
```

## Build Prod

- remove config/DevCorsConfig && line 15 of config/SecurityConfig

```bash
ng build --prod
```
```bash
gradlew build
```

## TODO

- ng build runs automatically when boot run starts.
