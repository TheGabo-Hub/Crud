package com.example.aplicacioncrud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Op_editar : Fragment() {
    private val client = OkHttpClient()

    // Declarar variables globales para poder usarlas en cualquier función
    private lateinit var etBuscarID: EditText
    private lateinit var etID: TextView
    private lateinit var etNombre: TextView
    private lateinit var etStock: TextView
    private lateinit var etPassword: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_op_editar, container, false)

        // Inicializar campos de texto
        etBuscarID = view.findViewById(R.id.txtIdProducto)
        etID = view.findViewById(R.id.txtID)
        etNombre = view.findViewById(R.id.txtNombre)
        etStock = view.findViewById(R.id.txtStock)
        etPassword = view.findViewById(R.id.txtPrecio)

        val btnBuscar = view.findViewById<Button>(R.id.bDelBuscar)
        val btnActualizar = view.findViewById<Button>(R.id.bDelEliminar)
        val btnLimpiar = view.findViewById<Button>(R.id.bDelLimpiar)

        // Botón buscar
        btnBuscar.setOnClickListener {
            val id = etBuscarID.text.toString().trim()
            if (id.isNotEmpty()) {
                buscarProducto(id, etID, etNombre, etStock, etPassword)
            } else {
                Toast.makeText(requireContext(), "⚠️ Ingresa un ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón actualizar
        btnActualizar.setOnClickListener {
            val id = etID.text.toString()
            val nombre = etNombre.text.toString()
            val stock = etStock.text.toString()
            val precio = etPassword.text.toString()

            if (id.isNotEmpty() && nombre.isNotEmpty() && stock.isNotEmpty() && precio.isNotEmpty()) {
                actualizarProducto(id.toInt(), nombre, stock.toInt(), precio.toDouble())
            } else {
                Toast.makeText(requireContext(), "⚠️ Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón limpiar manual
        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        return view
    }

    // Función para limpiar todos los campos
    private fun limpiarCampos() {
        etBuscarID.text.clear()
        etID.text = ""
        etNombre.text = ""
        etStock.text = ""
        etPassword.text = ""
    }

    // Función para buscar un usuario
    private fun buscarProducto(id: String, txtID: TextView, txtNombre: TextView, txtStock: TextView, txtPrecio: TextView) {
        // construye la URL con los parámetros

        // http://74.207.235.149/buscar_producto?id=1
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // Cambia si usas otro host
            .addPathSegment("buscar_producto.php")
            .addQueryParameter("id", id)
            .build()

        // crea la solicitud
        val request = Request.Builder().url(url).get().build()
        // lanza la corrutina

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // ejecuta la solicitud
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseBody != null) {
                        val json = JSONObject(responseBody)
                        if (json.optBoolean("success", false)) {
                            val usuario = json.getJSONObject("producto")
                            // Actualiza los TextView con los datos del usuario
                            // ----------------------------> EL NOMBRE DELA COLUMNA
                            txtID.text = usuario.optString("id", "")
                            txtNombre.text = usuario.optString("nombre", "")
                            txtStock.text = usuario.optString("stock", "")
                            txtPrecio.text = usuario.optString("precio", "")
                        } else {
                            Toast.makeText(requireContext(), "❌ Producto no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "❌ Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "❌ Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    // Función para actualizar los datos de un usuario
    private fun actualizarProducto(id: Int, nombre: String, stock: Int, precio: Double) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149")
            .addPathSegment("editar_producto.php")
            .build()

        val formBody = FormBody.Builder()
            .add("id", id.toString())
            .add("nombre", nombre)
            .add("stock", stock.toString())
            .add("precio", precio.toString())
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
                        val json = JSONObject(responseBody)
                        val mensaje = json.optString("mensaje", "Sin mensaje")
                        if (json.optBoolean("success", false)) {
                            Toast.makeText(requireContext(), "✅ $mensaje", Toast.LENGTH_SHORT).show()
                            limpiarCampos() // 👉 Limpiar solo si fue exitoso
                        } else {
                            Toast.makeText(requireContext(), "❌ $mensaje", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "❌ Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
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
