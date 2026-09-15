# Sports Explorer

Sports Explorer is an Android app built for the NIT3213 Final Assignment. The user can log in, see a list of sports loaded from the `nit3213api`, and tap any sport to see its full details.

The app has three screens:
- Login
- Dashboard
- Sport Details.

## Features

### Login
The user enters a student ID and password. The app checks that both fields are filled in, then sends the details to the login endpoint. If the login fails, an error message is shown on the screen. If it succeeds, the app will navigate to the Dashboard.

### Dashboard
The app uses the keypass returned from the login to load the list of sports and shows the sports in a RecyclerView. Each card shows the sport name, number of players, field type and Olympic status. The description is not shown on the card. Tapping a card opens the Details screen for that sport.

### Sport Details
This screen shows all the information about the selected sport, including the full description. The back arrow in the toolbar and the phone's back button both return to the Dashboard.

## API

The app uses the base URL `https://nit3213apinew.onrender.com/` and two endpoints.

| Purpose | Method | Endpoint               | Returns |
|---|---|------------------------|---|
| Login | POST | `/br/auth`             | A `keypass` |
| Dashboard | GET | `/dashboard/{keypass}` | A list of `entities` and the `entityTotal` |

The login request sends a `username` and `password`. The keypass from the login response is added to the dashboard address when the app runs, so it is never written into the code.

Each sport has a name, player count, field type, Olympic status and description.

As mentioned in class by the lecturer, the API is on a free hosting plan and goes to sleep when it is not used. Because of this, the first request after a quiet period can take longer than usual. This is normal and not a problem with the app.

## How the app is built

The app follows the MVVM pattern with a repository.

There is one Activity, `MainActivity`, and each screen is a Fragment. The Navigation Component moves between the screens, and Safe Args passes the selected sport from the Dashboard to the Details screen. The screens are built with XML layouts and `findViewById`.

The Login and Dashboard screens each have a ViewModel. The ViewModel holds the screen's state, and the Fragment watches that state and updates the screen when it changes. The Details screen does not need a ViewModel because it only shows the sport it was given.

Hilt is used for dependency injection. `NetworkModule` provides Retrofit, Moshi, the API service and the repository. The repository is passed into each ViewModel's constructor.

The ViewModels talk to a `SportsRepository` interface instead of calling Retrofit directly. This keeps the network code separate and makes it easy to replace the repository with a mock in the unit tests.

Network calls run in coroutines inside `viewModelScope`, so the calls stop automatically when the screen is closed.

## Project structure

```
app/src/main/java/com/example/s8189084assignment2/
│
├── Assignment2Application.kt      Starts Hilt for the whole app
├── MainActivity.kt                The single Activity that holds all screens
│
├── data/
│   ├── model/
│   │   ├── LoginRequest.kt        The data sent when logging in
│   │   ├── LoginResponse.kt       The login result, which holds the keypass
│   │   ├── DashboardResponse.kt   The dashboard result, which holds the sports list and total
│   │   └── Sport.kt               One sport and its details
│   ├── remote/
│   │   └── ApiService.kt          The login and dashboard API calls
│   └── repository/
│       ├── SportsRepository.kt    The interface the ViewModels use
│       └── SportsRepositoryImpl.kt  The real repository that calls the API
│
├── di/
│   └── NetworkModule.kt           Sets up Retrofit, Moshi and the repository for Hilt
│
└── ui/
    ├── login/
    │   ├── LoginFragment.kt       The Login screen
    │   ├── LoginViewModel.kt      Checks the input and handles the login
    │   └── LoginUiState.kt        What the Login screen should show
    ├── dashboard/
    │   ├── DashboardFragment.kt   The Dashboard screen
    │   ├── DashboardViewModel.kt  Loads the sports list
    │   ├── DashboardUiState.kt    What the Dashboard screen should show
    │   └── SportAdapter.kt        Shows each sport as a card in the list
    └── details/
        └── DetailsFragment.kt     The Sport Details screen

app/src/main/res/
├── layout/         Screen and list card layouts
├── navigation/     The navigation graph between screens
├── drawable/       Icons and badge shapes
└── values/         Colours, text and theme

app/src/test/java/com/example/s8189084assignment2/
├── MainDispatcherRule.kt                     Lets coroutines run inside unit tests
├── ui/login/LoginViewModelTest.kt            Tests for the Login ViewModel
└── ui/dashboard/DashboardViewModelTest.kt    Tests for the Dashboard ViewModel
```

## Dependencies

All libraries are listed in `gradle/libs.versions.toml`. Android Studio downloads the libraries automatically when the project syncs, so nothing needs to be installed by hand.

| Library | Version | Purpose |
|---|---|---|
| Android Gradle Plugin | 9.3.2 | Builds the app |
| KSP | 2.3.11 | Generates code for Hilt |
| Hilt | 2.60.1 | Dependency injection |
| Retrofit | 2.12.0 | Calls the API |
| Retrofit Moshi converter | 2.12.0 | Connects Retrofit and Moshi |
| Moshi Kotlin | 1.15.2 | Reads the JSON from the API |
| Navigation and Safe Args | 2.10.0 | Moves between screens and passes data |
| Lifecycle | 2.11.0 | ViewModels |
| Coroutines | 1.11.0 | Runs network calls in the background |
| RecyclerView | 1.4.0 | The sports list |
| Material Components | 1.14.0 | Buttons, text fields, cards and toolbar |
| ConstraintLayout | 2.2.2 | Screen layouts |
| AppCompat | 1.8.0 | Android support library |
| Core KTX | 1.19.0 | Android support library |
| Activity KTX | 1.13.0 | Android support library |
| Fragment KTX | 1.9.0 | Android support library |
| JUnit | 4.13.2 | Unit tests |
| MockK | 1.14.11 | Fake repository for unit tests |
| Coroutines Test | 1.11.0 | Runs coroutines in unit tests |

## Setup and build

Before building, make sure the following are available:

- Android Studio (a recent stable version)
- JDK 11 or newer
- Android SDK with API 37 installed
- A device or emulator running API 24 or above
- An internet connection

To build the app:

1. Clone the repository.
   ```
   git clone https://github.com/Aryasuwegatama/s8189084Assignment2.git
   ```
2. Open the project folder in Android Studio and wait for Gradle to finish syncing. If it does not start on its own, choose File, then Sync Project with Gradle Files.
3. Build the app from Android Studio, or run this command in the project folder.
   ```
   ./gradlew assembleDebug
   ```
   On Windows Command Prompt, use `gradlew.bat assembleDebug` instead.

## Running the app

In Android Studio, choose Run, then Run 'app', and pick an emulator or a connected phone.

The app opens on the Login screen. No login details are saved in this project, so the student ID and password need to be typed in each time.

- **Student ID:** the student ID without the letter `s` at the start, for example `12345678`
- **Password:** the student's first name, typed exactly as issued, because it is case sensitive.

After logging in, the Dashboard loads the list of sports. Tap any sport to open its details.

## Running the tests

The unit tests check the Login and Dashboard ViewModels. The tests use a fake repository, so no internet connection is needed, and no phone or emulator is needed either.

To run all tests from the terminal:

```
./gradlew testDebugUnitTest
```

To run the tests in Android Studio, open a test file in `app/src/test/` and click the green arrow next to a test, or next to the class name to run every test in that file. The results show in the Run panel.

There are 8 tests in total.

**LoginViewModelTest**
- A correct login saves the keypass.
- Wrong login details show an error message.
- No internet connection shows a connection error.
- Empty fields show an error on each field and do not call the API.

**DashboardViewModelTest**
- A successful load shows the sports list and the total.
- No internet connection shows a connection error.
- An empty list loads without showing an error.
- Loading the dashboard twice only calls the API once.
