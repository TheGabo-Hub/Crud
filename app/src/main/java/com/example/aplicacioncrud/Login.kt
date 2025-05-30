package com.example.aplicacioncrud

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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


        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
                iniciarSesion(usuario, contrasena)
            } else {
               Toast.makeText(this@Login, "⚠️ Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarSesion(usuario: String, contrasena: String) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host("74.207.235.149") // O tu dominio real
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
                        Toast.makeText(this@Login, mensaje, Toast.LENGTH_SHORT).show()

                        if (exito) {
                            Toast.makeText(this@Login, "🎉 Acceso correcto", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login, "🚫 Acceso denegado", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Login, "❌ Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Login, "❌ Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
