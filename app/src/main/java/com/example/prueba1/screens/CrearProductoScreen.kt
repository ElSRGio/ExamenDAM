package com.example.prueba1.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prueba1.viewmodel.StoreViewModel

@Composable
fun CrearProductoScreen(vm: StoreViewModel, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (!success) vm.fotoUri = null
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Producto") })

        // Preview de Imagen con Coil [cite: 40, 51]
        if (vm.fotoUri != null) {
            AsyncImage(model = vm.fotoUri, contentDescription = null, modifier = Modifier.size(200.dp))
        } else {
            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(200.dp)) // Placeholder [cite: 39]
        }

        Button(onClick = { cameraLauncher.launch(vm.getTmpUri()) }) {
            Text("Tomar Foto")
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