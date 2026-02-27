package com.example.prueba1.screens

import android.Manifest
import android.net.Uri
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
import com.example.prueba1.entiy.Producto
import com.example.prueba1.viewmodel.StoreViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(vm: StoreViewModel, navController: NavController, productoId: Int) {
    var nombre by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var precioTexto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Electrónica") }
    var expandedMenu by remember { mutableStateOf(false) }
    val opcionesTipo = listOf("Electrónica", "Línea Blanca", "Muebles", "Ropa", "Calzado")

    var productoActual by remember { mutableStateOf<Producto?>(null) }
    var mostrarDialogoGuardar by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        hasCameraPermission = permissions[Manifest.permission.CAMERA] ?: false
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {}

    LaunchedEffect(Unit) { permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA)) }

    LaunchedEffect(productoId) {
        val prod = vm.obtenerProducto(productoId)
        if (prod != null) {
            productoActual = prod
            nombre = prod.nombre
            desc = prod.descripcion
            precioTexto = prod.precio.toString()
            tipo = prod.tipo

            // Si hay una foto vieja en BD, la convertimos a formato URI para la vista previa
            if (vm.fotoUri == null && prod.fotoPath != null) {
                vm.fotoUri = Uri.fromFile(File(prod.fotoPath))
            }
        }
    }

    if (mostrarDialogoGuardar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoGuardar = false },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que deseas aplicar estos cambios al producto?") },
            confirmButton = {
                TextButton(onClick = {
                    if (productoActual != null) {
                        // --- LA CLAVE: Si tomó foto nueva, usamos la ruta física, si no, conservamos la vieja
                        val rutaFinalDb = if (vm.rutaRealFisica != null) vm.rutaRealFisica else productoActual!!.fotoPath

                        val prodActualizado = productoActual!!.copy(
                            nombre = nombre, descripcion = desc,
                            precio = precioTexto.toDoubleOrNull() ?: 0.0,
                            tipo = tipo,
                            fotoPath = rutaFinalDb
                        )
                        vm.actualizarProducto(prodActualizado)
                        vm.limpiarFoto()
                        mostrarDialogoGuardar = false
                        navController.popBackStack()
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogoGuardar = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Producto") },
                navigationIcon = {
                    IconButton(onClick = {
                        vm.limpiarFoto()
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

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = precioTexto, onValueChange = { precioTexto = it }, label = { Text("Precio $") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedMenu, onExpandedChange = { expandedMenu = !expandedMenu }, modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = tipo, onValueChange = {}, readOnly = true, label = { Text("Departamento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
                        opcionesTipo.forEach { seleccion ->
                            DropdownMenuItem(text = { Text(seleccion) }, onClick = { tipo = seleccion; expandedMenu = false })
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
            Button(onClick = { if (hasCameraPermission) cameraLauncher.launch(vm.generarUriCamara()) }) { Text("Cambiar Foto") }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { mostrarDialogoGuardar = true },
                enabled = nombre.isNotBlank() && desc.isNotBlank() && precioTexto.isNotBlank()
            ) { Text("Guardar Cambios") }
        }
    }
}