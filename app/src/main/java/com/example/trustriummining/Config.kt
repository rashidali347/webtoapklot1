package com.example.trustriummining

object Config {
    // Mode Selection: Set to true for URL (Mode 1), false for local files (Mode 2)
    const val LOAD_URL_MODE = true

    // Mode 1: URL (Used only if LOAD_URL_MODE is true)`
    const val WEBSITE_URL = "https://app.trustrium.com/" // Replace with your target website URL

    // Mode 2: Local Assets (Place files in app/src/main/assets/)
    const val LOCAL_FILE_NAME = "index.html" // The main file to load from the assets folder

    // AdMob Configuration (Leave blank "" to disable)
    const val ADMOB_BANNER_ID = "" // Replace with your AdMob Banner Ad Unit ID
    const val ADMOB_INTERSTITIAL_ID = "" // Replace with your AdMob Interstitial Ad Unit ID
    
    // Interstitial Ad frequency in seconds
    const val INTERSTITIAL_INTERVAL_SECONDS = 60L
}
