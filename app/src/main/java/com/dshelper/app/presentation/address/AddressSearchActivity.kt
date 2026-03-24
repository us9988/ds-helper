package com.dshelper.app.presentation.address

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class AddressSearchActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
            }
            addJavascriptInterface(
                AddressJavascriptInterface { roadAddress, jibunAddress ->
                    runOnUiThread {
                        val intent = Intent().apply {
                            putExtra(EXTRA_ROAD_ADDRESS, roadAddress)
                            putExtra(EXTRA_JIBUN_ADDRESS, jibunAddress)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                },
                "Android"
            )
            loadDataWithBaseURL(
                "https://postcode.map.daum.net",
                ADDRESS_SEARCH_HTML,
                "text/html",
                "UTF-8",
                null
            )
        }
        setContentView(webView)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        const val EXTRA_ROAD_ADDRESS = "road_address"
        const val EXTRA_JIBUN_ADDRESS = "jibun_address"

        private val ADDRESS_SEARCH_HTML = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body { width: 100%; height: 100vh; }
                </style>
            </head>
            <body>
                <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
                <script>
                    new daum.Postcode({
                        oncomplete: function(data) {
                            Android.processAddress(
                                data.roadAddress || '',
                                data.jibunAddress || ''
                            );
                        },
                        width: '100%',
                        height: '100%'
                    }).embed(document.body);
                </script>
            </body>
            </html>
        """.trimIndent()
    }
}

class AddressJavascriptInterface(
    private val onAddressSelected: (String, String) -> Unit
) {
    @JavascriptInterface
    fun processAddress(roadAddress: String, jibunAddress: String) {
        onAddressSelected(roadAddress, jibunAddress)
    }
}