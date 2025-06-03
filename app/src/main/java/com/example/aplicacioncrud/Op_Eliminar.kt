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

class Op_Eliminar : Fragment() {
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // desde este fragment va a relacionar las variables con el layout
        val view = inflater.inflate(R.layout.fragment_op__eliminar, container, false)
        // variables creadas ya relacionadas
        val etBuscarID = view.findViewById<EditText>(R.id.txtIdProducto)
        val txtID = view.findViewById<TextView>(R.id.txtID)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtStock = view.findViewById<TextView>(R.id.txtStock)
        val txtPrecio = view.findViewById<TextView>(R.id.txtPrecio)

        val btnBuscar = view.findViewById<Button>(R.id.bDelBuscar)
        val btnEliminar = view.findViewById<Button>(R.id.bDelEliminar)
        val btnLimpiar = view.findViewById<Button>(R.id.bDelLimpiar)

        // Botón para buscar
        btnBuscar.setOnClickListener {
            //
            val id = etBuscarID.text.toString().trim()
            if (id.isNotEmpty()) {
                // Llama a la función para buscar el usuario
                buscarProducto(id, txtID, txtNombre, txtStock, txtPrecio)
            } else {
                Toast.makeText(requireContext(), "⚠️ Ingresa un ID", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            val id = txtID.text.toString()
            if (id.isNotEmpty()) {
                eliminarProducto (id) {
                    // Limpiar campos si se eliminó
                    txtID.text = ""
                    txtNombre.text = ""
                    txtStock.text = ""
                    txtPrecio.text = ""
                    etBuscarID.setText("")
                }
            } else {
                Toast.makeText(requireContext(), "⚠️ Busca un producto primero", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            etBuscarID.setText("")
            txtID.text = ""
            txtNombre.text = ""
            txtStock.text = ""
            txtPrecio.text = ""
        }

        return view
    }

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
                            Toast.makeText(requireContext(), "❌ Usuario no encontrado", Toast.LENGTH_SHORT).show()
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

    private fun eliminarProducto(id: String, onSuccess: () -> Unit) {

        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // Cambia si usas otro host
            .addPathSegment("eliminar_producto.php")
            .addQueryParameter("id", id)
            .build()
        // crea la solicitud
        val request = Request.Builder().url(url).get().build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                //
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseBody != null) {
                        val json = JSONObject(responseBody)
                        val mensaje = json.optString("mensaje", "Sin mensaje")
                        if (json.optBoolean("success", false)) {
                            Toast.makeText(requireContext(), "🗑️ $mensaje", Toast.LENGTH_SHORT).show()
                            onSuccess()
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
