package com.example.radiusagent.di

import android.util.Log
import com.example.greedygame.api.NetworkApi
import com.example.radiusagent.database.DbExclusion
import com.example.radiusagent.database.DbFacility
import com.example.radiusagent.database.DbProperty
import com.example.radiusagent.database.PropertyDataBase
import com.example.radiusagent.pojo.*
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class ProductRepository @Inject constructor(
    private val api: NetworkApi,
    private val db: PropertyDataBase

) {
    private val propertyDao = db.propertyDao()

    //    fun getProducts() = networkBoundResource(
//        query = {
//            restaurantDao.getAllRestaurants()
//        },
//        fetch = {
//            delay(2000)
//            api.getRestaurants()
//        },
//        saveFetchResult = { restaurants ->
//            db.withTransaction {
//                restaurantDao.deleteAllRestaurants()
//                restaurantDao.insertRestaurants(restaurants)
//            }
//        }
//    )
    suspend fun getProperty(): Facilites {
        return  withContext(Dispatchers.IO)
             {
                 println("getProperty::Thread ${Thread.currentThread().name}")
                 println("getProperty::Thread ${Thread.currentThread().id}")
                 api.getProducts()

     //            println("getViewPagerData ${response.facilites.size}")

             }


    }

    suspend fun upDateBase(facilites: Facilites) {
        propertyDao.deleteAllFacility()
        propertyDao.deleteAllProperty()
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


        }


    }

    //insert into the dataBase
    suspend fun insertToDataBase(facilites: Facilites) {
        withContext(Dispatchers.IO)
        {
            val facList = facilites.facilities.toList()
            val exclusionList = facilites.exclusions
            var dbFacility = mutableListOf<DbFacility>()
            var dbproperty = mutableListOf<DbProperty>()
            var dbExclusion = mutableListOf<DbExclusion>()
            for (list in facList) {
                val facilityID = list.facility_id
                dbFacility.add(DbFacility(facility_id = list.facility_id,name =  list.name))
                val propertyList = list.options
                for (property in propertyList) {
                    dbproperty.add(
                        DbProperty(
                            id = property.id, name = property.name,
                            facilityId = facilityID, icon = property.icon
                        )
                    )
                }

            }
            var i = 0;
            var pairKey=0
            for (exclusion in exclusionList) {
                for (exclusionItem in exclusion.iterator()) {
                    dbExclusion.add(
                        DbExclusion(
                            i, optionId = exclusionItem.options_id,
                            facilityId = exclusionItem.facility_id,
                            exclusionPairKey = pairKey

                        )
                    )
                    i++
                }
               pairKey++


            }

            Log.d("ProductRepository", "insertToDataBase ${dbFacility.size} ")
            propertyDao.insertAllFacility(dbFacility)
            propertyDao.insertAllProperty(dbproperty)
            propertyDao.insertAllExclusion(dbExclusion)


        }


    }

    //
    suspend fun fetchPropertyDataBase(): ArrayList<Facilty> {
        var facilites = arrayListOf<Facilty>()
        var exclusionArray = arrayListOf<ArrayList<Exclusion>>()
        withContext(Dispatchers.IO)
        {
            val facilityList = propertyDao.getAllFacility()
            val propertyList = propertyDao.getAllProperty()
            val exclusionList = propertyDao.getAllExclusion()

            if (!facilityList.isNullOrEmpty() && !propertyList.isNullOrEmpty()) {
                for (facility in facilityList) {
                    val id = facility.facility_id
                    val optionList = arrayListOf<Options>()
                    for (property in propertyList) {
                        if (property.facilityId == id) {
                            val option = Options(name = property.name, id = property.id, icon = property.icon)
                            optionList.add(option)
                        }


                    }
                    facilites.add(Facilty(id, facility.name, optionList.toTypedArray()))


                }

                Log.d("ProductRepository", "list ${facilites.size}")
            }
            exclusionList.sortedWith(Comparator { a, b ->
                when {
                    a.id > b.id -> 1
                    a.id < b.id -> -1
                    else -> 0
                }
            })


        }
        return facilites
    }
        suspend fun fetchExclusionDataBase(): ArrayList<ArrayList<Exclusion>> {
            var exclusionArray = arrayListOf<ArrayList<Exclusion>>()
            withContext(Dispatchers.IO)
            {
                val exclusionList = propertyDao.getAllExclusion()
//                exclusionList.sortedWith(Comparator { a, b ->
//                    when {
//                        a.id > b.id -> 1
//                        a.id < b.id -> -1
//                        else -> 0
//                    }
//                })

                if (!exclusionList.isNullOrEmpty()) {
                    var i=0
                    var list = arrayListOf<Exclusion>()
                    list.add(Exclusion(facility_id = exclusionList[0].facilityId,
                        options_id = exclusionList[0].optionId))
                    for (index in 1..exclusionList.size-1) {
                        if(exclusionList[index].exclusionPairKey!=exclusionList[index-1].exclusionPairKey)
                        {
                           /* list.add(Exclusion(facility_id = exclusionList.get(index-1).facilityId,
                                options_id = exclusionList.get(index-1).optionId))*/
                            exclusionArray.add(list)
                            list= arrayListOf<Exclusion>()
                            /*list.add(Exclusion(facility_id = exclusionList[index].facilityId,
                                options_id = exclusionList[index].optionId))*/
                        }
                        //if the index is last
                        if(index==exclusionList.size-1)
                        {
                            exclusionArray.add(list)
                        }
                        list.add(Exclusion(facility_id = exclusionList[index].facilityId,
                            options_id = exclusionList[index].optionId))
                       /* else if(exclusionList[index].exclusionPairKey==exclusionList[index-1].exclusionPairKey)
                        {
                            list.add(Exclusion(facility_id = exclusionList[index].facilityId,
                                options_id = exclusionList[index].optionId))
                        }*/







                    }
                  //  exclusionArray.add(list)
                }

            }
            return exclusionArray


    }

}