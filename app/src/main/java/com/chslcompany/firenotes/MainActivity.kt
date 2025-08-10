package com.chslcompany.firenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chslcompany.firenotes.navigation.AuthNavGraph
import com.chslcompany.firenotes.navigation.BaseNavGraph
import com.chslcompany.firenotes.navigation.NotesNavGraph
import com.chslcompany.firenotes.navigation.SharedNoteNavGraph
import com.chslcompany.firenotes.ui.theme.FireNotesTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireNotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination =
                            if (firebaseAuth.currentUser == null) AuthNavGraph.Dest.Root
                            else NotesNavGraph.Dest.Root
                    ) {
                        listOf<BaseNavGraph>(AuthNavGraph, NotesNavGraph, SharedNoteNavGraph)
                            .forEach {
                                it.build(
                                    modifier = Modifier.padding(innerPadding),
                                    navHostController = navController,
                                    navGraphBuilder = this
                                )
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FireNotesTheme {
        Greeting("Android")
    }
}