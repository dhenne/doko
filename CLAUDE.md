# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Doppelkopf (card game) score tracking web application with a Spring Boot backend and SvelteKit frontend. The app allows users to create games, record rounds, and share scoreboards online while playing.

## Architecture

### Monorepo Structure
- **doko-backend**: Spring Boot 3.2.4 REST API with PostgreSQL
- **doko-frontend**: SvelteKit application with Tailwind CSS and Flowbite

### Key Architectural Pattern
The frontend builds **into** the backend's static resources directory (`doko-backend/src/main/resources/static`). This is configured in `doko-frontend/svelte.config.js` using the static adapter. The Spring Boot application then serves both the API and the static frontend from a single deployment.

### Backend Architecture
- **Package structure**: `de.vinode.dokobackend`
  - `controller/`: REST endpoints (e.g., GameController)
  - `controller/dto/`: Data transfer objects
  - `entity/`: JPA entities (GameEntity, PlayerEntity, RoundEntity, RoundPlayerResultEntity)
  - `repository/`: Spring Data JPA repositories
  - `service/`: Business logic layer
  - `mapper/`: MapStruct mappers for entity-DTO conversion
  - `domain/`: Domain models

- **Database**: PostgreSQL with JPA (Hibernate)
- **Database migrations**: Configured for Flyway (directory exists at `src/main/resources/db/migration`) but currently using JPA's `ddl-auto: update`
- **Key dependencies**:
  - Lombok for boilerplate reduction
  - MapStruct for entity-DTO mapping (configured with Spring component model)
  - Testcontainers for integration tests

### Frontend Architecture
- **Framework**: SvelteKit with TypeScript
- **Routing**: File-based routing in `src/routes/`
  - `/`: Home page
  - `/game/create/`: Game creation
- **Components**: Located in `src/lib/components/`
  - Key components: gameScreen, addRoundMenu, roundAddButton, gameScreenRowContent
- **Domain logic**: `src/lib/domain/` and `src/lib/domain/service/`
- **Backend adapter**: `src/lib/adapter/` for API communication
- **Styling**: Tailwind CSS with Flowbite component library

## Development Commands

### Initial Setup
```bash
# Start PostgreSQL and Traefik proxy
docker-compose up -d

# Start frontend dev server (from doko-frontend)
cd doko-frontend
pnpm install
pnpm run dev -- --host

# Start backend dev server (from doko-backend)
cd doko-backend
mvn spring-boot:run
```

Access the application at: `http://doko.localhost/`

### Traefik Routing
The Traefik proxy (configured in `traefik-provider-dev.yml`) routes traffic:
- `/api/*` → Backend at `localhost:8080`
- Everything else → Frontend at `localhost:5173`

### Backend Commands
```bash
cd doko-backend

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Run tests in native image
mvn test -PnativeTest

# Build native image with GraalVM
mvn spring-boot:build-image -Pnative

# The native image is published to ghcr.io/dhenne/doko:{version}
```

### Frontend Commands
```bash
cd doko-frontend

# Development server
pnpm run dev          # or: vite

# Build for production (outputs to ../doko-backend/src/main/resources/static)
pnpm run build        # or: svelte-kit build

# Preview production build
pnpm run preview      # or: svelte-kit preview

# Run tests
pnpm test             # or: vitest

# Linting/formatting
npx prettier --write .
npx eslint .
```

### Integration Tests (Playwright E2E)

The `e2e/` directory contains full-stack Playwright tests that run against the live dev environment.

**Prerequisites**: Docker Compose (PostgreSQL + Traefik), backend, and frontend must all be running.

```bash
cd e2e

# Install dependencies and browser
pnpm install
pnpm exec playwright install chromium

# Run tests
pnpm test

# Run tests with browser visible
pnpm test:headed

# Run tests with Playwright UI
pnpm test:ui
```

### Package Manager
The frontend uses **pnpm** (not npm), as evidenced by `pnpm-lock.yaml` and `.npmrc`.

## Database Configuration

PostgreSQL connection is configured via environment variables with defaults:
- `DATABASE_URL`: Default `jdbc:postgresql://localhost:5432/postgres`
- `DATABASE_USERNAME`: Default `postgres`
- `DATABASE_PASSWORD`: Default `postgres`

The docker-compose setup provides a local PostgreSQL instance on port 5432.

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.2.4
- Spring Data JPA with Hibernate enhancement (lazy initialization, dirty tracking, association management)
- PostgreSQL driver
- MapStruct 1.4.1.Final
- Lombok
- Flyway (configured but not actively used)
- GraalVM native image support via Paketo buildpacks

### Frontend
- Svelte 5.0
- SvelteKit 2.47
- TypeScript 5.9
- Vite 7.1
- Tailwind CSS 3.4
- Flowbite 3.1 (component library)
- Vitest for testing

## Build Artifacts

The production build process:
1. Build frontend: `cd doko-frontend && pnpm run build`
2. Build native image: `cd doko-backend && mvn -Pnative spring-boot:build-image`

This creates a native GraalVM container image with the frontend assets bundled inside.

## Deployment

The application is configured for deployment to fly.io (see `fly.toml`).
