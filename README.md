# Mini Netflix — Day 1 starter (through Step 5)

This project is the **"Connect to the Internet"** lesson, already completed up to
**Step 5 (the Detail screen)**. Clone it to catch up, then continue from **Step 6**.

It talks to **The Movie Database (TMDB)**: downloads popular movies, shows their
posters in a grid, and opens a detail screen when you tap one.

## What's already done (Steps 0–5)
- Project setup, libraries, INTERNET permission, View Binding
- Network layer: `Movie`, `MovieResponse`, Retrofit + Moshi (`TmdbApiService`)
- `OverviewViewModel` with LOADING / ERROR / DONE states
- Poster grid (RecyclerView + Glide)
- Loading spinner & error image
- Detail screen with Navigation + SafeArgs

## ⚙️ Setup — do this first (1 minute)

The API key is **not** in this repo (secrets must never be committed). You must add it yourself:

1. In the **project root**, create a file named **`local.properties`**.
2. Add this single line, pasting the key your instructor gave the class:
   ```properties
   TMDB_API_KEY=PASTE_THE_CLASS_KEY_HERE
   ```
3. In Android Studio: **File → Sync Project with Gradle Files**, then **Run ▶**.

> If you see `Failure: HTTP 401`, the key is missing or wrong — re-check `local.properties` and **sync again**.
> `local.properties` is listed in `.gitignore`, so it will never be pushed. That is intentional. Keep your key there only.

## Requirements
- Android Studio (recent version)
- An emulator or device with Internet access
- Minimum SDK: API 24

## Next
➡️ **Step 6** — filter movies (Popular / Top Rated / Now Playing).
