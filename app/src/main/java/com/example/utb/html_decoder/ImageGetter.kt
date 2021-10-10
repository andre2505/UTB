package com.example.utb.html_decoder

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target


class ImageGetter(val textView: TextView? = null) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val drawable = BitmapDrawablePlaceholder()
            textView?.let { theTextView ->
                Picasso.get()
                        .load(source)
                        .resize(textView.resources.displayMetrics.widthPixels , textView.resources.displayMetrics.heightPixels)
                        .centerInside()
                        .into(drawable)
        }
        return drawable
    }

    private inner class BitmapDrawablePlaceholder : BitmapDrawable(), Target {

        protected var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.let {
                it.draw(canvas);
            }
        }

        private fun setDrawableFinal(drawable: Drawable) {
            this.drawable = drawable
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            drawable.setBounds(0, 0, width, height)
            setBounds(0, 0, width, height)
            if (textView != null) {
                textView.text = textView.text
            }
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
            setDrawableFinal(BitmapDrawable(textView?.context?.resources, bitmap))
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
    }
}

