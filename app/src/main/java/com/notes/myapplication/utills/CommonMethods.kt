package com.notes.myapplication.utills

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CommonMethods {

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        return currentDate.format(formatter)
    }

}