package com.example.radiusagent.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/*@Entity(tableName = "exclusion")*/
@Parcelize
data class Exclusion(/*@PrimaryKey */ val facility_id:String,
                      val options_id:String):Parcelable
{
}