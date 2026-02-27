package com.example.prueba1.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.prueba1.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearProductoScreen(vm: StoreViewModel, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") } // Para el teclado numérico
    var tipo by remember { mutableStateOf("Electrónica") } // Menú desplegable
    var expandedMenu by remember { mutableStateOf(false) }
    val opcionesTipo = listOf("Electrónica", "Línea Blanca", "Muebles", "Ropa", "Calzado")

    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        hasCameraPermission = it[Manifest.permission.CAMERA] ?: false
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (!success) { vm.fotoUri = null }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = {
                        vm.fotoUri = null
                        navController.popBackStack()
                    }) { Icon(Icons.Default.ArrowBack, contentDescription = "Regresar") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Producto") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))

            // Fila para el Precio y el Menú Desplegable
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Campo de Precio con TECLADO NUMÉRICO
                TextField(
                    value = precioTexto,
                    onValueChange = { precioTexto = it },
                    label = { Text("Precio $") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                // Menú Desplegable de Tipo
                ExposedDropdownMenuBox(
                    expanded = expandedMenu,
                    onExpandedChange = { expandedMenu = !expandedMenu },
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Departamento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false }
                    ) {
                        opcionesTipo.forEach { seleccion ->
                            DropdownMenuItem(
                                text = { Text(seleccion) },
                                onClick = {
                                    tipo = seleccion
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (vm.fotoUri != null) {
                AsyncImage(model = vm.fotoUri, contentDescription = "Foto", modifier = Modifier.size(150.dp))
            } else {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Placeholder", modifier = Modifier.size(150.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { if (hasCameraPermission) cameraLauncher.launch(vm.generarUriCamara()) }) { Text("Tomar Foto del Producto") }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val precioDouble = precioTexto.toDoubleOrNull() ?: 0.0
                    vm.crearProducto(nombre, desc, precioDouble, tipo, 1)
                    navController.navigate("home") { popUpTo("crear") { inclusive = true } }
                },
                enabled = nombre.isNotBlank() && desc.isNotBlank() && precioTexto.isNotBlank()
            ) { Text("Guardar en Coppel Inventario") }
        }
    }
}