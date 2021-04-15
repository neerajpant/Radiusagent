package com.example.greedygame.api


import com.example.radiusagent.pojo.Facilites
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {
    companion object {

        const val BASE_URL = "https://my-json-server.typicode.com/"

    }

    @GET("iranjith4/ad-assignment/db")
    suspend fun getProducts(): Facilites
}