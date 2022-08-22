package pl.mc.cities

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

fun MockWebServer.Companion.createMockWebServer(): MockWebServer {
  System.setProperty("javax.net.ssl.trustStore", "NONE")
  return MockWebServer()
}

fun <T> MockWebServer.createRetrofitService(
  javaClass: Class<T>
): T {
  val objectMapper = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  val converter = JacksonConverterFactory.create(objectMapper)

  val okHttpClient = OkHttpClient.Builder()
    .readTimeout(1, TimeUnit.SECONDS)
    .connectTimeout(1, TimeUnit.SECONDS)
    .followRedirects(false)
    .build()

  val retrofit = Retrofit.Builder()
    .baseUrl(this.url(""))
    .addConverterFactory(converter)
    .client(okHttpClient)
    .build()

  return retrofit.create(javaClass)
}
