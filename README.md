# Grama-Vasathi

Grama-Vasathi is a modern Android application designed to bridge the gap between rural homestay hosts and travelers seeking authentic village experiences in Karnataka. The platform focuses on empowering rural communities through sustainable tourism while providing travelers with unique, immersive farm stays.

## Project Overview

The application serves as a marketplace for rural homestays, highlighting local culture, farm activities, and authentic hospitality. It addresses the challenge of visibility for rural hosts and provides a structured platform for booking and cultural exchange.

GitHub Repository: [https://github.com/jishnu70/Grama-Vasathi](https://github.com/jishnu70/Grama-Vasathi)

## Key Features

- **Localized Experience**: Full support for Kannada language across the interface to resonate with local hosts and travelers.
- **Premium Exploration**: A refined search and filter system allowing users to find stays based on specific farm activities such as cow milking, field plowing, and sunrise treks.
- **Detailed Homestay Insights**: Comprehensive profiles for each stay including host readiness scores, verified status, and specific amenities.
- **Integrated Booking**: A seamless flow for users to reserve their stay directly through the application.
- **Interactive Maps**: Support for location-based exploration using OSMDroid.
- **Dynamic Content**: Powered by Firebase Firestore for real-time updates and synchronization.

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase (Firestore, Authentication, Storage)
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose
- **Concurrency**: Kotlin Coroutines and Flow
- **Maps**: OSMDroid

## Project Structure

- `com.gramavasathi.data`: Contains models, repositories, and data sources.
- `com.gramavasathi.ui.screens`: Implementation of all Jetpack Compose screens (Home, Explore, Detail, Booking, etc.).
- `com.gramavasathi.ui.viewmodel`: Business logic and state management for the UI.
- `com.gramavasathi.ui.theme`: Centralized theme definitions including custom typography (Google Fonts) and color palettes.
- `com.gramavasathi.utils`: Helper functions and extensions.

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/jishnu70/Grama-Vasathi.git
   ```
2. Open the project in Android Studio (Hedgehog or newer recommended).
3. Ensure you have a `google-services.json` file in the `app/` directory if you intend to use the live Firebase backend.
4. Sync the project with Gradle files.
5. Build and run the application on an emulator or physical device.

## Localization

The project prioritizes the local culture of Karnataka. UI elements and hardcoded data have been localized to Kannada to ensure accessibility and authenticity for the primary target audience.

## License

This project is developed for the purpose of promoting rural development and sustainable tourism. Please refer to the repository for specific licensing details.
