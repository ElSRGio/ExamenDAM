package com.example.prueba1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.prueba1.database.AppDatabase
import com.example.prueba1.screens.CrearProductoScreen
import com.example.prueba1.screens.HomeScreen
import com.example.prueba1.screens.LoginScreen
import com.example.prueba1.screens.RegisterScreen
import com.example.prueba1.ui.theme.Prueba1Theme
import com.example.prueba1.viewmodel.StoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.prueba1.entiy.Categoria
import com.example.prueba1.screens.EditarProductoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Construcción de BD y adición de una categoría inicial por defecto
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tienda_db")
            .fallbackToDestructiveMigration() // <--- ESTA ES LA LÍNEA MÁGICA
            .build()
        val dao = db.appDao()

        CoroutineScope(Dispatchers.IO).launch {
            try { dao.insertCategoria(Categoria(id = 1, nombre_categoria = "General")) } catch (e: Exception) {}
        }

        setContent {
            Prueba1Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val vm: StoreViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return StoreViewModel(dao, applicationContext) as T
                        }
                    })

                    // Mapa de Navegación
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(vm, navController) }
                        composable("register") { RegisterScreen(vm, navController) }
                        composable("home") { HomeScreen(vm, navController) }
                        composable("crear") { CrearProductoScreen(vm, navController) }

                        // --- AGREGAR ESTA RUTA NUEVA ---
                        composable("editar/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            EditarProductoScreen(vm, navController, id)
                        }
                    }
                }
            }
        }
    }
}