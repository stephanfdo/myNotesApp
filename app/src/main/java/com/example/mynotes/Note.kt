package com.example.mynotes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val userId: String = ""
) : Parcelable