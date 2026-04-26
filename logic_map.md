# Logic Map — Assignment 04: Advanced Data Integration & Persistent Storage

**Project:** Last Call of Life — Dead Man's Switch / Digital Legacy App  
**Course:** Software for Mobile Devices (Spring 2026)  

---

## Logic Map Table

| Requirement ID | Class / File Name | Function / Method | Implementation Description |
|---|---|---|---|
| **F1** — REST API Integration | `api/NwsAlertService.kt` | `getActiveAlerts()` | Retrofit `suspend` function that fetches active safety alerts from the NWS API (`api.weather.gov/alerts/active`). Uses `@Headers` for User-Agent and accepts `status` and `limit` query params. |
| **F1** — REST API Integration | `api/RetrofitClient.kt` | `alertService` (lazy property) | Singleton Retrofit client with base URL `https://api.weather.gov/` and Gson converter. Provides the `NwsAlertService` instance. |
| **F1** — REST API Integration | `models/AlertFeature.kt` | `NwsAlertResponse`, `AlertFeature`, `AlertProperties` | Data classes mapping the NWS GeoJSON API response. Includes event, headline, description, severity, urgency, area, and sender fields. |
| **F1** — REST API Integration | `adapters/AlertAdapter.kt` | `bind()` | RecyclerView adapter that displays alert event name, headline, severity (color-coded), area description, and sender. Opens alert URL in browser on click. |
| **F1** — REST API Integration | `fragments/NewsFeedFragment.kt` | `fetchAlerts()` | Launches a coroutine on `lifecycleScope`, calls `RetrofitClient.alertService.getActiveAlerts()` on `Dispatchers.IO`, then updates RecyclerView on main thread. Shows loading/error/retry states. |
| **F2** — SQLite Schema Design | `db/LastCallDbHelper.kt` | `onCreate()` | Creates two tables: `contact_groups` (PK `_id` AUTOINCREMENT, `group_name`, `description`, `created_at`) and `emergency_contacts` (PK `_id` AUTOINCREMENT, FK `group_id` → `contact_groups._id` with ON DELETE CASCADE). Seeds 4 default groups. |
| **F2** — SQLite Schema Design | `db/LastCallDbHelper.kt` | `onOpen()` | Enables foreign key enforcement via `PRAGMA foreign_keys = ON`. |
| **F2** — SQLite Schema Design | `models/ContactGroup.kt` | `ContactGroup` data class | Kotlin data class with `id: Long`, `groupName`, `description`, `createdAt`. Maps to `contact_groups` table. |
| **F2** — SQLite Schema Design | `models/Contact.kt` | `Contact` data class | Updated to use `id: Long` (AUTOINCREMENT PK) and `groupId: Long` (FK to `contact_groups`). Implements `Parcelable` for navigation. |
| **F3** — Persistent CRUD | `db/ContactDao.kt` | `insertContact()` | INSERT — converts `Contact` to `ContentValues` and inserts into `emergency_contacts` table. Returns the new row ID. |
| **F3** — Persistent CRUD | `db/ContactDao.kt` | `getAllContacts()` | READ — executes `SELECT c.*, g.group_name FROM emergency_contacts c LEFT JOIN contact_groups g ON c.group_id = g._id ORDER BY priority`. |
| **F3** — Persistent CRUD | `db/ContactDao.kt` | `updateContact()` | UPDATE — converts `Contact` to `ContentValues` and calls `db.update()` with `WHERE _id = ?`. |
| **F3** — Persistent CRUD | `db/ContactDao.kt` | `deleteContact()` | DELETE — calls `db.delete()` with `WHERE _id = ?`. Returns rows deleted. |
| **F3** — Persistent CRUD | `fragments/AddEditContactFragment.kt` | `onViewCreated()` → save button click | Form fragment for CREATE and UPDATE. Loads groups from DB into a Spinner. Validates input, builds a `Contact` object, calls `dao.insertContact()` or `dao.updateContact()` on `Dispatchers.IO`. |
| **F3** — Persistent CRUD | `fragments/ContactDetailFragment.kt` | `btn_delete` click listener | Shows `MaterialAlertDialogBuilder` confirmation, then calls `dao.deleteContact()` on `Dispatchers.IO` and navigates back. |
| **F4** — Data Integration Strategy | *(Architecture-level)* | Strategy B | API module (Safety Alerts) and SQLite module (Emergency Contacts) operate as two **separate, independent functional modules**. The API fetches and displays live NWS alerts; SQLite manages persistent local contact data. No cross-dependency. |
| **F5** — Dynamic SQL Queries | `db/ContactDao.kt` | `searchContacts()` | Executes `SELECT ... WHERE name LIKE '%query%' OR relationship LIKE '%query%'` for text search. |
| **F5** — Dynamic SQL Queries | `db/ContactDao.kt` | `getContactsSorted()` | Executes `SELECT ... ORDER BY $orderBy` where orderBy can be `name ASC`, `priority ASC`, or `c.created_at DESC`. |
| **F5** — Dynamic SQL Queries | `fragments/ContactsFragment.kt` | `loadContacts()` | Calls `dao.searchContacts()` or `dao.getContactsSorted()` on `Dispatchers.IO` based on search bar text and sort menu selection. |

---

## Global Constraints Compliance

| Constraint | How It Is Met |
|---|---|
| **Existing Codebase** | All changes are in the Assignment #03 project (MyApplicationpractice). |
| **Background Threading** | All network calls use `withContext(Dispatchers.IO)` in `NewsFeedFragment.fetchAlerts()`. All DB operations use `withContext(Dispatchers.IO)` in `ContactsFragment`, `AddEditContactFragment`, and `ContactDetailFragment`. |
| **SQLiteOpenHelper** | `LastCallDbHelper` extends `SQLiteOpenHelper`. No Room or SharedPreferences used for business data. |
| **Modular UI** | All content screens are Fragments (`NewsFeedFragment`, `ContactsFragment`, `AddEditContactFragment`, `ContactDetailFragment`, etc.). Activities are containers only (`MainActivity`, `SplashActivity`, `LoginActivity`). |

---

## New & Modified Files Summary

| Action | File | Requirement |
|---|---|---|
| **NEW** | `api/NwsAlertService.kt` | F1 |
| **NEW** | `api/RetrofitClient.kt` | F1 |
| **NEW** | `models/AlertFeature.kt` | F1 |
| **NEW** | `adapters/AlertAdapter.kt` | F1 |
| **NEW** | `fragments/NewsFeedFragment.kt` | F1 |
| **NEW** | `res/layout/fragment_news_feed.xml` | F1 |
| **NEW** | `res/layout/item_alert.xml` | F1 |
| **NEW** | `db/LastCallDbHelper.kt` | F2 |
| **NEW** | `db/ContactDao.kt` | F2, F3, F5 |
| **NEW** | `models/ContactGroup.kt` | F2 |
| **MODIFY** | `models/Contact.kt` | F2 |
| **NEW** | `fragments/AddEditContactFragment.kt` | F3 |
| **NEW** | `res/layout/fragment_add_edit_contact.xml` | F3 |
| **MODIFY** | `fragments/ContactsFragment.kt` | F3, F5 |
| **MODIFY** | `fragments/ContactDetailFragment.kt` | F3 |
| **MODIFY** | `res/navigation/nav_main.xml` | F1, F3 |
| **MODIFY** | `app/build.gradle.kts` | F1 |
| **MODIFY** | `AndroidManifest.xml` | F1 |
| **MODIFY** | `res/menu/bottom_nav_menu.xml` | F1 |
| **NEW** | `res/menu/menu_contacts_sort.xml` | F5 |
