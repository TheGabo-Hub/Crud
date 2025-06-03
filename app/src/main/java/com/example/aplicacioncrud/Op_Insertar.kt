package com.example.aplicacioncrud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class Op_Insertar : Fragment() {
    private val client = OkHttpClient()


    // Declarar variables globales para poder usarlas en cualquier función
    private lateinit var txtNombre: EditText
    private lateinit var txtStock: EditText
    private lateinit var txtPrecio: EditText
    private lateinit var etUser: TextView
    private lateinit var etPassword: TextView






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_op__insertar, container, false)

         txtNombre = view.findViewById<EditText>(R.id.txtNombre)
        txtStock = view.findViewById<EditText>(R.id.txtStock)
        txtPrecio = view.findViewById<EditText>(R.id.txtPrecio)
        val btnRegistrar = view.findViewById<Button>(R.id.btnregistrar)
        val btnLimpiar = view.findViewById<Button>(R.id.btnlimpiar)

        btnRegistrar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val usuario = txtStock.text.toString().trim()
            val precio = txtPrecio.text.toString().trim()

            if (nombre.isNotEmpty() && usuario.isNotEmpty() && precio.isNotEmpty()) {
                registrarProductos(nombre, usuario, precio)
            } else {
                Toast.makeText(requireContext(), "⚠️ Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }



        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        return view
    }
    private fun limpiarCampos()
    {
        txtNombre.text.clear()
        txtStock.text.clear()
        txtPrecio.text.clear()
    }

    private fun registrarProductos(nombre: String, usuario: String, password: String) {
        val url = "http://74.207.235.149/registrar_productos.php" // Cambia si usas dominio propio

        val formBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("stock", usuario)
            .add("precio", password)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseBody != null) {
                        Toast.makeText(requireContext(), responseBody, Toast.LENGTH_LONG).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(requireContext(), "❌ Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "❌ Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
