package com.example.prueba1.entiy

import androidx.room.*

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
    val email: String
)

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre_categoria: String
)

@Entity(
    tableName = "productos",
    foreignKeys = [
        ForeignKey(entity = Usuario::class, parentColumns = ["id"], childColumns = ["usuarioId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Categoria::class, parentColumns = ["id"], childColumns = ["categoriaId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val fotoPath: String?,
    val categoriaId: Int,
    val usuarioId: Int
)

// DTO para relación [cite: 36, 48]
data class ProductoConDetalles(
    @Embedded val producto: Producto,
    @Relation(parentColumn = "categoriaId", entityColumn = "id")
    val categoria: Categoria,
    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: Usuario
)