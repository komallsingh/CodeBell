# 🔔💻 CodeBell  

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Room-FF6F00?style=for-the-badge&logo=android&logoColor=white)

> 🚀 Never miss a coding contest again. Track, filter, and get reminded — all in one place from different platforms.

---

## 📌 What is CodeBell?

Keeping up with contests across platforms like Codeforces, LeetCode, and CodeChef can be messy.  

**CodeBell** simplifies everything by:
- 📡 Bringing all contests into one clean feed  
- ⏳ Showing a live countdown  
- 🔔 Letting you set smart reminders  

No more missed contests. No more switching apps.

---

## ✨ Features

- 📡 **Live Contest Feed**  
  Auto-fetches upcoming contests from multiple platforms on launch  

- ⏳ **Real-time Countdown**  
  See exactly how much time is left for your next contest  

- 🔔 **Smart Reminders**  
  Get notified:
  - 30 minutes before  
  - 1 hour before  
  - 1 day before  

- ➕ **Manual Contests**  
  Add your own college or private contests easily  

- 🔍 **Filter & Search**  
  Find contests by platform, name, or date  

- 🛠 **Reminder Management**  
  Update or cancel reminders directly from the UI  

- 📶 **Offline Support**  
  Works without internet after first fetch  

---

## 🧠 Tech Stack

| ⚙️ Category | 🚀 Tech |
|------------|--------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM |
| Database | Room |
| Networking | Retrofit + OkHttp |
| Notifications | AlarmManager + BroadcastReceiver |
| State | ViewModel + StateFlow |
| Navigation | Jetpack Navigation Compose |
| Async | Kotlin Coroutines |

---

## ⚙️ How It Works

### 📡 Contest Fetching
- Fetches data from Codeforces, LeetCode, and CodeChef APIs  
- No API keys required  
- API contests are refreshed every launch  
- Manual contests stay safe 😄  

---

### 🔔 Reminders
- Uses `AlarmManager.setExactAndAllowWhileIdle()`  
- Works even in Doze mode ⚡  
- Each reminder is uniquely tied to a contest ID  
- Cancelling removes both alarm + DB flag  

---

### ➕ Adding Manual Contests
- Bottom sheet input UI  
- Fields:
  - Contest name  
  - Platform  
  - Date & Time  
  - Reminder offset  

- Room generates ID → used instantly for alarm scheduling  
- 🚫 No race conditions  

---

## 📱 Screens

| 📺 Screen | 📝 Description |
|----------|---------------|
| 👋 Welcome | Onboarding screen |
| 🏠 Home | Countdown + upcoming contests |
| 📋 All Contests | Full list with filters & search |
| ➕ Add Contest | Bottom sheet for adding contests |

---

```bash
git clone https://github.com/komallsingh/CodeBell.git
