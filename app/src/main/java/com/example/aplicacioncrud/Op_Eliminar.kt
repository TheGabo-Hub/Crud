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
        val view = inflater.inflate(R.layout.fragment_op__eliminar, container, false)

        val etBuscarID = view.findViewById<EditText>(R.id.txtIdProducto)
        val txtID = view.findViewById<TextView>(R.id.txtID)
        val txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        val txtUser = view.findViewById<TextView>(R.id.txtStock)
        val txtPassword = view.findViewById<TextView>(R.id.txtPrecio)

        val btnBuscar = view.findViewById<Button>(R.id.bDelBuscar)
        val btnEliminar = view.findViewById<Button>(R.id.bDelEliminar)
        val btnLimpiar = view.findViewById<Button>(R.id.bDelLimpiar)

        btnBuscar.setOnClickListener {
            val id = etBuscarID.text.toString().trim()
            if (id.isNotEmpty()) {
                buscarUsuario(id, txtID, txtNombre, txtUser, txtPassword)
            } else {
                Toast.makeText(requireContext(), "⚠️ Ingresa un ID", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            val id = txtID.text.toString()
            if (id.isNotEmpty()) {
                eliminarUsuario(id) {
                    // Limpiar campos si se eliminó
                    txtID.text = ""
                    txtNombre.text = ""
                    txtUser.text = ""
                    txtPassword.text = ""
                    etBuscarID.setText("")
                }
            } else {
                Toast.makeText(requireContext(), "⚠️ Busca un usuario primero", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            etBuscarID.setText("")
            txtID.text = ""
            txtNombre.text = ""
            txtUser.text = ""
            txtPassword.text = ""
        }

        return view
    }

    private fun buscarUsuario(id: String, txtID: TextView, txtNombre: TextView, txtUser: TextView, txtPassword: TextView) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // Cambia si usas otro host
            .addPathSegment("buscar_usuario.php")
            .addQueryParameter("id", id)
            .build()

        val request = Request.Builder().url(url).get().build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseBody != null) {
                        val json = JSONObject(responseBody)
                        if (json.optBoolean("success", false)) {
                            val usuario = json.getJSONObject("usuario")
                            txtID.text = usuario.optString("id", "")
                            txtNombre.text = usuario.optString("nombre", "")
                            txtUser.text = usuario.optString("usuario", "")
                            txtPassword.text = usuario.optString("contrasena", "")
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

    private fun eliminarUsuario(id: String, onSuccess: () -> Unit) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // Cambia si usas otro host
            .addPathSegment("eliminar_usuario.php")
            .addQueryParameter("id", id)
            .build()

        val request = Request.Builder().url(url).get().build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

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
