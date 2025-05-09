package com.tecsup.datossinmvvm
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    var db: UserDatabase
    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser = remember { mutableStateOf("") }

    db = crearDatabase(context)
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    // Scaffold con TopAppBar
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
                actions = {
                    // Botón Agregar Usuario
                    Button(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            AgregarUsuario(user = user, dao = dao)
                        }
                        firstName = ""
                        lastName = ""
                    }) {
                        Text("Agregar Usuario")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Botón Listar Usuarios
                    Button(onClick = {
                        coroutineScope.launch {
                            val data = getUsers(dao = dao)
                            dataUser.value = data
                        }
                    }) {
                        Text("Listar Usuarios")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Botón Eliminar Último Usuario
                    Button(onClick = {
                        coroutineScope.launch {
                            dao.deleteLastUser() // Elimina el último usuario
                            dataUser.value = "Último usuario eliminado"
                        }
                    }) {
                        Text("Eliminar Último")
                    }
                }
            )
        },
        content = {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(50.dp))

                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID (solo lectura)") },
                    enabled = false,  // Cambiar a enabled false
                    singleLine = true
                )

                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name: ") },
                    singleLine = true
                )

                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name:") },
                    singleLine = true
                )

                // Mostrar la lista de usuarios o el mensaje
                Text(
                    text = dataUser.value, fontSize = 20.sp
                )
            }
        }
    )
}

@Composable
fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta: String = ""
    val users = dao.getAll()
    users.forEach { user ->
        val fila = user.firstName + " - " + user.lastName + "\n"
        rpta += fila
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error: insert: ${e.message}")
    }
}
