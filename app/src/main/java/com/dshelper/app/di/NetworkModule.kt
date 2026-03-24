package com.dshelper.app.di

import android.util.Log
import com.dshelper.app.BuildConfig
import com.dshelper.app.data.api.AuthApi
import com.dshelper.app.data.api.PostApi
import com.dshelper.app.data.api.ReservationApi
import com.dshelper.app.data.local.TokenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenDataStore: TokenDataStore
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = runBlocking { tokenDataStore.accessToken.first() }
                Log.d("NETWORK", "저장된 토큰 전체: $token")
                Log.d("NETWORK", "토큰 앞 20자: ${token?.take(20)}")
                val request = chain.request().newBuilder()
                    .apply {
                        if (!token.isNullOrEmpty()) {
                            addHeader("Authorization", "Bearer $token")  // ✅ 토큰 추가
                            Log.d("NETWORK", "Authorization 헤더: [Bearer $token]")
                        }
                    }
                    .build()
                Log.d("NETWORK", "요청 URL: ${request.url}")
                Log.d("NETWORK", "요청 헤더: ${request.headers}")
                val response = chain.proceed(request)

                Log.d("NETWORK", "응답 코드: ${response.code}")
                if (response.code == 403) {
                    val errorBody = response.peekBody(Long.MAX_VALUE).string()
                    Log.e("NETWORK", "403 에러 바디: $errorBody")  // 403 원인 확인 ✅
                }

                response
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun providePostApi(retrofit: Retrofit): PostApi {
        return retrofit.create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideReservationApi(retrofit: Retrofit): ReservationApi {
        return retrofit.create(ReservationApi::class.java)
    }
}
