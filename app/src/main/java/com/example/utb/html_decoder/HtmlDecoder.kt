package com.example.utb.html_decoder
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter

object HtmlDecoder {

    @JvmStatic
    @BindingAdapter("htmlEncode")
    fun encode(textView: TextView? = null, text: String? = null) {
        text?.let { theText ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val html = Html.fromHtml(Html.fromHtml(theText, Html.FROM_HTML_MODE_LEGACY).toString(), Html.FROM_HTML_MODE_LEGACY, ImageGetter(textView), null)
                textView?.text = html
            } else {
                val html = Html.fromHtml(Html.fromHtml(theText, ImageGetter(textView), null).toString(), ImageGetter(textView), null)
                textView?.text = html
            }
        }
    }

}