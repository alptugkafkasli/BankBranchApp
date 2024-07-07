package com.alptugkafkasli.bankapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("fatiha380/mockjson/main/bankdata")
    fun getBranchs(): Call<List<BranchItem>>

}