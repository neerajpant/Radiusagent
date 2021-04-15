package com.example.radiusagent.backgroundtask

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.greedygame.api.NetworkApi
import com.example.radiusagent.database.DbFacility
import com.example.radiusagent.database.DbProperty
import com.example.radiusagent.database.PropertyDao
import com.example.radiusagent.database.PropertyDataBase
import com.example.radiusagent.pojo.Facilites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PropertyWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            withContext(Dispatchers.IO) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(NetworkApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val dataBase =
                    Room.databaseBuilder(
                        applicationContext,
                        PropertyDataBase::class.java,
                        "product_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                val dao = dataBase.propertyDao()
                val networkApi = retrofit.create(NetworkApi::class.java)
                Log.d("PropertyWorker", "doWork retrofit "+retrofit.hashCode())
                Log.d("PropertyWorker", "doWork  networkapi"+networkApi.hashCode())
                Log.d("PropertyWorker", "doWork  dao"+dao.hashCode())

                val response = networkApi.getProducts()
                Log.d("PropertyWorker", "response ${response.facilities.size}")

                //update  dataBase
                upDateBase(response,dao)

            }

            Result.success()

        } catch (exception: Exception) {

            Result.failure()
        }


    }
    suspend fun upDateBase(
        facilites: Facilites,
        propertyDao: PropertyDao
    ) {
        Log.d("PropertyWorker","Before delete dataBase ${propertyDao.getAllProperty().size}")
        propertyDao.deleteAllFacility()
        propertyDao.deleteAllProperty()
        Log.d("PropertyWorker","After delete dataBase ${propertyDao.getAllProperty().size}")
        withContext(Dispatchers.IO)
        {
            val facList = facilites.facilities.toList()
            var dbFacility = mutableListOf<DbFacility>()

            for (list in facList) {
                val facilityID = list.facility_id
                dbFacility.add(DbFacility(list.facility_id, list.name))
                val propertyList = list.options
                var dbPropertyList = arrayListOf<DbProperty>()
                for (property in propertyList) {
                    dbPropertyList.add(
                        DbProperty(
                            id = property.id, name = property.name,
                            facilityId = facilityID, icon = property.icon
                        )
                    )
                }
                propertyDao.insertAllProperty(dbPropertyList)

            }
            propertyDao.insertAllFacility(dbFacility)
            Log.d("PropertyWorker","Updated DataBase ${propertyDao.getAllProperty().size}")


        }


    }

}