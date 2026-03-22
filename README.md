# CodeBell

A clean, minimal Android app to track upcoming competitive programming contests — with smart reminders so you never miss one.

---

## Features

- Fetches live contests from **Codeforces**, **LeetCode**, and **CodeChef**
- Real-time countdown timer to the next contest
- Set reminders — 30 minutes, 1 hour, or start of day before any contest
- Add and track manual contests with a custom date & time picker
- Filter by platform or date range — Today, This Week, This Month
- Search contests by name or platform
- Full reminder management — set, change, or cancel from the contest card
- All data stored locally — works offline after first fetch

---

## Tech Stack

- **Language** — Kotlin
- **UI** — Jetpack Compose + Material 3
- **Architecture** — MVVM (ViewModel + StateFlow)
- **Database** — Room
- **Networking** — Retrofit + OkHttp
- **Notifications** — AlarmManager + BroadcastReceiver
- **Navigation** — Jetpack Navigation Compose
- **Async** — Kotlin Coroutines

---

## How It Works

**Contest Fetching**
On launch the app hits the Codeforces, LeetCode, and CodeChef public APIs, clears stale API data, and re-inserts fresh results into Room. Manual contests are never affected by this refresh.

**Reminders**
Uses `AlarmManager.setExactAndAllowWhileIdle()` to fire precise notifications even in Doze mode. Each alarm is keyed by `contestId` so there are no collisions. Cancelling a reminder removes the pending intent and clears the flag in the database.

**Manual Contests**
Added via a bottom sheet — name, platform, date/time, and reminder offset. Room returns the real auto-generated ID after insert, which is used immediately to schedule the alarm.

---

## Screens

| Screen | Description |
|--------|-------------|
| Welcome | Onboarding |
| Home | Next contest countdown + also coming up list |
| All Contests | Full list with search and filters |
| Add Contest | Bottom sheet to add a manual contest |

---

## Getting Started

```bash
git clone https://github.com/yourusername/ContestTracker.git
```

Open in Android Studio, let Gradle sync, and run on any device or emulator with API 26+.

No API keys required — all contest sources are public.

---

## Permissions

```
POST_NOTIFICATIONS      — show reminder notifications (Android 13+)
USE_EXACT_ALARM         — precise alarm scheduling
INTERNET                — fetch contest data
```

---

## License

MIT
