package com.example.openinapp.apis

import com.example.openinapp.data.DataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface OpenInAppAPI {
    @GET("api/v1/dashboardNew")
    fun getData(@Header("Authorization") token: String): Call<DataResponse>
}