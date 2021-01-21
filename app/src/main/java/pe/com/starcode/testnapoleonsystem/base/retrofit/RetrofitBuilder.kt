package pe.com.starcode.testnapoleonsystem.base.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    private fun getRetrofit(): Retrofit {

        var http = OkHttpClient.Builder()
        val okHttpClient = http
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    var apiService = getRetrofit().create(ApiService::class.java)


}