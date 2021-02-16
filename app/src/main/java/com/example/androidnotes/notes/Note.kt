package com.example.androidnotes.notes

import java.text.SimpleDateFormat
import java.util.*

class Note(newText: String, newTimestamp: String) {
    private var text: String = newText
    private var timestamp: String = newTimestamp

    fun setText(newText: String){
        text = newText
    }

    fun setTimestamp(newTimestamp: String){
        timestamp = newTimestamp
    }

    fun getText(): String{
        return text
    }

    fun getTimestamp(): String{
        return timestamp
    }
}