package com.example.prueba1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.prueba1.entiy.Categoria
import com.example.prueba1.entiy.Producto
import com.example.prueba1.entiy.ProductoConDetalles
import com.example.prueba1.entiy.Usuario

@Dao
interface AppDao {
    // Registro e Inicio de sesión real (Rúbrica 1.1)
    @Insert suspend fun insertUsuario(usuario: Usuario): Long
    @Query("SELECT * FROM usuarios WHERE username = :user AND password = :pass LIMIT 1")
    suspend fun login(user: String, pass: String): Usuario?

    @Insert suspend fun insertCategoria(categoria: Categoria): Long
    @Query("SELECT * FROM categorias")
    suspend fun getCategorias(): List<Categoria>

    // Relación de productos con Usuario y Categoría (Rúbrica 2.3)
    @Insert suspend fun insertProducto(producto: Producto): Long
    
    @Transaction
    @Query("SELECT * FROM productos WHERE usuarioId = :userId")
    suspend fun getProductosByUser(userId: Int): List<ProductoConDetalles>
}