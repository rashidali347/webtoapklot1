package com.example.trustriummining

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object Admob {
    private var interstitialAd: InterstitialAd? = null
    private var lastInterstitialTime: Long = 0

    fun loadBanner(context: Context, container: FrameLayout) {
        if (Config.ADMOB_BANNER_ID.isBlank()) return

        val adView = AdView(context)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = Config.ADMOB_BANNER_ID
        container.addView(adView)
        adView.loadAd(AdRequest.Builder().build())
    }

    fun loadInterstitial(context: Context) {
        if (Config.ADMOB_INTERSTITIAL_ID.isBlank()) return

        InterstitialAd.load(context, Config.ADMOB_INTERSTITIAL_ID, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            })
    }

    fun showInterstitial(activity: Activity) {
        if (interstitialAd == null || Config.ADMOB_INTERSTITIAL_ID.isBlank()) return
        
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInterstitialTime >= Config.INTERSTITIAL_INTERVAL_SECONDS * 1000) {
            interstitialAd?.show(activity)
            lastInterstitialTime = currentTime
            loadInterstitial(activity) // Preload next
        }
    }
}
