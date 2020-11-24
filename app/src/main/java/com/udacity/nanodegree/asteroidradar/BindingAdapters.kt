package com.udacity.nanodegree.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

/**
 * Binding adapter used to display images from URL using Glide
 */
@BindingAdapter("pictureOfDay")
fun AppCompatImageView.setPictureOfDay(pictureOfDay: PictureOfDay?) {
    if (pictureOfDay?.mediaType == "image")
        Picasso.with(context).load(pictureOfDay.url).error(R.drawable.placeholder_picture_of_day)
            .into(this)
    else setImageResource(R.drawable.placeholder_picture_of_day)
    contentDescription = pictureOfDay?.title
}

@BindingAdapter("statusIcon")
fun AppCompatImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
