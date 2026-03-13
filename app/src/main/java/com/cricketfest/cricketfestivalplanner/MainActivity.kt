package com.cricketfest.cricketfestivalplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.cricketfest.cricketfestivalplanner.ui.navigation.AppNavGraph
import com.cricketfest.cricketfestivalplanner.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = koinViewModel()
            val theme by viewModel.theme.collectAsState()
            AppTheme(theme = theme) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}
