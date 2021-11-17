package com.example.utb.html_decoder

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter

object HtmlDecoder {
    fun encode(text: String? = null): Spanned? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val html = Html.fromHtml(text!!, Html.FROM_HTML_MODE_LEGACY)
            return html
        } else {
            val html = Html.fromHtml(text!!)
            return html
        }
    }

}