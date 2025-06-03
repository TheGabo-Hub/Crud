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
    //    private lateinit var etUser: TextView
    //    private lateinit var etPassword: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // desde este fragment va a relacionar las variables con el layout
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
                // Llama a la función para registrar el producto
                registrarProductos(nombre, usuario, precio)
            } else {
                Toast.makeText(requireContext(), "⚠️ Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
        // Limpiar campos
        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
        // regresa la vista
        return view
    }
    // metodo para limpiar campos
    private fun limpiarCampos()
    {
        txtNombre.text.clear()
        txtStock.text.clear()
        txtPrecio.text.clear()
    }
    // metodo para registrar productos
    private fun registrarProductos(nombre: String, usuario: String, password: String) {
        // Construye la solicitud POST
        val url = "http://74.207.235.149/registrar_productos.php"
        // se construye el cuerpo de la solicitud POST
        val formBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("stock", usuario)
            .add("precio", password)
            .build()
        // se construye la solicitud
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        // Esto es un hilo de ejecución separado para la solicitud
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // se ejecuta la solicitud y se obtiene la respuesta
                val response = client.newCall(request).execute()
                // obtiene el cuerpo de la respuesta
                val responseBody = response.body?.string()
                // se ejecuta en el hilo principal para actualizar la interfaz de usuario
                withContext(Dispatchers.Main) {
                    // si la respuesta es exitosa, muestra un mensaje de éxito y limpia los campos
                    if (response.isSuccessful && responseBody != null) {
                        Toast.makeText(requireContext(), responseBody, Toast.LENGTH_LONG).show()
                        limpiarCampos()
                    } else {
                        // si la respuesta no es exitosa, muestra un mensaje de error
                        Toast.makeText(requireContext(), "❌ Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }
                // si ocurre una excepción, muestra un mensaje de error
            } catch (e: IOException) {
                // se ejecuta en el hilo principal para mostrar el mensaje de error
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "❌ Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
