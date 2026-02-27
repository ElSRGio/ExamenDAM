package com.example.prueba1.entiy

import androidx.room.*

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "email") val email: String
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
    ],
    indices = [Index(value = ["usuarioId"]), Index(value = ["categoriaId"])]
)
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    @ColumnInfo(name = "fotoPath") val fotoPath: String?,
    val categoriaId: Int,
    val usuarioId: Int
)

// Objeto de Transferencia de Datos (DTO) para la relación
data class ProductoConDetalles(
    @Embedded val producto: Producto,
    @Relation(parentColumn = "categoriaId", entityColumn = "id")
    val categoria: Categoria,
    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: Usuario
)
