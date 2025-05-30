package com.example.aplicacioncrud

import com.example.aplicacioncrud.LoginResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Ajuste de insets visuales
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los elementos de la vista
        val usuarioEditText = findViewById<EditText>(R.id.txtUsuario)
        val contraEditText = findViewById<EditText>(R.id.txtPassword)
        val btnIniciar = findViewById<Button>(R.id.btnEntrar)

        btnIniciar.setOnClickListener {
            val usuario = usuarioEditText.text.toString()
            val contra = contraEditText.text.toString()

            if (usuario.isEmpty() || contra.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val call = ApiClient.apiService.loginUser(usuario, contra)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.success == true) {
                            Toast.makeText(this@Login, "Bienvenido ${body.nombre}", Toast.LENGTH_LONG).show()
                            // Ir a otra pantalla
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login, body?.mensaje ?: "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Login, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@Login, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
