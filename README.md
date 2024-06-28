# SPAID APP
## Sleep Paralysis Aid Device APP
### A Research Capstone Design

![3efe90c5-9bd4-4646-a240-8d74c75ec34a](https://github.com/LlenyDy/SPAID-APP/assets/173585484/1a928bbe-4e27-48a8-915a-21cb15fd1fd3)

**Researchers/Authors**: Charis Faith Acquiadan, Jufford Austin Enriquez, Jedmel Louise Pabillar  
**App Developer**: Allen Angel H. Delfino

## Description
This project aims to provide a device application that monitors symptoms, alerts users when a patient is experiencing sleep paralysis, and records these incidents in a historical data log. The application is developed using [Kotlin](https://kotlinlang.org/), and leverages [Firebase Realtime Database](https://firebase.google.com/products/realtime-database), written within the [Android Studio](https://developer.android.com/studio) framework.

## Features
The application displays readings from sensors, which can monitor the following:
- Heart Rate: Continuously tracks the user’s heart rate.
- Oxygen Level: Measures the oxygen saturation in the blood.
- Stress Level: Monitors indicators of stress
- Muscle Stiffness: Detects the stiffness of muscles, which can be indicative of sleep paralysis.
- Finger Detection: Ensures accurate placement of sensors by detecting if they are correctly positioned on the finger.
  
Additionally, the app alerts the user when sleep paralysis threshold values are reached, triggering an alarm sound along with a vibration motor to wake the patient.

## Installation
1. Clone the repository: `git clone https://github.com/LlenyDy/SPAID-APP.git`
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Set up Firebase by following the instructions [here](https://firebase.google.com/docs/android/setup).

## Usage
This app is typically paired with a hardware device equipped with sensors, vibration motors, and microcontrollers. The app acts as a central tool for recording, managing gathered data, and providing additional wake-up parameters for the patient.

## Screenshots and Screen Records
https://github.com/LlenyDy/SPAID-APP/assets/173585484/828ac7e0-f0d4-4b2d-8220-8e9bda032195
demonstration of functions of the app

https://github.com/LlenyDy/SPAID-APP/assets/173585484/0ef8daef-440f-4b72-8f4a-2e3ef5f6dfce
demonstration of alarm feature of the app

![historicalrecord](https://github.com/LlenyDy/SPAID-APP/assets/173585484/bde54665-c989-40e2-8b2e-0f81018bb280)
historical record sample screenshot

## Technologies Used

### Programming Language and Frameworks

- **Programming Language**: Kotlin
- **Android Framework**: Android Studio

### Firebase Services

- Firebase Realtime Database (`com.google.firebase:firebase-database:20.3.1`)
- Firebase Authentication (`com.google.firebase:firebase-auth`)
- Firebase Storage (`com.google.firebase:firebase-storage:20.0.0`)

### Android Jetpack Components

- androidx.core (`androidx.core:core-ktx:1.12.0`)
- androidx.appcompat (`androidx.appcompat:appcompat:1.6.1`)
- com.google.android.material (`com.google.android.material:material:1.11.0`)
- androidx.constraintlayout (`androidx.constraintlayout:constraintlayout:2.1.4`)
- androidx.activity (`androidx.activity:activity:1.8.0`)
- androidx.activity-ktx (`androidx.activity:activity-ktx:1.4.0`)
- androidx.fragment (`androidx.fragment:fragment-ktx:1.4.0`)

### Date/Time Library

- ThreeTenABP (`com.jakewharton.threetenabp:threetenabp:1.3.0`)

### Testing

- JUnit for unit testing (`junit:junit:4.13.2`)
- Espresso for UI testing (`androidx.test.espresso:espresso-core:3.5.1`)

### Build System and Configurations

- **Build System**: Gradle
- **Android SDK Version**:
  - `compileSdkVersion`: 34
  - `minSdkVersion`: 24
  - `targetSdkVersion`: 34
- **Java Compatibility**: Java 8 (configured with `sourceCompatibility` and `targetCompatibility` set to `JavaVersion.VERSION_1_8`)
- **Build Features**:
  - View Binding enabled (`buildFeatures { viewBinding = true }`)
- **Firebase BOM**: Firebase Bill of Materials (`com.google.firebase:firebase-bom:32.8.0`), used for dependency management.

  ## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.







