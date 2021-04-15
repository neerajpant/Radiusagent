package com.example.radiusagent.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//import androidx.room.Entity
//import androidx.room.PrimaryKey


/*
@Entity(tableName = "option")*/
@Parcelize
data class Options( val name:String,
                    val icon:String,
                   /* @PrimaryKey*/ val id:String):Parcelable
{

}