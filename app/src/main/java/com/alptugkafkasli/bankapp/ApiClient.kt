package com.alptugkafkasli.bankapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            // HTTP Logging Interceptor
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ResponseInterceptor())
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        return retrofit as Retrofit
    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)

            // Check if response is gzip compressed
            if ("gzip".equals(response.header("Content-Encoding"), ignoreCase = true)) {
                // Decompress gzipped response body
                val inputStream = GZIPInputStream(response.body?.byteStream())
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val responseBody = StringBuilder()
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    responseBody.append(line)
                    line = bufferedReader.readLine()
                }

                // Create new response with decompressed body
                return response.newBuilder()
                    .body(ResponseBody.create(response.body?.contentType(), responseBody.toString()))
                    .build()
            }

            return response
        }
    }
}