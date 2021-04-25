package com.udacity.asteroidradar.screens.ui_utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.models.PictureOfDay

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
        context.getString(R.string.description_hazardous_status_image)
    } else {
        setImageResource(R.drawable.ic_status_normal)
        context.getString(R.string.description_non_hazardous_status_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        context.getString(R.string.description_hazardous_image)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        context.getString(R.string.description_non_hazardous_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun TextView.bindTextViewToAstronomicalUnit(number: Double) {
    val context = context
    text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun TextView.bindTextViewToKmUnit(number: Double) {
    val context = context
    text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun TextView.bindTextViewToDisplayVelocity(number: Double) {
    val context = context
    text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("pictureOfDay")
fun ImageView.bindImageOfTheDay(pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let {
        if (it.mediaType == "image" && it.url.isNotBlank()) {
            Picasso.get().load(it.url).into(this)
            contentDescription = "${pictureOfDay.title} ${context.getString(R.string.image)}"
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
