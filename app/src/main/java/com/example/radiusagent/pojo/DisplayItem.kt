package com.example.radiusagent.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DisplayItem(/*@PrimaryKey */ val facility_id: String,
                                        val options_id: String,
                                        val name: String,
                                        val icon: String
) : Parcelable {
}