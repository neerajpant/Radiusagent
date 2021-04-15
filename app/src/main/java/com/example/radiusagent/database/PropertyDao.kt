package com.example.radiusagent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PropertyDao {
    @Query("SELECT * FROM dbfacility")
  suspend  fun getAllFacility(): List<DbFacility>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFacility(characters: List<DbFacility>)

    @Query("DELETE FROM dbfacility")
    suspend fun deleteAllFacility()

  //Property list

    @Query("SELECT * FROM dbproperty")
    suspend fun getAllProperty(): List<DbProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProperty(characters: List<DbProperty>)

    @Query("DELETE FROM dbproperty")
    suspend fun deleteAllProperty()

  //Exclusion list
  @Query("SELECT * FROM dbexclusion")
  suspend fun getAllExclusion(): List<DbExclusion>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAllExclusion(characters: List<DbExclusion>)

  @Query("DELETE FROM dbexclusion")
  suspend fun deleteExclusion()
}