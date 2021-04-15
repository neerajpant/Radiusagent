package com.example.radiusagent.pojo


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
    data class Facilites(@SerializedName("facilities")val facilities: Array<Facilty>,
                     @SerializedName("exclusions")  val exclusions:Array<Array<Exclusion>>) :Parcelable{


}