package com.example.laboratorio09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        content = { paddingValues -> Contenido(paddingValues, navController) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    TopAppBar(
        title = {
            Text(
                "Trama",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
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
            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
            label = { Text("Publicaciones") },
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
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(26.dp)) {
                Text(
                    "Una lectura lleva a otra.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(18.dp))
                Text(
                    "Explora las publicaciones de JSONPlaceholder y descubre cada historia a tu ritmo.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = onOpenPosts,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Explorar publicaciones")
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Contenido de JSONPlaceholder Mock",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
