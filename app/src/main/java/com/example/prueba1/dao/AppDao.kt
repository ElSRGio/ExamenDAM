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
    @Insert
    suspend fun insertUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE username = :user AND password = :pass LIMIT 1")
    suspend fun login(user: String, pass: String): Usuario?

    @Insert
    suspend fun insertCategoria(categoria: Categoria)

    @Insert
    suspend fun insertProducto(producto: Producto)

    @Transaction
    @Query("SELECT * FROM productos WHERE usuarioId = :userId")
    suspend fun getProductosByUser(userId: Int): List<ProductoConDetalles>
    @androidx.room.Delete
    suspend fun deleteProducto(producto: Producto)

    @androidx.room.Update
    suspend fun updateProducto(producto: Producto)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun getProductoById(id: Int): Producto?
}