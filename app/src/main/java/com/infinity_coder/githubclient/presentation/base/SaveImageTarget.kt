package com.infinity_coder.githubclient.presentation.base

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso

class SaveImageTarget(
    private val bitmapLoaded: (bitmap: Bitmap) -> Unit = { _ -> }
    ) : com.squareup.picasso.Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                bitmapLoaded(bitmap)
            }
        }

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
        }
    }