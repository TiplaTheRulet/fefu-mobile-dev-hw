package com.example.swfilmfinder.di

import com.example.swfilmfinder.data.remote.SwapiService
import com.example.swfilmfinder.data.repository.FilmRepositoryImpl
import com.example.swfilmfinder.domain.repository.FilmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSwapiService(): SwapiService {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SwapiService::class.java)
    }

    @Provides
    @Singleton
    fun provideFilmRepository(api: SwapiService): FilmRepository {
        return FilmRepositoryImpl(api)
    }
}
