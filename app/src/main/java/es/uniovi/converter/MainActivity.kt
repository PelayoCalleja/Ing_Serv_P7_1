package es.uniovi.converter

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEuros: EditText
    private lateinit var editTextDollars: EditText

    // Asociamos el ViewModel a la Activity
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEuros = findViewById(R.id.editTextEuros)
        editTextDollars = findViewById(R.id.editTextDollars)

        // Descarga el tipo de cambio solo la primera vez
        viewModel.fetchExchangeRate()
    }

    fun onClickToDollars(view: View) {
        val factor = viewModel.euroToDollar
        convert(editTextEuros, editTextDollars, factor)
    }

    fun onClickToEuros(view: View) {
        val factor = if (viewModel.euroToDollar != 0.0) {
            1.0 / viewModel.euroToDollar
        } else {
            0.0
        }
        convert(editTextDollars, editTextEuros, factor)
    }

    private fun convert(source: EditText, destination: EditText, factor: Double) {
        val text = source.text.toString().trim()

        if (text.isEmpty()) {
            destination.setText("")
            Toast.makeText(this, "Introduce una cantidad", Toast.LENGTH_SHORT).show()
            return
        }

        val value = text.toDoubleOrNull()
        if (value == null) {
            destination.setText("")
            Toast.makeText(this, "Valor no válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (factor == 0.0) {
            destination.setText("")
            Toast.makeText(this, "Tipo de cambio no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val result = value * factor
        val rounded = String.format("%.2f", result)
        destination.setText(rounded)
    }
}
