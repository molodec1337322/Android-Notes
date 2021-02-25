package com.example.androidnotes.notes

import android.graphics.Bitmap
import java.text.SimpleDateFormat
import java.util.*

class Note(newTitle: String, newText: String, newTimestamp: String, newImage: Bitmap?) {
    private var title: String = newTitle
    private var text: String = newText
    private var timestamp: String = newTimestamp
    private var image: BitmapDataObject? = if(newImage == null) null else BitmapDataObject(newImage)

    fun setTitle(newTitle: String){
        title = newTitle
    }

    fun setText(newText: String){
        text = newText
    }

    fun setTimestamp(newTimestamp: String){
        timestamp = newTimestamp
    }

    fun setImage(newImage: Bitmap){
        image = BitmapDataObject(newImage)
    }

    fun getTitle(): String{
        return title
    }

    fun getText(): String{
        return text
    }

    fun getTimestamp(): String{
        return timestamp
    }

    fun getImage(): Bitmap?{
        return image?.getBitmap()
    }
}