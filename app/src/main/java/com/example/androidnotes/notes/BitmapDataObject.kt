package com.example.androidnotes.notes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*

class BitmapDataObject(var currentImage: Bitmap){
    private lateinit var byteArray: ByteArray

    init {
        bitmapToBytes()
    }

    @Throws(IOException::class)
    private fun bitmapToBytes() {
        val stream = ByteArrayOutputStream()
        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        byteArray = stream.toByteArray()
    }

    fun getBitmap(): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}