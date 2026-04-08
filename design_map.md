# Design Map — Assignment 2: The XML Architect

**Roll No:** 23F-XXXX *(replace with your roll numbers)*  
**Project:** Last Call — Dead Man's Switch / Digital Legacy App  
**Date:** March 7, 2026  
**Course:** Software for Mobile Devices  

---

## Project Description

Last Call is a **Dead Man's Switch** application. Users check in daily to confirm they are alive. If a check-in is missed, the app automatically notifies emergency contacts and unlocks a secure digital vault containing finances, medical records, estate plans, final wishes, and threat assessments — ensuring loved ones have access to critical information.

---

## Layout Mapping Table

| Requirement ID | Screen / Filename | Function in My App |
|---|---|---|
| **L1** — CoordinatorLayout + AppBarLayout + CollapsingToolbarLayout | `activity_main.xml` | The main app shell. The expanded header shows the "Last Call" title with a shield icon and "Your Digital Legacy" subtitle. When the user scrolls any fragment content, the toolbar collapses and pins at the top showing only the title. |
| **L2** — ConstraintLayout with Guideline + Barrier + Chain | `fragment_profile.xml` | The user profile card. A vertical **Guideline** at 35% aligns all form labels (Full Name, Phone, Email, Bio). A **Barrier** is placed after the longest label to prevent value text from overlapping. A horizontal **spread Chain** distributes the "Edit Profile" and "Share" buttons evenly across the width. |
| **L3** — LinearLayout (Weighted) | `fragment_vault_finances.xml` | The financial summary stats row at the top of the Finances screen. Three cards — **Total Owed** (PKR 2.8M), **To Collect** (PKR 750K), and **Net Worth** (PKR 12.4M) — are given equal width using `android:layout_weight="1"` inside a horizontal LinearLayout. |
| **L4** — RelativeLayout | `item_contact.xml` + `fragment_contacts.xml` | The emergency contact card. The avatar is on the left (`alignParentStart`). The contact name is positioned using `layout_toRightOf` the avatar. The relationship label is positioned using `layout_below` the name. The priority chip (#1, #2, #3) is aligned to the right edge using `layout_alignParentEnd`. |
| **L5** — FrameLayout (Notification Hub) | `fragment_home.xml` | The profile avatar in the greeting header. A `FrameLayout` stacks the user's avatar with a red circular notification badge on top using `layout_gravity="top|end"`. The badge shows the unread alert count ("3"). |
| **L6** — GridLayout with columnSpan | `fragment_vault_modules.xml` | The vault modules dashboard. A 2-column `GridLayout` displays 5 module cards (Finances, Medical, Estate, Final Wishes, Threats). The **Finances** card spans 2 columns using `layout_columnSpan="2"` as the primary/featured module at the top. |
| **L7** — TableLayout with stretchColumns | `fragment_vault_finances.xml` | The "Financial Summary" data table. Uses `stretchColumns="1"` so the middle column (ITEMS) expands to fill available width. Contains 6 data rows: Bank Accounts, Real Estate, Debts Owed, Receivables, Insurance, and Investments — each showing category name, item count, and total PKR value. |
| **L8** — HorizontalScrollView | `fragment_home.xml` | The emergency contacts carousel on the home dashboard. A `HorizontalScrollView` contains a horizontal `LinearLayout` with 4 hardcoded contact avatars (Sara, Ahmed, Fatima, Usman) that scroll left-to-right, independent of the main vertical scroll. |
| **L9** — RadioGroup | `fragment_vault_wishes.xml` | The funeral preference selector in the Final Wishes vault module. A `RadioGroup` with 4 `RadioButton` options: Traditional Burial (default selected), Cremation, Green/Natural Burial, and Donate to Science. Only one option can be selected at a time. |
| **L10** — ConstraintLayout Flow | `fragment_vault_threats.xml` | The "Threat Categories" tag cloud. Uses `androidx.constraintlayout.helper.widget.Flow` with `wrapMode="chain"` to arrange 8 category chips (Blackmail, Harassment, Stalking, Financial Fraud, Domestic Violence, Property Dispute, Cyber Threat, Legal Action) that automatically wrap to the next line when they exceed the screen width. |

---

## Asset Fabrication (XML Only — No Raster Images)

All visual assets are built using XML drawables:

| Asset | File | Technique |
|---|---|---|
| User Avatar | `ic_avatar_placeholder.xml` | `<layer-list>` with oval shapes for head and body silhouette |
| Card Background | `bg_card_dark.xml` | `<shape>` with solid color, corner radius, and stroke |
| Notification Badge | `bg_count_badge.xml` | `<shape android:shape="oval">` with solid red fill |
| Button Backgrounds | `bg_button_primary.xml`, `bg_button_safe.xml` | `<shape>` with gradient/solid fills and rounded corners |
| Status Dot | `shape_status_dot_green.xml` | `<shape android:shape="oval">` with green fill |
| Radial Glow | `bg_radial_glow.xml` | `<shape android:shape="oval">` with radial gradient |
| All Icons | `ic_shield.xml`, `ic_vault_lock.xml`, `ic_finance.xml`, etc. | Android Vector Assets (`<vector>`) |

**Zero `.png`, `.jpg`, or `.webp` files exist in the project.**

---

## Code Hygiene

- **MainActivity.kt** contains only `setContentView(R.layout.activity_main)` — zero backend logic.
- **No Fragment .kt files** exist — all UI is defined purely in XML.
- **No OnClickListeners or Adapters** are used anywhere.
- All colors are defined in `res/values/colors.xml`.
- All dimensions are defined in `res/values/dimens.xml`.
- All strings use `@string/` references or hardcoded dummy data via `android:text`.
- Files follow clear naming: `activity_*.xml`, `fragment_*.xml`, `item_*.xml`, `bottom_sheet_*.xml`.

---

## Team Members

| Roll No | Name | Contribution |
|---|---|---|
| 23F-XXXX | *(Your Name)* | *(e.g., Home, Contacts, Profile screens)* |
| 23F-XXXX | *(Partner Name)* | *(e.g., Vault modules, Finances, Threats)* |


