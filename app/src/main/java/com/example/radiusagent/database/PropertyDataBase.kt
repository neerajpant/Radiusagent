package com.example.radiusagent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [DbFacility::class,DbProperty::class,DbExclusion::class], version = 21)

abstract class PropertyDataBase :RoomDatabase(){
   abstract fun propertyDao(): PropertyDao

}