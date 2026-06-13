# 🔔 CodeBell

<div align="center">

### Never miss a coding contest again.

Track upcoming contests, manage reminders, and stay competition-ready across multiple coding platforms — all from one app.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-FF6F00?style=for-the-badge&logo=android&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=for-the-badge&logo=nodedotjs&logoColor=white)
![Express.js](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white)

</div>

---

## 📖 Overview

Competitive programmers often participate in contests hosted across multiple platforms such as Codeforces, LeetCode, CodeChef, AtCoder, and more.

Keeping track of contest schedules manually can be frustrating and often leads to missed opportunities.

**CodeBell** solves this problem by providing a centralized platform where users can:

- 📡 View upcoming contests from multiple platforms
- ⏳ Track live countdowns to contest start times
- 🔔 Schedule personalized reminders
- ➕ Add custom contests and events
- 📶 Access previously fetched contests offline

Whether you're preparing for placements, improving your competitive programming skills, or participating in coding events regularly, CodeBell helps you stay organized and never miss an important contest.

---

## ✨ Features

### 📡 Unified Contest Feed
View upcoming contests from multiple competitive programming platforms in a single place.

### ⏳ Live Countdown Timer
Real-time countdowns help you know exactly when the next contest begins.

### 🔔 Smart Reminder System
Schedule reminders according to your preference:

- 30 Minutes Before
- 1 Hour Before
- 1 Day Before

### ➕ Custom Contest Support
Create and manage your own contests, hackathons, college coding events, or practice sessions.

### 🔍 Search & Filter
Quickly find contests based on:

- Platform
- Contest Name
- Date

### 🛠 Reminder Management
Modify or cancel reminders anytime from within the app.

### 📶 Offline Support
Previously fetched contests remain accessible even without an internet connection using Room Database.

---

## 🏗 Architecture

CodeBell follows the **MVVM (Model–View–ViewModel)** architecture pattern to maintain clean separation of concerns and improve scalability.

```text
UI (Jetpack Compose)
        ↓
ViewModel
        ↓
Repository
   ↙         ↘
Room       Retrofit
(Local)    (Remote)
```

---

## 🧠 Tech Stack

| Category | Technology |
|-----------|------------|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose |
| Design System | Material 3 |
| Architecture | MVVM |
| Database | Room |
| Networking | Retrofit + OkHttp |
| Dependency Injection | Hilt *(Planned)* |
| Navigation | Navigation Compose |
| State Management | StateFlow |
| Asynchronous Programming | Kotlin Coroutines |
| Notifications | AlarmManager + BroadcastReceiver |
| Local Persistence | Room Database |

---

## ⚙️ Core Functionality

### Contest Fetching

- Retrieves upcoming contests from supported platforms
- Keeps contest information updated
- Stores fetched contests locally for offline access

### Reminder Scheduling

Uses Android's AlarmManager:

```kotlin
AlarmManager.setExactAndAllowWhileIdle()
```

Benefits:

- Accurate reminder delivery
- Works during Doze Mode
- Supports multiple reminders
- Reliable notification scheduling

### Manual Contest Creation

Users can create their own events by specifying:

- Contest Name
- Platform
- Date & Time
- Reminder Preference

Custom contests are stored locally and behave exactly like fetched contests.

---

## 📱 Screens

| Screen | Description |
|----------|------------|
| Welcome Screen | User onboarding |
| Home Screen | Countdown and upcoming contests |
| Contest List | All contests with search and filtering |
| Add Contest | Create custom contests |
| Reminder Management | Manage notifications |

---

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/komallsingh/CodeBell.git
```

### Open in Android Studio

```bash
File → Open → Select CodeBell
```

### Build & Run

```bash
./gradlew assembleDebug
```

---

## 🛣 Roadmap

### Completed

- [x] Contest Listing
- [x] Search & Filtering
- [x] Reminder Scheduling
- [x] Offline Storage with Room
- [x] Custom Contest Creation

### Planned

- [ ] Firebase Authentication
- [ ] Cloud Sync
- [ ] Widgets
- [ ] Dynamic Theme Support
- [ ] Multi-device Reminder Sync
- [ ] Calendar Integration
- [ ] Contest Analytics

---

## 🤝 Contributing

Contributions, suggestions, and feedback are welcome.

If you would like to contribute:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a Pull Request

---

## 👩‍💻 Author

**Komal Singh**

Android Developer • Open Source Contributor • Aspiring Generative AI Engineer

---

## 📄 License

This project is licensed under the MIT License.

See the LICENSE file for more information.

---

<div align="center">

⭐ If you find CodeBell useful, consider starring the repository.

</div>
