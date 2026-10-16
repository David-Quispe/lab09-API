package com.example.laboratorio09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.laboratorio09.ui.ScreenPost
import com.example.laboratorio09.ui.ScreenPosts
import com.example.laboratorio09.ui.theme.Laboratorio09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio09Theme {
                ProgPrincipal9()
            }
        }
    }
}

@Composable
fun ProgPrincipal9() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues -> Contenido(paddingValues, navController) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(title = { Text("JSONPlaceholder Access") })
}

@Composable
fun BarraInferior(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route
    NavigationBar {
        NavigationBarItem(
            icon = { Text("⌂") },
            label = { Text("Inicio") },
            selected = route == "inicio",
            onClick = {
                navController.navigate("inicio") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Text("≡") },
            label = { Text("Posts") },
            selected = route == "posts" || route == "postsVer/{id}",
            onClick = {
                navController.navigate("posts") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}

@Composable
fun Contenido(pv: PaddingValues, navController: NavHostController) {
    Box(Modifier.fillMaxSize().padding(pv)) {
        NavHost(navController = navController, startDestination = "inicio") {
            composable("inicio") { ScreenInicio { navController.navigate("posts") } }
            composable("posts") { ScreenPosts(navController) }
            composable(
                "postsVer/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                ScreenPost(navController, entry.arguments?.getInt("id") ?: 0)
            }
        }
    }
}

@Composable
fun ScreenInicio(onOpenPosts: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Laboratorio 09", style = MaterialTheme.typography.headlineMedium)
        Text("Consulta publicaciones de JSONPlaceholder", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onOpenPosts, modifier = Modifier.padding(top = 20.dp)) {
            Text("Ver posts")
        }
    }
}
