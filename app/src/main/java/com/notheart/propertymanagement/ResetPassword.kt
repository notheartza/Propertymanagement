package com.notheart.propertymanagement

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        WebView.settings.javaScriptEnabled = true
        WebView.loadUrl("https://www.landvist.xyz/#/resetPassword")
    }
}
