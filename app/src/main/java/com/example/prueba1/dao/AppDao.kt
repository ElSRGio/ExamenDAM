package com.example.prueba1.dao

@Dao
interface AppDao {
    @Insert suspend fun insertUsuario(usuario: Usuario)
    @Query("SELECT * FROM usuarios WHERE username = :user AND password = :pass LIMIT 1")
    suspend fun login(user: String, pass: String): Usuario?

    @Insert suspend fun insertCategoria(categoria: Categoria)
    @Query("SELECT * FROM categorias")
    suspend fun getCategorias(): List<Categoria>

    @Insert suspend fun insertProducto(producto: Producto)
    @Update suspend fun updateProducto(producto: Producto)
    @Query("SELECT * FROM productos WHERE usuarioId = :userId")
    suspend fun getProductosByUser(userId: Int): List<ProductoConDetalles>
}