package com.example.aplicacioncrud

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String
    ): Call<LoginResponse>
}
