package com.example.aplicacioncrud

import android.content.Intent // <-- IMPORTANTE
import android.os.Bundle
<<<<<<< HEAD
import android.widget.Button
=======
import android.view.View
>>>>>>> 59ade810278d3cb5370ca0a58d1961a77696d526
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.aplicacioncrud.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

<<<<<<< HEAD
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
=======
        var drawerLayout=findViewById<DrawerLayout>(R.id.drawellayout)
        var tollbar=findViewById<MaterialToolbar>(R.id.tollbar)
        val navigationView = findViewById<NavigationView>(R.id.navigation)

        tollbar.setOnClickListener(View.OnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        })
        replaceFragment(Op_Consultar())

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.consultar -> replaceFragment(Op_Consultar())
                R.id.Agregar -> replaceFragment(Op_Insertar())
                R.id.Editar -> replaceFragment(Op_editar())
                R.id.Eliminar -> replaceFragment(Op_Eliminar())
                R.id.Logout -> finish()

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        }
    }
>>>>>>> 59ade810278d3cb5370ca0a58d1961a77696d526
