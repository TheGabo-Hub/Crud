package com.example.aplicacioncrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(private val listaUsuarios: List<Usuario>) :
    RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.txtItemID)
        val nombre: TextView = itemView.findViewById(R.id.txtItemNombre)
        val usuario: TextView = itemView.findViewById(R.id.txtItemUsuario)
        val password: TextView = itemView.findViewById(R.id.txtItemPassword)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.id.text = usuario.id
        holder.nombre.text = usuario.nombre
        holder.usuario.text = usuario.usuario
        holder.password.text = usuario.contrasena
    }

    override fun getItemCount(): Int = listaUsuarios.size
}
