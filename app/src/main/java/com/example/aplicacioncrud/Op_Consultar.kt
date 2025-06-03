package com.example.aplicacioncrud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Op_Consultar : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_op__consultar, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewConsulta)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        consultarUsuarios()

        val btnLimpiar = view.findViewById<Button>(R.id.btnLimpiar)
        btnLimpiar.setOnClickListener {
            recyclerView.adapter = ProductoAdapter(emptyList())
        }

        return view
    }

    private fun consultarUsuarios() {
        val url = "http://74.207.235.149/mostrar_productos.php"

        val request = Request.Builder().url(url).get().build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && responseBody != null) {
                        val json = JSONObject(responseBody)
                        if (json.optBoolean("success", false)) {
                            val listaProductos = mutableListOf<Producto>()
                            val usuarios = json.getJSONArray("productos")
                            for (i in 0 until usuarios.length()) {
                                val u = usuarios.getJSONObject(i)
                                listaProductos.add(
                                    Producto(
                                        u.getString("id"),
                                        u.getString("nombre"),
                                        u.getInt("stock"),
                                        u.getDouble("precio")
                                    )
                                )
                            }
                            adapter = ProductoAdapter(listaProductos)
                            recyclerView.adapter = adapter
                        } else {
                            Toast.makeText(requireContext(), "❌ Sin registros", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "❌ Error de servidor", Toast.LENGTH_SHORT).show()
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

