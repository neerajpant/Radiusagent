package com.example.radiusagent.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//
//@Entity(tableName = "facilty")
@Parcelize
data class Facilty(/*@PrimaryKey*/ val facility_id:String,
                   val name:String,
                   val options:Array<Options>):Parcelable{
}