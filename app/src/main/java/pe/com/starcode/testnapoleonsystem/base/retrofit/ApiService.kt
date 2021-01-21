package pe.com.starcode.testnapoleonsystem.base.retrofit

import org.json.JSONObject
import pe.com.starcode.testnapoleonsystem.base.retrofit.response.GetUserResponse
import pe.com.starcode.testnapoleonsystem.base.retrofit.response.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<PostResponse>

    @GET("users/{id}")
    suspend fun getUser(@Path("id")  userId: Int): Response<GetUserResponse>

}