# PrepVault

> An offline-first JEE preparation companion for Android.

PrepVault helps you keep an **Error Book** and an **Unsolved Question Book**, plan your study with a **To-Do** list, track **chapter progress**, and **revise** with spaced repetition. It is built for the workflow:

> _I attempted a question, either I made a mistake or couldn't solve it. I save it, retry it later, learn from it, and track my improvement._

> **Note on originality:** PrepVault is an independent product. It is not affiliated with, endorsed by, or derived from any existing commercial study-planner app. All branding, copy, data model, and source code in this repository are original.

---

## Features

- **Works out of the box — local accounts** — sign up / log in with email + password stored securely on-device (PBKDF2-hashed). No Firebase setup needed to use the app.
- **Optional cloud sync** — if you configure a real Firebase project, the same credentials are mirrored to Firebase Auth automatically and Firestore sync turns on.

- **Error Book** — save mistakes by mistake type (conceptual, calculation, silly, formula, misread, time-management, guessing, other); add attempted + correct solution, explanation, lesson learned; spaced-repetition review with 1/3/7/14/30-day schedules.
- **Unsolved Question Book** — questions you couldn't solve, organised by source:
  - **DPP** (Daily Practice Problem sheets)
  - **PYQ** (Previous Year Questions, JEE Main + Advanced)
  - **Module** (coaching modules)
  - **Mock Test** (test-series questions)
  - **Book** (textbook exercises)
  - **Custom** (unlimited user-defined sources — "Coaching Sheet", "NCERT", "Revision Sheet", etc.)
- **Retry Queue** — surface questions whose next-retry date has arrived.
- **Move-to-Error workflow** — when an unsolved question turns out to be a genuine mistake, copy it into the Error Book while preserving source metadata and the original unsolved record.
- **To-Do** — Today / Upcoming / Overdue / Completed sections, priorities, recurrence (DAILY / WEEKLY), study streak.
- **Progress Tracking** — per-subject / per-chapter / per-exam-type (JEE Main + Advanced) percent bars; chapter state machine (Not Started → Learning → Practicing → Completed → Needs Revision).
- **Statistics** — total errors / unsolved / solved / tasks completed / revision sessions, current & longest streak, errors by subject / mistake type, unsolved by source.
- **Revision Sessions** — focused walks through due errors with Understood / Needs Revision / Still Confused outcomes.
- **Search** — global search across Error Book and Unsolved Book.
- **Firebase** — email/password + Google sign-in, Cloud Firestore (per-user subcollections), Storage (per-user image folders), Crashlytics (crashes only — no PII).
- **Offline-first** — Room is the local source of truth; a WorkManager worker syncs pending changes to Firestore when connectivity returns. Adding errors, unsolved questions, tasks, progress, and reviews works fully offline.
- **Backup & Restore** — JSON export/import of all user-owned data.
- **Manage Screens** — create / rename / delete custom sources, chapters, and tags.
- **Original Material 3 design** — premium minimal aesthetic, dark mode, accessible typography, empty / loading / error states.

---

## Tech Stack

| Concern | Library |
| --- | --- |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean (presentation → viewmodel → repository → data sources) |
| Async | Kotlin Coroutines + Flow |
| Local DB | Room (with KSP codegen) |
| Cloud DB | Cloud Firestore |
| Auth | Local accounts (PBKDF2) + optional Firebase Auth |
| Storage | Firebase Storage |
| Background sync | WorkManager (periodic + on-demand) |
| Settings | DataStore Preferences |
| Images | Coil |
| JSON | Moshi |
| Analytics / Crashlytics | Firebase Analytics + Crashlytics |

**Why no Hilt?** Hilt's KSP/KAPT version matrix is fragile. For a single-Activity Compose app of this size, a small manual DI container (`ServiceLocator`) is smaller, faster to compile, and easier to reason about. Adding Hilt later is straightforward.

---

## Project Structure

```
app/
└── src/main/java/com/studyinfo/app/
    ├── PrepVaultApp.kt              # Application class, sets up DI + sync
    ├── MainActivity.kt
    ├── ServiceLocator.kt            # Manual DI container
    ├── data/
    │   ├── database/
    │   │   ├── dao/                 # Room DAOs
    │   │   ├── entity/              # Room entities
    │   │   ├── converter/           # Room type converters
    │   │   └── PrepVaultDatabase.kt
    │   ├── firebase/                # Firestore + Storage data sources (only place that touches Firebase)
    │   ├── repository/              # Repositories wrapping Room + Firebase
    │   └── seed/                    # Default JEE chapter database
    ├── domain/model/                # Enums and core domain types
    ├── navigation/                  # Routes + bottom-nav destinations
    ├── sync/                        # SyncWorker (WorkManager)
    ├── ui/                          # Compose screens + ViewModels, grouped by feature
    │   ├── auth/ home/ errors/ unsolved/ todo/ progress/
    │   ├── statistics/ revision/ search/ settings/ backup/ manage/ about/
    │   ├── components/ theme/ icons/
    └── utils/                       # AppResult, id helpers
```

The UI **never** touches Firebase directly. All Firebase code is in `data/firebase/`, wrapped by repositories in `data/repository/`. This lets you swap Firebase for another backend without touching the UI.

---

## Local Setup

### Prerequisites
- Android Studio Iguana or newer
- JDK 17
- A Firebase project (free tier is fine)

### 1. Clone & open
```bash
git clone https://github.com/Epsilon-StudyInfo/Study-Info.git
cd Study-Info
# Open in Android Studio: File → Open → select the directory
```

Android Studio will offer to "Sync Project with Gradle Files". Accept — this generates the local `gradlew` and `gradle-wrapper.jar` if they are not present.

### 2. Run the app
- Connect an Android device or start an emulator (API 24+).
- In Android Studio: Run ▶.

**The app works with zero setup.** Local accounts (email + password, stored securely on-device) and “Continue with Google” both appear on the login screen; email/password works out of the box.

### 3. (Optional) Firebase — cloud sync + Google Sign-In

The repo already contains a real `app/google-services.json` (Firebase client identifiers only — they are not secrets; access is guarded by `firestore.rules` / `storage.rules`). To switch it to YOUR own Firebase project:

1. Go to <https://console.firebase.google.com/> and **Add Project** (or use the existing `epsilon-studyinfo` project).
2. Add an **Android app** with package name `com.studyinfo.app`.
3. Download `google-services.json` and place it at `app/google-services.json`.
4. In **Authentication → Sign-in method**, enable:
   - Email / Password
   - Google
5. In **Firestore Database → Create database**, choose production mode (rules are in this repo).
6. In **Storage → Get started**, choose production mode.
7. Deploy security rules:
   ```bash
   firebase deploy --only firestore:rules,storage
   ```
   (Requires the Firebase CLI: `npm i -g firebase-tools`.)

#### Enabling “Continue with Google” (one-time, per Firebase project)

> ✅ **Already done for this project** — the committed `app/google-services.json` contains the OAuth client entries, and the SHA-1 of the shared debug keystore is registered in Firebase. Google Sign-In works out of the box in CI/debug builds. The steps below are only needed if you switch to a different Firebase project.

Google Sign-In only works after the **SHA-1 fingerprint** of the APK's signing key is registered in Firebase:

1. **Debug builds (CI + local)** are all signed with the shared keystore committed at `keystores/prepvault-debug.keystore` (password `android`). Its fingerprints are:

   - SHA-1: `D6:1A:90:7C:C9:00:21:A5:5F:1D:A7:49:F3:71:CC:97:8F:2E:20:BB`
   - SHA-256: `AE:4E:89:74:19:D5:45:00:35:A4:F5:6A:91:C4:25:E2:55:C8:5E:47:1A:49:EC:88:AD:2D:87:2B:5A:EB:75:E8`

   Verify yourself with:
   ```bash
   keytool -list -v -keystore keystores/prepvault-debug.keystore -alias androiddebugkey -storepass android
   ```
2. In Firebase Console → **Project settings → Your apps → com.studyinfo.app → Add fingerprint**, paste the SHA-1 (add the SHA-256 too).
3. In **Authentication → Sign-in method**, make sure **Google** is **Enabled**.
4. **Re-download** `google-services.json` (it now contains the OAuth client entries) and replace `app/google-services.json` in the repo.

Until steps 2–4 are done, the Google button shows a friendly explanation instead of the account picker — email/password sign-in is unaffected.

---

## GitHub Actions — Build the APK

This repository ships with a workflow at `.github/workflows/build-apk.yml` that you can trigger manually:

1. Open the repository on GitHub.
2. Go to **Actions**.
3. Select **Build Android APK** in the left sidebar.
4. Click **Run workflow**.
5. Choose `build_type` (`debug` or `release-unsigned`).
6. Click the green **Run workflow** button.

When the run finishes:

- The APK will appear under the run's **Artifacts** section.
- File name pattern: `PrepVault-APK-debug-<run_number>` (or `release-unsigned`).
- Click the artifact to download a `.zip` containing the APK.
- Install with `adb install -r PrepVault-debug.apk`.

The workflow:
1. Checks out the repo.
2. Sets up JDK 17.
3. Sets up Android SDK + Build Tools 35.
4. Sets up Gradle 8.11.1 (with built-in dependency caching).
5. Restores `app/google-services.json` from the `GOOGLE_SERVICES_JSON` secret if set (otherwise uses the committed one).
6. Runs `gradle test` (unit tests).
7. Runs `gradle assembleDebug` (or `assembleRelease`).
8. Uploads the APK as a build artifact (retained 30 days).

All debug APKs — CI or local — are signed with the **shared committed debug keystore** (`keystores/prepvault-debug.keystore`), so a new APK always installs cleanly over a previous one.

---

## Firebase GitHub Secrets (optional)

`app/google-services.json` **is committed** — it only contains client-side identifiers, which Firebase considers public (real access control lives in `firestore.rules` / `storage.rules`). If you still prefer to keep your config out of the repo, set the `GOOGLE_SERVICES_JSON` secret and it will **override** the committed file at CI time:

1. Locally, base64-encode the file:
   ```bash
   base64 -w 0 app/google-services.json > /tmp/gs.b64
   ```
2. On GitHub, go to **Settings → Secrets and variables → Actions → New repository secret**.
3. Name: `GOOGLE_SERVICES_JSON`
4. Value: the contents of `/tmp/gs.b64` (paste the whole thing).
5. Save.

**Never commit:**
- Firebase Admin SDK service-account JSON
- Release signing keystores / passwords

(The shared *debug* keystore is intentionally committed — debug keystores are not secrets.)

---

## Release Signing

For now the workflow builds a **debug** APK and an **unsigned release** APK. To produce a signed release:

1. Generate a keystore locally:
   ```bash
   keytool -genkey -v -keystore prepvault-release.keystore \
     -alias prepvault -keyalg RSA -keysize 2048 -validity 10000
   ```
2. Base64-encode it:
   ```bash
   base64 -w 0 prepvault-release.keystore > /tmp/ks.b64
   ```
3. Add GitHub secrets:
   - `STORE_FILE_BASE64` — contents of `/tmp/ks.b64`
   - `STORE_PASSWORD` — keystore password
   - `KEY_ALIAS` — `prepvault`
   - `KEY_PASSWORD` — key password
4. Update `.github/workflows/build-apk.yml` to decode the keystore into `app/release.keystore` before the build, and reference it via `secrets.properties` (which `app/build.gradle.kts` already reads).
5. Set `isMinifyEnabled = true` in `app/build.gradle.kts` once you've validated proguard rules.

Keystores and passwords are never committed.

---

## Backup & Restore

From **Settings → Backup & Restore**:

- **Export** writes a JSON snapshot of tags + custom sources (+ other tables with a schema-version guard) to `app/filesDir/backups/prepvault-<timestamp>.json`. The file is visible in the device's file picker.
- **Import** parses pasted JSON, validates the schema version, then merges tags + custom sources into Room (upsert by id). Future versions will extend import to all tables.

Backup JSON contains **no Firebase credentials**. It is safe to share.

---

## Firestore & Storage Security

The rules in `firestore.rules` and `storage.rules` enforce:

- Every document under `users/{uid}/...` is only readable / writable when `request.auth.uid == uid`.
- No unauthenticated reads or writes.
- Storage uploads must be images ≤ 5 MB.
- No top-level collections are exposed.

A user **cannot** read another user's errors, unsolved questions, tasks, progress, reviews, tags, custom sources, chapters, topics, images, settings, or streak data.

---

## Privacy

- All study data is private by default.
- Crashlytics collects **crash traces only**. It does **not** record question content, solutions, notes, or any other private study material.
- The app requests only the permissions it needs: INTERNET, ACCESS_NETWORK_STATE, POST_NOTIFICATIONS, and CAMERA (only used if you capture a photo via the in-app intent).
- Account deletion removes the Firebase Auth user, the Firestore user document + all subcollections, and (best-effort) the user's Storage tree.

---

## Roadmap

Planned future work (not implemented in v1.0):

- AI-assisted error analysis and question explanations
- PDF import for DPP / PYQ sheets
- Pre-seeded PYQ / DPP chapter databases
- Coaching-specific content packs
- Improved cloud-sync conflict resolution (server-side merge)
- Web dashboard
- Teacher / mentor accounts
- Study groups and shared collections
- Auto-generated study plans based on progress
- In-app image capture with on-device compression pipeline
- Real-time per-question sync (vs periodic)
- Wear OS complication for today's tasks
- Localization (Hindi, Bengali, Tamil, Telugu)

---

## Tests

The project includes unit tests for core business logic:

```bash
gradle test
```

Tests cover:
- Error creation and update flow
- Unsolved → Solved status transitions
- Unsolved → Error Book migration (copies source info, preserves original)
- Task status toggling and recurrence
- Progress percent clamping and chapter state derivation
- Streak computation (current / longest)
- Backup export JSON shape
- Subject / QuestionSource / Difficulty enum round-trips
- Google sign-in: new-account creation, deterministic ids, and linking to an existing email/password account

Tests run on every GitHub Actions build. See `app/src/test/java/com/studyinfo/app/`.

---

## License

MIT — see [LICENSE](LICENSE).
