package pl.mc.cities

import java.net.HttpURLConnection

val HTTP_400_ERROR_CODES = listOf(
  HttpURLConnection.HTTP_UNAUTHORIZED,
  HttpURLConnection.HTTP_FORBIDDEN,
  HttpURLConnection.HTTP_NOT_FOUND,
  499
)

val HTTP_500_CODES = listOf(
  HttpURLConnection.HTTP_INTERNAL_ERROR,
  HttpURLConnection.HTTP_NOT_IMPLEMENTED,
  HttpURLConnection.HTTP_BAD_GATEWAY,
  HttpURLConnection.HTTP_UNAVAILABLE,
  HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
  HttpURLConnection.HTTP_VERSION,
  599
)

val HTTP_ERROR_CODES = listOf(
  HTTP_400_ERROR_CODES,
  HTTP_500_CODES
).flatten()
