package com.example.trustriummining

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.MobileAds // یہ امپورٹ چیک کر لیں

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. سب سے پہلے ایڈز کو انیشلائز کریں تاکہ کریش نہ ہو
        MobileAds.initialize(this) {}

        val root = FrameLayout(this)
        setContentView(root)
        
        swipeRefresh = SwipeRefreshLayout(this)
        webView = WebView(this)
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.max = 100
        
        val adContainer = FrameLayout(this)
        adContainer.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { gravity = android.view.Gravity.BOTTOM }

        root.addView(swipeRefresh)
        swipeRefresh.addView(webView)
        root.addView(progressBar, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 15))
        root.addView(adContainer)

        // 2. WebView Settings (بہتر کنفگریشن)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = android.view.View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = android.view.View.GONE
                swipeRefresh.isRefreshing = false
            }
            // ایرر ہینڈلنگ تاکہ کریش نہ ہو
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                swipeRefresh.isRefreshing = false
            }
        }
        
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        // Load Content
        if (Config.LOAD_URL_MODE) {
            webView.loadUrl(Config.WEBSITE_URL)
        } else {
            webView.loadUrl("file:///android_asset/${Config.LOCAL_FILE_NAME}")
        }

        swipeRefresh.setOnRefreshListener { webView.reload() }

        // Ads - اب یہ محفوظ طریقے سے لوڈ ہوں گے
        try {
            Admob.loadBanner(this, adContainer)
            Admob.loadInterstitial(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            // انٹرسٹیشل ایڈ دکھانے سے پہلے چیک کرنا بہتر ہے
            try {
                Admob.showInterstitial(this)
            } catch (e: Exception) {
                super.onBackPressed()
            }
            super.onBackPressed()
        }
    }
}
