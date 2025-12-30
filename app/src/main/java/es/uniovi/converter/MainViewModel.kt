package es.uniovi.converter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // Factor de conversión EUR -> USD (valor provisional hasta descargar el real)
    var euroToDollar: Double = 1.16
        private set

    // Para evitar que se descargue más de una vez si se gira la pantalla
    private var alreadyFetched = false

    init {
        Log.d("MainViewModel", "ViewModel creado")
    }

    /**
     * Descarga el tipo de cambio desde Frankfurter.
     * Solo se ejecuta la primera vez (evitamos descargas duplicadas).
     */
    fun fetchExchangeRate() {

        if (alreadyFetched) {
            Log.d("MainViewModel", "Tipo de cambio ya descargado previamente")
            return
        }

        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Solicitando tipo de cambio EUR->USD...")
                val response = RetrofitClient.api.convert("EUR", "USD", 1.0)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        euroToDollar = body.rates.usd
                        alreadyFetched = true
                        Log.d("MainViewModel", "Cambio: 1 EUR = ${euroToDollar} USD")
                    } else {
                        Log.e("MainViewModel", "Respuesta vacía")
                    }
                } else {
                    Log.e("MainViewModel", "Error HTTP: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("MainViewModel", "Excepción obteniendo el cambio", e)
            }
        }
    }
}