package com.notes.myapplication.utills

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(separator = ",")

    @TypeConverter
    fun toList(data: String): List<String> = if (data.isEmpty()) emptyList() else data.split(",")
}
