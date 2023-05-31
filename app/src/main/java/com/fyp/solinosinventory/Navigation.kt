package com.fyp.solinosinventory

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fyp.solinosinventory.screens.*

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ){
        composable(route = Screens.ProductScreen.route) {
            ProductsScreen(navController = navController)
        }
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screens.ProductsSubCategoryScreen.route+"/{productName}", arguments = listOf(navArgument(name = "productName"){
            type = NavType.StringType
        })) {
            it.arguments?.getString("productName")
                ?.let { it -> ProductsSubCategoryScreen(navController = navController, productName = it) }
        }
        composable(route = Screens.InventoryScreen.route+"/{productName}", arguments = listOf(navArgument(name = "productName"){
            type = NavType.StringType
        })) {
            it.arguments?.getString("productName")
                ?.let { it -> InventoryScreen(navController = navController, subProductName = it) }
        }
        composable(route = Screens.VendorScreen.route) {
            VendorScreen(navController = navController)
        }
        composable(route = Screens.AddVendorScreen.route) {
            AddVendorScreen(navController = navController)
        }
    }



}

sealed class Screens(val route: String){

    //splash screen
    object SplashScreen: Screens("splash_screen")
    object TestScreen: Screens("test_screen")
    //app content
    object ProductScreen : Screens("product_screen")
    object ProductsSubCategoryScreen: Screens("products_sub_category")
    object InventoryScreen: Screens("inventory_screen")
    //vendor screen
    object VendorScreen: Screens("vendor_screen")
    object AddVendorScreen: Screens("add_vendor_screen")

}