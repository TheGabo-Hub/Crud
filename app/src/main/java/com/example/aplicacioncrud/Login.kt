package com.example.aplicacioncrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Login : AppCompatActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<EditText>(R.id.txtUsuario)
        val etContrasena = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnEntrar)
        val tvResultado = findViewById<TextView>(R.id.txtResultado)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
                iniciarSesion(usuario, contrasena, tvResultado)
            } else {
                tvResultado.text = "Completa todos los campos"
            }
        }
    }

    private fun iniciarSesion(usuario: String, contrasena: String, resultado: TextView) {
        // ✅ Asegúrate de usar tu dominio real aquí
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // por ejemplo: losgabos.free.nf
            .addPathSegment("login.php")
            .addQueryParameter("usuario", usuario)
            .addQueryParameter("contrasena", contrasena)
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val json = JSONObject(responseBody)
                    val exito = json.optBoolean("success", false)
                    val mensaje = json.optString("mensaje", "Sin mensaje")

                    withContext(Dispatchers.Main) {
                        resultado.text = if (exito) "✅ Bienvenido: $mensaje" else "❌ Error: $mensaje"
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        resultado.text = "❌ Error en la respuesta del servidor"
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    resultado.text = "❌ Error de red: ${e.message}"
                }
            }
        }
    }
}
