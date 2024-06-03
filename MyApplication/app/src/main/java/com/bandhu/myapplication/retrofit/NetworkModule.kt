package com.example.ride.retrofit

import android.content.Context
import com.bandhu.myapplication.service.RemoteApiService
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val API_BASE_URL = "https://dummyjson.com/"
    private const val HTTP_DIR_CACHE = "mapper_cache"
    private const val CACHE_SIZE = 200 * 1024 * 1024


    private fun provideCache(context: Context): Cache {
        return Cache(File(context.cacheDir, HTTP_DIR_CACHE), CACHE_SIZE.toLong())
    }



    private fun provideOkHttpClient(context: Context): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val token:String? =null//get from session
            // Add bearer token if available
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            val request = requestBuilder
                .addHeader("App-Version", "1.0")
                .addHeader("Accept-Encoding", "identity")
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .cache(provideCache(context))
            .build()
    }

    private fun provideRetrofit(context: Context): Retrofit {
        val okHttpClient = provideOkHttpClient(context)
        val gson = GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

        val gsonConverterFactory = GsonConverterFactory.create(gson)

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
    }

    fun provideApiService(context: Context): RemoteApiService {
        return provideRetrofit(context).create(RemoteApiService::class.java)
    }
}

