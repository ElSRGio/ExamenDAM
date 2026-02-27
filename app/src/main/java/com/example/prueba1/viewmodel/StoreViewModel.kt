package com.example.prueba1.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba1.dao.AppDao
import com.example.prueba1.entiy.Producto
import com.example.prueba1.entiy.ProductoConDetalles
import com.example.prueba1.entiy.Usuario
import java.io.File
import kotlinx.coroutines.launch

class StoreViewModel(private val dao: AppDao, private val context: Context) : ViewModel() {
    var currentUser by mutableStateOf<Usuario?>(null)
    var fotoUri by mutableStateOf<Uri?>(null)
    var productos = mutableStateListOf<ProductoConDetalles>()

    fun login(u: String, p: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = dao.login(u, p)
            if (user != null) {
                currentUser = user
                loadProductos()
                onResult(true)
            } else onResult(false)
        }
    }

    fun loadProductos() {
        currentUser?.let {
            viewModelScope.launch {
                productos.clear()
                productos.addAll(dao.getProductosByUser(it.id))
            }
        }
    }

    fun crearProducto(nombre: String, desc: String, catId: Int) {
        viewModelScope.launch {
            val nuevo = Producto(
                nombre = nombre,
                descripcion = desc,
                fotoPath = fotoUri?.toString(),
                categoriaId = catId,
                usuarioId = currentUser!!.id
            )
            dao.insertProducto(nuevo)
            loadProductos()
        }
    }

    // Generar URI para la cámara [cite: 28, 30]
    fun getTmpUri(): Uri {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foto_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        fotoUri = uri
        return uri
    }
}