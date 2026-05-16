# Grama-Vasathi (ग्राम वसति)

**Tagline:** _Live the Matti-Vasane — Scent of the Soil_

Compose-first Kotlin MVP for a rural homestay discovery and host-readiness project.

## Tech Stack
- Kotlin + Jetpack Compose (Material 3)
- Firebase Firestore + Anonymous Auth + Storage (free tier)
- OSMDroid (OpenStreetMap, no API key)
- Glide (image loading)
- Coroutines

## Run Setup
1. Create a Firebase project (free Spark plan).
2. Add Android app package: `com.gramavasathi`.
3. Enable:
   - Anonymous Authentication
   - Firestore Database
   - Firebase Storage
4. Download `google-services.json` and place it at:
   - `app/google-services.json`
5. Sync Gradle and run on device/emulator with internet.

## OSMDroid Setup Note
Already configured in `AndroidManifest.xml`:
- `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_FINE_LOCATION`
- `<uses-library android:name="org.apache.http.legacy" android:required="false" />`

## Data Seeding
- In **Home** screen, long-press the app title/logo area (debug behavior) to trigger seeding.
- Seeder class: `com.gramavasathi.utils.FirebaseSeeder`
- Seeds 5 homestays with Unsplash source URLs exactly as required.

## Screen Overview
- **Splash:** warm intro + anonymous auth bootstrap + timed transition.
- **Home:** hero carousel, activity vibe chips, homestay cards, host CTA, cultural guide CTA.
- **Explore:** debounced search, multi-select activity filters (ALL-selected matching), price/score filters, sorting.
- **Map:** OSMDroid markers for homestays + detail card CTA.
- **Detail:** gallery/info/activities/amenities/host score + booking CTA.
- **Booking:** simulated booking form, validation, total calculation, Firestore save, success dialog.
- **Host Training:** step-by-step checklist, live readiness score (0-100), tier badge updates, final breakdown + share.
- **Guide:** expandable cultural etiquette sections.

## Notes
- MVP scope: no payment gateway, no CI/CD, no ProGuard tuning, no unit-test mandate.
- Firestore collections used:
  - `homestays`
  - `bookings`
