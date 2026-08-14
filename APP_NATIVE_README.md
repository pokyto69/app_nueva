# RestApp Wear Native

This is a native Wear OS application built with Kotlin and Jetpack Compose.

## Features

- **Identification Screen**: Allows waiters to select their name from a list fetched from Firestore.
- **Monitor Global**: Real-time monitoring of table statuses (LIBRE, OCUPADA, PAGO).
- **Active Alerts**: View tables that have requested payment (`solicitudPago`).
- **Alert Detail**: Detailed view of an alert with a "Voy en camino" button to clear the notification.
- **Shift Summary**: Quick overview of current restaurant status for the logged-in waiter.

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose for Wear OS
- **Backend**: Firebase Firestore
- **Package Name**: `com.tuname.myapplication`

## Setup

The app is configured to use the existing Firebase project. The `google-services.json` has been updated with the package name `com.tuname.myapplication`.

## Project Structure

- `app/src/main/java/com/tuname/myapplication/MainActivity.kt`: Entry point and Navigation setup.
- `app/src/main/java/com/tuname/myapplication/screens/`: Contains all the Compose screens.
- `app/src/main/java/com/tuname/myapplication/theme/`: Wear OS Material theme configuration.
