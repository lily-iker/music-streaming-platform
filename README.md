# Music Streaming Platform

A robust music streaming service built with Spring Boot that allows users to stream music, create playlists, and enables artists to manage their content, etc.

## Features

- 🎵 Music Streaming
- 👤 User Authentication & Authorization
- 🎨 Artist Profiles
- 📀 Album Management
- 🎶 Playlist Creation
- 🏷️ Genre-based Organization
- 🔍 Search Functionality
- ☁️ Cloud Storage Integration

## Tech Stack

- **Backend Framework:** Spring Boot 3.3.2
- **Language:** Java 21
- **Database:** MySQL
- **Security:** Spring Security with JWT
- **Media Storage:** Cloudinary
- **Build Tool:** Maven
- **API Documentation:** SpringDoc OpenAPI

## Prerequisites

- Docker

## How to Run

1. **Clone the repository:**
```bash
git clone https://github.com/lily-iker/music-streaming-platform.git
```

2. **Run:**
```bash
bash start.sh ${profile} ${command}
```

### Available Profiles

- `dev`: Development environment configuration
- `test`: Testing environment configuration
- `prod`: Production environment configuration

### Available Commands
- `up`: Start the application
- `down`: Stop the application
- `logs`: View application logs
- `build`: Rebuild the application

### Example
```bash
bash start.sh dev up
```
