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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_op__insertar, container, false)

        val txtNombre = view.findViewById<EditText>(R.id.txtNombre)
        val txtUsuario = view.findViewById<EditText>(R.id.txtUsuario)
        val txtPassword = view.findViewById<EditText>(R.id.txtPassword)
        val btnRegistrar = view.findViewById<Button>(R.id.btnregistrar)
        val btnLimpiar = view.findViewById<Button>(R.id.btnlimpiar)

        btnRegistrar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val usuario = txtUsuario.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (nombre.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty()) {
                registrarUsuario(nombre, usuario, password)
            } else {
                Toast.makeText(requireContext(), "⚠️ Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            txtNombre.text.clear()
            txtUsuario.text.clear()
            txtPassword.text.clear()
        }

        return view
    }

    private fun registrarUsuario(nombre: String, usuario: String, password: String) {
        val url = "http://74.207.235.149/registrar.php" // Cambia si usas dominio propio

        val formBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("usuario", usuario)
            .add("contrasena", password)
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
