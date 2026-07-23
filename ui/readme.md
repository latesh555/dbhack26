# RegIntel AI

Enterprise regulatory impact intelligence platform for investment banks.

**Tagline:** From regulatory update to business impact in under 5 minutes.

## Screens

1. **Upload & Analyze** (`/`) — Document upload with drag-and-drop, URL paste, and recent uploads
2. **AI Impact Dashboard** (`/dashboard`) — Full impact assessment with KPIs, heatmap, and AI recommendations
3. **Engineering Impact** (`/engineering`) — Jira stories, timeline, and engineering recommendations

## Getting Started

```bash
cd ui
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173)

## Tech Stack

- React 18 + Vite
- React Router
- Recharts (trend visualization)
- Lucide React (banking icons)

## Project Structure

```
src/
├── components/
│   ├── common/       # Button, Card, KPICard, StatusBadge, SearchBar
│   ├── dashboard/    # DocumentBanner, RiskHeatmap, ActionCard, etc.
│   ├── engineering/  # StoryCard, Timeline, EngineeringPanel
│   ├── layout/       # Header, Sidebar, Logo
│   └── upload/       # UploadZone, HeroIllustration
├── data/             # Mock data
├── pages/            # UploadPage, DashboardPage, EngineeringPage
└── styles/           # Global CSS and design tokens
```
