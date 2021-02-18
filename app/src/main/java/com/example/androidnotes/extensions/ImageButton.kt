package com.example.androidnotes.extensions

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageButton

fun ImageButton.setActive(){
    this.isClickable = true
    this.setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY)
}

fun ImageButton.setInactive(){
    this.isClickable = false
    this.setColorFilter(Color.rgb(180, 180, 180), PorterDuff.Mode.MULTIPLY)
}