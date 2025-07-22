package udemy.appdev.complaintscompiler

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController, authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login") {

        // Home screen with animation + FAB
        composable("login"){
            LoginScreen(navController, authViewModel)
        }
        composable("signup"){
            SignupScreen(navController, authViewModel)
        }
        composable("home") {
            HomeScreen(navController)
        }

        // Complaint Form screen
        composable("form") {
            ComplaintFormScreenWithScaffold(navController)
        }
        composable("admin"){
            DisplayReportsScreen(navController)
        }
        composable("my_complaints"){
            MyComplaints(navController)
        }
        composable("my_profile"){
            MyProfileScreen(navController)
        }
        composable("dev_info"){
            DeveloperInfoScreen(navController)
        }

    }
}