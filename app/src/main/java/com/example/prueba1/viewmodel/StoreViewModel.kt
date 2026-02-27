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

    private val prefs = context.getSharedPreferences("tienda_prefs", Context.MODE_PRIVATE)

    var currentUser by mutableStateOf<Usuario?>(null)
    var fotoUri by mutableStateOf<Uri?>(null)

    // --- NUEVO: Guardamos la ruta física real que nunca caduca ---
    var rutaRealFisica by mutableStateOf<String?>(null)

    var listaProductos = mutableStateListOf<ProductoConDetalles>()

    init {
        val savedUserId = prefs.getInt("user_id", -1)
        if (savedUserId != -1) {
            currentUser = Usuario(id = savedUserId, username = prefs.getString("user_name", "") ?: "", password = "", email = "")
            loadProductos()
        }

        val savedUri = prefs.getString("foto_uri", null)
        if (savedUri != null) {
            fotoUri = Uri.parse(savedUri)
            rutaRealFisica = prefs.getString("foto_path_real", null)
        }
    }

    // Función para limpiar la foto cuando cambiamos de pantalla
    fun limpiarFoto() {
        fotoUri = null
        rutaRealFisica = null
        prefs.edit().remove("foto_uri").remove("foto_path_real").apply()
    }

    fun login(u: String, p: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = dao.login(u, p)
            if (user != null) {
                currentUser = user
                prefs.edit().putInt("user_id", user.id).putString("user_name", user.username).apply()
                loadProductos()
                onResult(true)
            } else onResult(false)
        }
    }

    fun register(usuario: Usuario, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            dao.insertUsuario(usuario)
            onResult(true)
        }
    }

    fun loadProductos() {
        currentUser?.let { user ->
            viewModelScope.launch {
                listaProductos.clear()
                listaProductos.addAll(dao.getProductosByUser(user.id))
            }
        }
    }

    fun crearProducto(nombre: String, desc: String, precio: Double, tipo: String, catId: Int) {
        viewModelScope.launch {
            try {
                try { dao.insertCategoria(com.example.prueba1.entiy.Categoria(id = 1, nombre_categoria = "General")) } catch (e: Exception) {}

                if (currentUser != null) {
                    // --- LA SOLUCIÓN: Usamos la ruta física real para la Base de Datos ---
                    val rutaDb = rutaRealFisica ?: prefs.getString("foto_path_real", null)

                    val nuevo = Producto(
                        nombre = nombre, descripcion = desc, precio = precio, tipo = tipo,
                        fotoPath = rutaDb, categoriaId = 1, usuarioId = currentUser!!.id
                    )
                    dao.insertProducto(nuevo)

                    limpiarFoto() // Limpiamos todo rastro de la memoria
                    loadProductos()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun generarUriCamara(): Uri {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "foto_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        fotoUri = uri
        rutaRealFisica = file.absolutePath // Extraemos la ruta real (C:\...\foto.jpg)

        prefs.edit().putString("foto_uri", uri.toString()).putString("foto_path_real", file.absolutePath).apply()
        return uri
    }

    fun borrarProducto(producto: Producto) {
        viewModelScope.launch {
            try { dao.deleteProducto(producto); loadProductos() } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun actualizarProducto(productoActualizado: Producto) {
        viewModelScope.launch {
            try { dao.updateProducto(productoActualizado); loadProductos() } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun obtenerProducto(id: Int): Producto? {
        return dao.getProductoById(id)
    }
}