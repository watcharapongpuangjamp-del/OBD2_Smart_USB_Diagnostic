package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.DtcCategory
import com.example.data.model.DtcSeverity

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString("|||") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split("|||")
    }

    @TypeConverter
    fun fromDtcCategory(value: DtcCategory): String = value.name

    @TypeConverter
    fun toDtcCategory(value: String): DtcCategory = enumValueOf<DtcCategory>(value)

    @TypeConverter
    fun fromDtcSeverity(value: DtcSeverity): String = value.name

    @TypeConverter
    fun toDtcSeverity(value: String): DtcSeverity = enumValueOf<DtcSeverity>(value)
}
