package com.example.bookapitest.interfaces
import com.example.bookapitest.models.Recommendation
import com.example.bookapitest.models.UserRequest
import com.example.bookapitest.models.UserResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiInterface {
    @POST("users")
    suspend fun createUser(@Body userRequest: UserRequest)

    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>

    @DELETE("users/{id_user}")
    suspend fun deleteUser(@Path("id_user") id_user:Int):Response<UserResponse>

    @GET("/recommendations/{id_user}")
    suspend fun getRecommendations(@Path("id_user") id_user: Int):Response<List<Recommendation>>
}