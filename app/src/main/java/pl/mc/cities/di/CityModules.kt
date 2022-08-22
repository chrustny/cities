package pl.mc.cities.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.mc.cities.application.query.CitySearchQuery
import pl.mc.cities.remote.CityService
import pl.mc.cities.remote.RemoteCitySearchQuery
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CityModules {

  @Provides
  @Singleton
  fun retrofit(): Retrofit {
    val objectMapper = jacksonObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    return Retrofit.Builder()
      .client(OkHttpClient())
      .baseUrl("https://secure.geonames.org/")
      .addConverterFactory(JacksonConverterFactory.create(objectMapper))
      .build()
  }


  @Provides
  @Singleton
  fun service(retrofit: Retrofit): CityService = retrofit.create(CityService::class.java)

  @Provides
  fun citySearchQuery(service: CityService): CitySearchQuery = RemoteCitySearchQuery(service)
}