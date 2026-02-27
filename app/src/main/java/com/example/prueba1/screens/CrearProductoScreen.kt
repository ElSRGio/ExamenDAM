package com.example.prueba1.screens

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