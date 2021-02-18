package com.example.androidnotes.notes

import java.text.SimpleDateFormat
import java.util.*

class Note(newTitle: String, newText: String, newTimestamp: String) {
    private var title: String = newTitle
    private var text: String = newText
    private var timestamp: String = newTimestamp

    fun setTitle(newTitle: String){
        title = newTitle
    }

    fun setText(newText: String){
        text = newText
    }

    fun setTimestamp(newTimestamp: String){
        timestamp = newTimestamp
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
}