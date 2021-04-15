package com.example.radiusagent.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.radiusagent.backgroundtask.PropertyWorker
import com.example.radiusagent.di.ProductRepository
import com.example.radiusagent.pojo.*

import dagger.hilt.android.lifecycle.HiltViewModel


import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    val repository: ProductRepository
) : ViewModel() {
    private var _facilites_list =
        MutableLiveData<com.example.radiusagent.pojo.Resource<Facilites>>()
    val facilites get() = _facilites_list
    private var _addedList = MutableLiveData<ArrayList<Facilites>>()
    val addedList get() = _addedList
    private var _optionsList = MutableLiveData<ArrayList<Options>>()
    val optionList get() = _optionsList
    private var _exclusionList = MutableLiveData<ArrayList<ExclusionList>>()
    val exclusionList get() = _exclusionList
    private var _exclusion = MutableLiveData<Exclusion>()
    val exclusion get() = _exclusion

    private var _exclusionValue = arrayListOf<Exclusion>()
    val exclusionValue get() = _exclusionValue

    private var _displayItemValue = MutableLiveData<DisplayItem>()
    val displayItemValue get() = _displayItemValue

    private var _displayItem = MutableLiveData<ArrayList<DisplayItem>>()
    val displayItem get() = _displayItem

    private var _faciltyItem = MutableLiveData<Resource<ArrayList<Facilty>>>()
    val faciltyItem get() = _faciltyItem
    private var _exclusionItem = MutableLiveData<Resource<ArrayList<ArrayList<Exclusion>>>>()
    val exclusionItem get() = _exclusionItem

    private var _exclusionItemsList = MutableLiveData<ArrayList<ArrayList<Exclusion>>>()
    val exclusionItemsList get() = _exclusionItemsList

    private var _exclusionshowList = MutableLiveData<ArrayList<DisplayExclusionList>>()
    val exclusionshowList get() = _exclusionshowList

    private var exclusionMap = hashMapOf<String, String>()


    //gettting the data from network and save it into database
    fun getFacilites() {
        //_facilites_list.value = Resource.loading(null)

        viewModelScope.launch() {
            try {

                val result = repository.getProperty()
                println("getFacilites:: ${result.facilities.size}")
                repository.insertToDataBase(result)
                getDataFromDb()

                // _facilites_list.postValue(com.example.radiusagent.pojo.Resource.success(result))
            } catch (err: Throwable) {
                _facilites_list.postValue(
                    Resource.error(
                        null,
                        message = err.message ?: "Error occured"
                    )
                )
            }
        }
    }

    fun updateBackGroundData() {
        val worker = WorkManager.getInstance()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)

            .build()
        val request: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<PropertyWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
                .addTag("properties")
                .build()
        worker.enqueue(request)


    }


    fun createExclusionList(exclusions: Array<Array<Exclusion>>) {
        //   val hashMap =HashMap<>
        val list = mutableListOf<ArrayList<Exclusion>>()
        // for(exclusion)
        Log.d("ProductsViewModel", " id $exclusions.get(0).get(0).facility_id")
        // exclusions.get(0).get(0).facility_id
        for (exclusion in exclusions) {
            list.add(exclusion.toList() as ArrayList<Exclusion>)
            /* _exclusionList.value!!.add( ExclusionList(
                 exclusion.get(0).facility_id,
                 exclusion.get(0).options_id,
                 exclusion.get(1).facility_id,
                 exclusion.get(1).options_id
             ))

             val key= exclusion.get(0).facility_id +  exclusion.get(0).facility_id;
             val value=  exclusion.get(1).facility_id +  exclusion.get(1).options_id
             exclusionMap.put(key,value)
             Log.d("ProductViewModel","key"+exclusionMap.keys.iterator().next())
             Log.d("ProductViewModel","value"+exclusionMap.keys.iterator().next())*/
        }
        val excutionList = arrayListOf<ExclusionList>()

        for (exclusionValue in list) {

            excutionList.add(
                ExclusionList(
                    exclusionValue.get(0).facility_id,
                    exclusionValue.get(0).options_id,
                    exclusionValue.get(1).facility_id,
                    exclusionValue.get(1).options_id
                )
            )


        }
        _exclusionList.value = excutionList;


    }

    fun addList() {

    }

    fun checkExclusionList(): Boolean {
        /*  for(exclusion in exclusionList)
          {
            // if (addedList.
          }*/
        return true
    }

    fun addToExclusionList(exclusion: Exclusion) {
        //   _exclusionValue.add(exclusion)
        _exclusion.value = exclusion
        //_exclusion.value!!.add(exclusion)


    }

    fun addToDisplayList(displayItem: DisplayItem) {
        _displayItemValue.value = displayItem

    }

    fun trackDisplayItem(displayItemTrackList: ArrayList<DisplayItem>?) {
        _displayItem.value = displayItemTrackList!!

    }

    fun upDateDataBase(facilites: Facilites) {
        viewModelScope.launch {
            repository.upDateBase(facilites)

        }

    }

    fun getDataFromDb() {
        viewModelScope.launch {
            _faciltyItem.value = Resource.loading(null)
            val response = repository.fetchPropertyDataBase()
            val exclusionResposne = repository.fetchExclusionDataBase()
            if (!response.isNullOrEmpty()) {

                _faciltyItem.value = Resource.success(response)
            }
            if (response.isNullOrEmpty())
                _faciltyItem.value = Resource.error(arrayListOf<Facilty>(), "No Value in DataBase")
            if (!exclusionResposne.isNullOrEmpty()) {

                _exclusionItem.value = Resource.success(exclusionResposne)
            }
            if (!exclusionResposne.isNullOrEmpty())
                _exclusionItem.value = Resource.error(null, "No Value in DataBase")


        }
    }

    fun getExclusionItem(exclusions: ArrayList<ArrayList<Exclusion>>?) {
        _exclusionItemsList.value = exclusions!!

    }

    //extracting the exclude list
    fun extractExclusionList(
        displayItemList: ArrayList<DisplayItem>?,
        exclusionList: ArrayList<ArrayList<Exclusion>>
    ) {
        var showList = arrayListOf<DisplayExclusionList>()
        val facilityMap = HashMap<String, DisplayItem>()
        val optionsMap = HashSet<String>()

        for (index in 0..displayItemList!!.size - 1) {

            val facility = displayItemList.get(index).facility_id
            val optionId = displayItemList.get(index).options_id
            facilityMap.put(facility, displayItemList.get(index))
            //    optionsMap.add(optionId)

        }
        //this list to mark the given list is the excluded one

        val list = arrayListOf<Boolean>()
        for (index in 0..exclusionList!!.size - 1) {


            val options = exclusionList.get(index).get(0).options_id
            val facility = exclusionList.get(index).get(0).facility_id
            val options2 = exclusionList.get(index).get(1).options_id
            val facility2 = exclusionList.get(index).get(1).facility_id
            if (facilityMap.containsKey(facility) || facilityMap.containsKey(facility2)) {

                if(facilityMap.get(facility)!=null || facilityMap.get(facility2)!=null)
                {
                    val facilityId = facilityMap.get(facility)!!.facility_id
                    val optionId = facilityMap.get(facility)!!.options_id
                    val name = facilityMap.get(facility)!!.name
                    val icon = facilityMap.get(facility)!!.icon
                    showList.add(DisplayExclusionList(name = name,facility_id = facilityId,
                        options_id = optionId,
                        icon = icon,
                        status = true))
                }


//                if (facilityMap.get(facility)!!.options_id == options &&
//                    facilityMap.get(facility2)!!.options_id==options2) {
//                    val facilityId = facilityMap.get(facility)!!.facility_id
//                    val optionId = facilityMap.get(facility)!!.options_id
//                    val name = facilityMap.get(facility)!!.name
//                    val icon = facilityMap.get(facility)!!.icon
//                    showList.add(DisplayExclusionList(name = name,facility_id = facilityId,
//                        options_id = optionId,
//                        icon = icon,
//                        status = true))
//
//
//                }


            }

        }


        _exclusionshowList.value=showList


    }

}