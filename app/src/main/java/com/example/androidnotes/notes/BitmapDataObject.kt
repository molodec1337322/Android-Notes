package com.example.androidnotes.notes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*

class BitmapDataObject(var currentImage: Bitmap) : Serializable {
    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        val stream = ByteArrayOutputStream()
        currentImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        out.writeInt(byteArray.size)
        out.write(byteArray)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        val bufferLength = `in`.readInt()
        val byteArray = ByteArray(bufferLength)
        var pos = 0
        do {
            val read = `in`.read(byteArray, pos, bufferLength - pos)
            pos += if (read != -1) {
                read
            } else {
                break
            }
        } while (pos < bufferLength)
        currentImage = BitmapFactory.decodeByteArray(byteArray, 0, bufferLength)
    }
}