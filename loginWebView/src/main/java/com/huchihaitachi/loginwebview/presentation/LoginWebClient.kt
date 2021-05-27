package com.huchihaitachi.loginwebview.presentation

import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.huchihaitachi.loginwebview.BuildConfig
import com.huchihaitachi.loginwebview.di.scope.LoginWebScope
import javax.inject.Inject

@LoginWebScope
class LoginWebClient @Inject constructor() : WebViewClient() {
  lateinit var onLoggedIn: (code: String) -> Unit

  override fun onReceivedSslError(
    view: WebView?,
    handler: SslErrorHandler?,
    error: SslError?
  ) {
    super.onReceivedSslError(view, handler, error)
    handler?.proceed()
  }

  override fun shouldOverrideUrlLoading(
    view: WebView?,
    url: String?
  ): Boolean {
    val parsedUrl = Uri.parse(url)
    return if (parsedUrl.host == BuildConfig.REDIRECT_HOST) {
      parsedUrl.getQueryParameter(CODE)?.let { code ->
        onLoggedIn(code)
      }
      true
    } else {
      false
    }
  }

  companion object {
    const val CODE = "code"
  }
}