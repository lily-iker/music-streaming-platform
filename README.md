# Music Streaming Platform

A fullstack music streaming service built with Spring Boot and Reactjs that allows users to stream music, create playlists, and enables artists to manage their content, etc.

## Features

- 🎵 Music Streaming
- 👤 User Authentication & Authorization
- 🎨 Artist Profiles
- 📀 Song and Album Management
- 🎶 Playlist Creation
- 🏷️ Genre-based Organization
- 🔍 Search Functionality
- ☁️ Cloud Storage Integration

## Demo

![Client GIF](./demo/unauthentication.gif)
*View of the app without authentication*

![Admin GIF](./demo/admin.gif)
*View of the app with admin account*

## Tech Stack

- **Backend Framework:** Spring Boot 3.3.2
- **Language:** Java 21
- **Database:** MySQL
- **Security:** Spring Security with JWT
- **Media Storage:** Cloudinary
- **Build Tool:** Maven
- **API Documentation:** SpringDoc OpenAPI
- **Frontend**: React, TypeScript, Tailwind CSS, Shadcn/ui

## Prerequisites

- Docker
- Node.js and npm

## How to Run

1. **Clone the repository**
```bash
git clone https://github.com/lily-iker/music-streaming-platform.git
```

2. **Set up .env file in backend folder**
```bash
JWT_SECRET_KEY=
JWT_REFRESH_KEY=
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
```

3. **Run**
```bash
docker-compose up -d
```