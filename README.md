# 📱 Last Call of Life — Dead Man's Switch & Digital Legacy App

> **Course:** Software for Mobile Devices (Spring 2026)  
> **Platform:** Android (Kotlin)  
> **Min SDK:** 26 | **Target SDK:** 35

---

## 📖 Overview

**Last Call of Life** is a *Dead Man's Switch* Android application. Users check in daily to confirm they are alive. If a check-in is missed, the app automatically notifies emergency contacts and unlocks a secure **Digital Vault** containing finances, medical records, estate plans, final wishes, and threat assessments — ensuring loved ones have access to critical information when it matters most.

---

##  Features

###  Home Dashboard
- Personalized greeting with notification badge (FrameLayout overlay)
- Emergency contacts carousel (HorizontalScrollView)
- Quick-access vault module shortcuts

### 🔐 Digital Vault
- **Finances** — Total owed, collectibles, and net worth summary with data table
- **Medical** — Medical records and health information
- **Estate Plans** — Property and estate documentation
- **Final Wishes** — Funeral preferences via RadioGroup selector
- **Threat Assessment** — Category tag cloud with ConstraintLayout Flow

### 👥 Emergency Contacts
- Full CRUD (Create, Read, Update, Delete) backed by **SQLite**
- Contact groups with foreign key relationships
- Real-time search and multi-field sorting
- Priority ranking (#1, #2, #3)

### 📡 Safety Alerts (News Feed)
- Live alerts fetched from the **NWS (National Weather Service) API** (`api.weather.gov`)
- Severity color-coding, event details, and in-app browser links
- Background threading via Kotlin Coroutines

### 👤 Profile
- User profile card with ConstraintLayout Guideline + Barrier
- Edit and Share actions

---

## 🏗️ Architecture & Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | XML Layouts (zero Jetpack Compose) |
| Navigation | Jetpack Navigation Component (single-Activity) |
| Networking | Retrofit 2 + Gson Converter |
| Local DB | SQLite via `SQLiteOpenHelper` (no Room) |
| Async | Kotlin Coroutines (`lifecycleScope`, `Dispatchers.IO`) |
| UI Components | Material Design 3, ConstraintLayout, CoordinatorLayout |
| Assets | 100% XML Drawables — zero raster images |

---

## 📐 Layout Showcase

| Screen | Layout Technique |
|---|---|
| `activity_main.xml` | CoordinatorLayout + CollapsingToolbarLayout |
| `fragment_profile.xml` | ConstraintLayout with Guideline, Barrier, and Chain |
| `fragment_vault_finances.xml` | LinearLayout (weighted) + TableLayout (stretchColumns) |
| `fragment_contacts.xml` + `item_contact.xml` | RelativeLayout |
| `fragment_home.xml` | FrameLayout (notification badge) + HorizontalScrollView |
| `fragment_vault_modules.xml` | GridLayout with columnSpan |
| `fragment_vault_wishes.xml` | RadioGroup |
| `fragment_vault_threats.xml` | ConstraintLayout Flow (tag cloud) |

---

## 🗄️ Database Schema

```sql
-- Contact groups table
CREATE TABLE contact_groups (
    _id         INTEGER PRIMARY KEY AUTOINCREMENT,
    group_name  TEXT NOT NULL,
    description TEXT,
    created_at  TEXT
);

-- Emergency contacts table (FK → contact_groups)
CREATE TABLE emergency_contacts (
    _id          INTEGER PRIMARY KEY AUTOINCREMENT,
    group_id     INTEGER REFERENCES contact_groups(_id) ON DELETE CASCADE,
    name         TEXT NOT NULL,
    relationship TEXT,
    priority     INTEGER,
    created_at   TEXT
);
```

---

## 🌐 REST API Integration

- **Endpoint:** `https://api.weather.gov/alerts/active`
- **Format:** GeoJSON FeatureCollection
- **Fields Used:** `event`, `headline`, `description`, `severity`, `urgency`, `areaDesc`, `senderName`
- **Error Handling:** Loading / error / retry states in `NewsFeedFragment`

---

## 📁 Project Structure

```
app/src/main/java/com/example/myapplicationpractice/
├── activities/          # MainActivity, SplashActivity, LoginActivity
├── fragments/           # All UI screens as Fragments
├── adapters/            # RecyclerView Adapters (AlertAdapter, etc.)
├── api/                 # Retrofit service & client (NwsAlertService, RetrofitClient)
├── db/                  # SQLiteOpenHelper & DAO (LastCallDbHelper, ContactDao)
├── models/              # Data classes (Contact, ContactGroup, AlertFeature, etc.)
└── res/
    ├── layout/          # All XML layout files
    ├── drawable/        # XML-only drawable assets
    ├── navigation/      # nav_main.xml
    ├── menu/            # Bottom nav & sort menus
    └── values/          # colors.xml, dimens.xml, strings.xml, themes.xml
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (or later)
- JDK 11+
- Android device / emulator with API 26+

### Build & Run
```bash
# Clone the repository
git clone https://github.com/Zubair-Ali-Sandhu/LAST-CALL-OF-LIFE.git
cd LAST-CALL-OF-LIFE

# Open in Android Studio, then sync Gradle and run on a device/emulator
```

### Permissions Required
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 📦 Key Dependencies

```kotlin
// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.9.7")
implementation("androidx.navigation:navigation-ui-ktx:2.9.7")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

// UI
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.constraintlayout:constraintlayout:2.x.x")
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

---

## 🎨 Design Principles

- **Dark theme** throughout with a consistent color palette (`bg_primary`, `accent_red`, `text_secondary`)
- **Zero raster images** — all visuals are XML drawables (`<shape>`, `<layer-list>`, `<vector>`)
- **Single Activity architecture** — `MainActivity` is purely a container; all screens are Fragments
- **No business logic in Activities** — `MainActivity.kt` contains only `setContentView()`
- All colors → `colors.xml` | All dimensions → `dimens.xml` | All strings → `strings.xml`

---

## 👨‍💻 Team

| Roll No | Name | Contribution |
|---|---|---|
| 23F-XXXX | *(Member 1)* | Home, Contacts, Profile screens |
| 23F-XXXX | *(Member 2)* | Vault modules, Finances, Threats |

---

## 📄 License

This project is developed for academic purposes as part of the *Software for Mobile Devices* course (Spring 2026).

