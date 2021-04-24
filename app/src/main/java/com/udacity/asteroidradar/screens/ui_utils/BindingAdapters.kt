package com.udacity.asteroidradar.screens.ui_utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.models.PictureOfDay

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
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

@BindingAdapter("pictureOfDay")
fun ImageView.bindImageOfTheDay(pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (it.mediaType == "image" && it.url.isNotBlank()) {
            Picasso.get().load(it.url).into(this)
        }
    }
}

@BindingAdapter("pictureOfDayTitle")
fun TextView.bindImageOfTheDay(pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        text = if (it.mediaType == "image" && it.url.isNotBlank() && it.title.isNotBlank()) {
            it.title
        } else {
            context.getString(R.string.image_of_the_day)
        }
    }
}
