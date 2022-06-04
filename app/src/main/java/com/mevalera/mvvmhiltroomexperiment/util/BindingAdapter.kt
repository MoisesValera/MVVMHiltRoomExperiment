package com.mevalera.mvvmhiltroomexperiment.util

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.imageview.ShapeableImageView
import com.mevalera.mvvmhiltroomexperiment.R.color



@SuppressLint("CheckResult")
@BindingAdapter("logo")
fun ShapeableImageView.logo(
    url: String?
) {
    val placeholderParam = shimmerDrawable()

    @DrawableRes
    val placeholderError = android.R.drawable.stat_notify_error
    val downloadSuccess: (() -> Unit)? = null
    val downloadError: ((Throwable) -> Unit)? = null

    val requestOptions = RequestOptions()
    requestOptions.signature(ObjectKey(System.currentTimeMillis()))

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .placeholder(placeholderParam)
        .error(placeholderError)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                downloadError?.invoke(e ?: Exception("Undefined exception"))
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                downloadSuccess?.invoke()
                return false
            }
        })
        .into(this)
}

private const val BASE_ALPHA = 0.85f
private const val SHIMMER_DURATION = 1000L

private fun ImageView.shimmerDrawable(): ShimmerDrawable {
    val shimmer = Shimmer.ColorHighlightBuilder()
        .setBaseColor(ContextCompat.getColor(this.context, color.md_theme_light_onPrimary))
        .setBaseAlpha(BASE_ALPHA)
        .setHighlightAlpha(BASE_ALPHA)
        .setHighlightColor(
            ContextCompat.getColor(
                this.context,
                color.md_theme_light_onPrimaryContrast
            )
        )
        .setDuration(SHIMMER_DURATION)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()
    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
    return shimmerDrawable
}
