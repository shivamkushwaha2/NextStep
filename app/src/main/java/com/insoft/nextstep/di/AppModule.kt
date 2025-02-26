package com.insoft.nextstep.di

import com.insoft.nextstep.data.remote.ApiService
import com.insoft.nextstep.data.repository.JobRepositoryImp
import com.insoft.nextstep.domain.repository.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
       return Retrofit.Builder()
            .baseUrl("https://next-step-backend.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJobRepository(apiService: ApiService): JobRepository {
        return JobRepositoryImp(apiService)
    }
}