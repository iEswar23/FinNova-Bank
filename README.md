# FinNova Bank — Android (Java · XML · MVP)

A practice banking app built to learn real-world Android development end to end:
**MVP architecture, XML layouts, form validation, unit tests and UI tests.**
The main feature is a **Profile & Settings** module with full client-side and server-side validation.

> Personal learning project. FinNova Bank is a fictional brand; the app uses an in-memory fake backend and no real banking data.

## Features

| Area | What it does |
|---|---|
| Sign On | User ID / password, required-field checks, failed-login handling |
| Bottom navigation | Accounts · Benefits · Offers · More |
| Accounts | Greeting + list of accounts (RecyclerView) loaded through a Presenter |
| Profile & Settings | Profile and Settings sections; current values shown masked (email, phone) |
| Preferred first name | Letters, single space / hyphen / apostrophe, max 30, must differ from current |
| Email address | Format check, confirm field must match, server-side "email already in use" |
| Mobile phone | US NANP rules, live formatting `(469) 555-0142`, sent to server as 10 digits |
| Address | Street (no P.O. box), optional line 2, city, state dropdown, ZIP / ZIP+4, focus moves to the first error |
| Income | Employment status, annual income, monthly housing (cross-field: yearly housing ≤ income) |
| User ID & password | Live password checklist, 9 password rules, wrong-current-password and user-ID-taken server errors |
| Login options / Account alerts | Switches saved in SharedPreferences |

## Architecture (MVP)

```
Activity / Fragment  (View)          — renders state, forwards clicks/text to the Presenter
        │  ▲
        ▼  │  Contract.View methods
Presenter                             — screen logic, calls Validator + Repository, no Android classes
        │
        ├── ProfileValidator          — pure Java rules → ValidationResult(ValidationError)
        └── Repository (interface)    — FakeBankRepository (in-memory, fake network delay)
```

* Every screen has a **Contract** (`View` + `Presenter` interfaces), a **Presenter**, and an **Activity** + **XML layout**.
* Validation returns **error codes** (`ValidationError`), and `ErrorMessages` maps codes → `strings.xml`.
* Presenters are tested on the JVM with JUnit + Mockito; the fake repository runs synchronously in tests.

## Project structure

```
app/src/main/java/com/eswar/finnova/
├── base/            BaseView, BasePresenter, BaseActivity
├── data/model/      UserProfile, Address, Income, Account
├── data/repository/ ProfileRepository, AuthRepository, AccountRepository, FakeBankRepository
├── di/              Injection (manual dependency injection)
├── validation/      ProfileValidator, ValidationResult, ValidationError
├── util/            Formatters, TaskRunner
└── ui/
    ├── login/  main/  accounts/  benefits/  more/  common/
    ├── profile/settings/   ← Profile & Settings list
    ├── profile/name|email|phone|address|income/
    └── settings/credentials|comingsoon/ + ToggleSettingsActivity
```

## Run it

1. Android Studio (Ladybug or newer) → **File → Open** → select this folder.
2. Let Gradle sync (JDK 17+).
3. Pick an emulator (API 26+) → **Run ▶**.
4. Sign on with **`demouser` / `Finnova@2026`**.
5. **More → Profile & Settings** → try the edit screens.

Inputs that trigger **server-side** errors in the fake backend:

| Screen | Input | Result |
|---|---|---|
| Email | `taken@finnova.com` | "This email is already linked to another profile." |
| User ID & Password | current password ≠ `Finnova@2026` | "Your current password is incorrect." |
| User ID & Password | new User ID `takenuser` | "That User ID is not available." |

## Tests

```bash
chmod +x gradlew                     # macOS/Linux, once (files uploaded via the web lose the exec bit)
./gradlew testDebugUnitTest          # 43 JVM unit tests (validator, formatters, presenters)
./gradlew connectedDebugAndroidTest  # Espresso UI test (needs emulator/device)
```

## Practice backlog

1. Build **Link accounts**, **Snapshot** and **Connected Apps** screens with MVP.
2. Convert `ToggleSettingsActivity` to MVP with a `SettingsRepository`.
3. Replace `FakeBankRepository` with Retrofit + a mock server (e.g. MockWebServer).
4. Replace `Thread.sleep` in the Espresso test with an `IdlingResource`.
5. Add biometric sign-on with `androidx.biometric`.
6. Switch `AccountsAdapter` to `ListAdapter` + `DiffUtil`.

A full walkthrough is in [`docs/FinNova_Bank_Guide.pdf`](docs/FinNova_Bank_Guide.pdf).
