package com.example.radiusagent.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity
data class DbFacility(@PrimaryKey val facility_id:String,
                      @ColumnInfo(name="name") val name:String) {


}
