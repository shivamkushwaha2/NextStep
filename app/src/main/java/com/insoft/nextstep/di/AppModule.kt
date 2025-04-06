package com.insoft.nextstep.di

import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.data.repository.AuthRepositoryImpl
import com.insoft.nextstep.data.repository.JobRepositoryImp
import com.insoft.nextstep.data.repository.PostRepositoryImpl
import com.insoft.nextstep.data.repository.ProjectRepositoryImpl
import com.insoft.nextstep.data.repository.VideoRepositoryImpl
import com.insoft.nextstep.domain.repository.AuthRepository
import com.insoft.nextstep.domain.repository.JobRepository
import com.insoft.nextstep.domain.repository.PostRepository
import com.insoft.nextstep.domain.repository.ProjectRepository
import com.insoft.nextstep.domain.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs request and response bodies
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Attach logging interceptor
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
       return Retrofit.Builder()
            .baseUrl("https://nextstepbackend.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Attach OkHttpClient with logging
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobRepository(apiService: ApiService): JobRepository {
        return JobRepositoryImp(apiService)
    }
    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideVideoRepository(apiService: ApiService): VideoRepository {
        return VideoRepositoryImpl(apiService)
    }
    @Provides
    @Singleton
    fun providePostRepository(apiService: ApiService): PostRepository {
        return PostRepositoryImpl(apiService)
    }
    @Provides
    fun provideProjectRepository(api: ApiService): ProjectRepository =
        ProjectRepositoryImpl(api)
}