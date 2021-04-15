package com.example.radiusagent.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.radiusagent.pojo.Facilty
import com.example.radiusagent.pojo.Options
import com.google.gson.Gson
import javax.annotation.Nullable

@Entity
data class DbProperty(@PrimaryKey val id:String,
                      val name:String,

                      val icon:String,val facilityId:String) {

}

/*
data class OptionsDb( val facility_id:String,
                      val name:String,val options:String) {


}
class OptionsTypeConvert{
    @TypeConverter
    fun listToJson(value :MutableList<OptionsDb>?)
    {
      Gson().toJson(value)
    }
    @TypeConverter
    fun jsonToList(value :String)= Gson().fromJson(value,Array<OptionsDb>::class.java).toMutableList()
}*/
