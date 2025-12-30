package es.uniovi.converter

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// --- MODELOS DE DATOS PARA EL JSON ---

data class Rates(
    @SerializedName("USD")
    val usd: Double
)

data class ExchangeRateResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Rates
)

// --- INTERFAZ DE LA API REST ---

interface ExchangeRateApi {

    @GET("latest")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<ExchangeRateResponse>
}

// --- CLIENTE RETROFIT (SINGLETON) ---

object RetrofitClient {

    private const val BASE_URL = "https://api.frankfurter.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ExchangeRateApi by lazy {
        retrofit.create(ExchangeRateApi::class.java)
    }
}