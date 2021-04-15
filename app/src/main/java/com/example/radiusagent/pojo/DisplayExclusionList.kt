package com.example.radiusagent.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DisplayExclusionList(val facility_id: String,
                                        val options_id: String,
                                        val name: String,
                                        val icon: String,
                                val status:Boolean=false
) : Parcelable {
}