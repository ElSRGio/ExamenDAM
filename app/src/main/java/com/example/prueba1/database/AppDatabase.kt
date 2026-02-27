package com.example.prueba1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prueba1.dao.AppDao
import com.example.prueba1.entiy.Categoria
import com.example.prueba1.entiy.Producto
import com.example.prueba1.entiy.Usuario

@Database(entities = [Usuario::class, Categoria::class, Producto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}