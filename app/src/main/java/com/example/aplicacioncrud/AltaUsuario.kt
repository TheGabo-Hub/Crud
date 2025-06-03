package com.example.aplicacioncrud

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException

class AltaUsuario : AppCompatActivity() {

    private lateinit var txtNombre: EditText
    private lateinit var txtUsuario: EditText
    private lateinit var txtPassword: EditText
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_usuario)

        txtNombre = findViewById(R.id.txtNombre)
        txtUsuario = findViewById(R.id.txtUsuario)
        txtPassword = findViewById(R.id.txtPassword)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)

        btnRegistrar.setOnClickListener {
            val nombre = txtNombre.text.toString().trim()
            val usuario = txtUsuario.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (nombre.isNotEmpty() && usuario.isNotEmpty() && password.isNotEmpty()) {
                registrarUsuario(nombre, usuario, password)
            } else {
                Toast.makeText(this, "⚠️ Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun limpiarCampos() {
        txtNombre.text.clear()
        txtUsuario.text.clear()
        txtPassword.text.clear()
    }

    private fun registrarUsuario(nombre: String, usuario: String, password: String) {
        val url = "http://74.207.235.149/registrar.php"
        val formBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("usuario", usuario)
            .add("password", password)
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
                        Toast.makeText(this@AltaUsuario, responseBody, Toast.LENGTH_LONG).show()
                        limpiarCampos()
                    } else {
                        Toast.makeText(this@AltaUsuario, "❌ Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AltaUsuario, "❌ Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
