package com.example.utb.html_decoder

import android.os.Build
import android.text.Html
import android.text.Spanned

@SuppressWarnings("deprecated")
object HtmlDecoder {
    fun encode(text: String? = null): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text!!, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text!!)
        }
    }

}