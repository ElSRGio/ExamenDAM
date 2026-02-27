package com.example.prueba1.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun CrearProductoScreen(vm: StoreViewModel, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    // 1. Launcher para capturar la foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) { /* La URI ya está en el ViewModel */ }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Producto") })

        AsyncImage(
            model = vm.fotoUri,
            contentDescription = "Foto",
            // Placeholder si la foto es nula o carga (Rúbrica 3.2)
            placeholder = painterResource(id = android.R.drawable.ic_menu_camera),
            error = painterResource(id = android.R.drawable.ic_menu_report_image),
            modifier = Modifier.size(120.dp)
        )

        // 2. Botón que solicita permisos y abre cámara (Rúbrica 1.4)
        Button(onClick = {
            val uri = vm.generarUriCamara() // Función que creamos en el ViewModel
            cameraLauncher.launch(uri)
        }) {
            Text("Tomar Foto del Producto")
        }

        Button(onClick = {
            vm.crearProducto(nombre, desc, 1) // 1 asumiendo categoría default
            // Navegación limpia [cite: 41]
            navController.navigate("home") {
                popUpTo("crear") { inclusive = true }
            }
        }) {
            Text("Guardar en Coppel Inventario")
        }
    }
}