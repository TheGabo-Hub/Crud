package com.example.aplicacioncrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(private val listaProductos: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.txtItemID)
        val nombre: TextView = itemView.findViewById(R.id.txtItemNombre)
        val stock: TextView = itemView.findViewById(R.id.txtItemStock)
        val precio: TextView = itemView.findViewById(R.id.txtItemPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaProductos[position]
        holder.id.text = usuario.id
        holder.nombre.text = usuario.nombre
        holder.stock.text = usuario.stock.toString()
        holder.precio.text = usuario.precio.toString()
    }

    override fun getItemCount(): Int = listaProductos.size
}
