package com.komal.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.komal.myapplication.screens.HomeScreen
import com.komal.myapplication.screens.ViewAllScreen
import com.komal.myapplication.screens.WelcomeScreen
@Composable
fun AppNavigation(navController: NavHostController){
  NavHost(
      navController=navController, startDestination = "welcome"
  ){
      composable("welcome"){
          WelcomeScreen(navController)
      }
      composable("home"){
          HomeScreen(navController)
      }
      composable(route="view all"){
          ViewAllScreen(navController)
      }
  }
}