package com.example.prueba1.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) { /* La URI ya está en el ViewModel */ }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Producto") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = vm.fotoUri,
            contentDescription = "Foto",
            placeholder = painterResource(id = android.R.drawable.ic_menu_camera),
            error = painterResource(id = android.R.drawable.ic_menu_report_image),
            modifier = Modifier.size(120.dp)
        )

        Button(onClick = {
            if (hasCameraPermission) {
                val uri = vm.generarUriCamara()
                cameraLauncher.launch(uri)
            } else {
                // Opcional: Mostrar un mensaje al usuario
            }
        }) {
            Text("Tomar Foto del Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.crearProducto(nombre, desc, 1) // 1 asumiendo categoría default
                navController.navigate("home") {
                    popUpTo("crear") { inclusive = true }
                }
            },
            enabled = nombre.isNotBlank() // El botón se activa si el nombre no está vacío
        ) {
            Text("Guardar en Coppel Inventario")
        }
    }
}