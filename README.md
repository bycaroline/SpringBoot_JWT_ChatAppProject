# Chat App, Server

## Description
This is a chat app project, set up as an assignment at Campus Mölndal. 

## Features
- **Chat**: Users can create chats with other users
- **Search**: Users can search for other users
- **Authentication**: JWT is set up for createing accounts and logging in

## Technologies
- **Backend**: Java
- **Frontend**: HTML, CSS, JavaScript, React
- **Database**: MongoDB

## Licens
Caroline Eklund holds the license. 

## Endpoints Overview

### 1. User Registration
- **`POST /api/user`**  
  Register a new user with a username and password.
  - **Response:** `200 OK`

### 2. User Login
- **`POST /api/login`**  
  Log in to get a JWT token.
  - **Response:** `200 OK` with token  
  - **Failure:** `401 Unauthorized`

### 3. List Chats (Requires Authentication)
- **`GET /api/chats`**  
  List all chats for the logged-in user, sorted by date.
  - **Response:** `200 OK`

### 4. Search User (Requires Authentication)
- **`POST /api/user/{userid}`**  
  Search for a user by their ID.
  - **Response:** `200 OK`  
  - **Failure:** `404 Not Found`

### 5. Start a Chat (Requires Authentication)
- **`POST /api/chats`**  
  Start a new chat or return an existing one.
  - **Response:** `200 OK`

### 6. Send a Message (Requires Authentication)
- **`POST /api/chats/{id}`**  
  Send a message to a specific chat.
  - **Response:** `200 OK`

### 7. Read Messages (Requires Authentication)
- **`GET /api/chats/{id}`**  
  Retrieve all messages from a chat, sorted by date.
  - **Response:** `200 OK`

### 8. Add Participant to Chat (Requires Authentication)
- **`PUT /api/chats/{id}`**  
  Add a new participant to an existing chat.
  - **Response:** `200 OK`

