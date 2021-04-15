package com.example.radiusagent.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbExclusion(@PrimaryKey val id:Int,
                      val optionId:String,
                       val exclusionPairKey:Int,
                     val facilityId:String) {

}