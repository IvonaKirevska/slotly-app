# README

A small booking page for a beauty salon (hair, makeup, nails). The host publishes time slots, clients book one with their name, email and service, and a booked slot can't be taken twice.

- Live app: [LIVE_FRONTEND_URL](https://slotly-app-1.onrender.com)
- API docs: [LIVE_BACKEND_URL/swagger-ui.html](http://localhost:8080/swagger-ui/index.html)

> The backend runs on a free Render instance, so the first request after a pause can take up to a minute.

## What I built

- **Book view:** slots grouped by day, inline booking form (service, name, email, note). Taken slots stay visible but greyed out.
- **Host view:** publish slots (date, start, duration) and see their status. 
- **AI confirmation (Gemini, free tier):** after a booking is saved, the app asks Gemini for a short friendly confirmation. Generating it can take a while, so the call has a 10-second limit. If Gemini doesn't answer in time, everyone gets the same default confirmation message. The booking itself never depends on the AI.

**Stack:** React + TypeScript (Vite), Spring Boot (Java), PostgreSQL (Docker locally, Neon in the cloud), hosted on Render. Backend layers: controller → application service → domain service → repository, with DTOs so booker details are never exposed in the public list.

## Run locally

```bash
# 1. Database
docker run --name slotly-db -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=slotly -p 5433:5432 -d postgres:16

# 2. Backend (http://localhost:8080, Swagger at /swagger-ui.html)
cd backend
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run

# 3. Frontend (http://localhost:5173)
cd client
npm install
npm run dev
```

## AI tools used

- **Claude:** planning the architecture step by step, debugging setup and deployment issues.
- **Gemini API:** generates the confirmation message inside the app.

## What I would improve

- Make the AI confirmation part work more effectively.
- Show the host who booked each slot, allow deleting slots, and add host login.
- Durations per service, Google Calendar clash detection, confirmation emails, cancel/reschedule.
