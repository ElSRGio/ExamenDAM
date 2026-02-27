package com.example.prueba1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prueba1.dao.AppDao
import com.example.prueba1.entiy.Categoria
import com.example.prueba1.entiy.Producto
import com.example.prueba1.entiy.Usuario

// ¡AQUÍ ESTÁ LA MAGIA! Cambiamos version = 1 a version = 2
@Database(entities = [Usuario::class, Categoria::class, Producto::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}