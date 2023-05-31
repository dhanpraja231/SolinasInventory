package com.fyp.solinosinventory.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.fyp.solinosinventory.LoginActivity
import com.fyp.solinosinventory.R
import com.fyp.solinosinventory.Screens


data class NavDrawerItem (
    val id:String,
    val title:String,
    val contentDescription:String,
    val icon: ImageVector
)



@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colors.primary)
        //.padding(vertical = 24.dp),
        ,contentAlignment = Alignment.Center
    ){
        Image(painterResource(R.drawable.solinasbackdrop),"content description")
        //Text(text="Replace image",fontSize = 40.sp)
    }
}

@Composable
fun DrawerBody(
    //items: List<NavDrawerItem>,
    //will need the navigation controller and context
    navController: NavController,
    context: Context,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    //   onItemCLick: (NavDrawerItem) -> Unit
) {

    val items =
        listOf(
            NavDrawerItem(
                id = "Home Screen",
                title = "Products",
                contentDescription = "Go to User home screen",
                icon = Icons.Default.Home
            ),

            NavDrawerItem(
                id = "Vendor",
                title = "Vendors",
                contentDescription = "Vendors",
                icon = Icons.Default.ShoppingCart
            ),
            NavDrawerItem(
                id = "SignOut",
                title = "Sign out",
                contentDescription = "Sign out",
                icon = Icons.Default.ExitToApp
            ),

        )

    val onItemCLick: (NavDrawerItem, NavController) -> Unit = {
            it, navController ->

        when(it.id){
            "SignOut" -> {
                signOut(context)
                val intent = Intent(context, LoginActivity::class.java)
                ContextCompat.startActivity(context, intent, null)
            }
            "Vendor"-> {
                navController.navigate(Screens.VendorScreen.route)
            }
            "Home Screen"->{
                navController.navigate(Screens.ProductScreen.route)
            }
//
        }

    }



    LazyColumn(modifier){
        items(items){
                item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onItemCLick(item,navController)
                    }
            ){
                Icon(imageVector = item.icon, contentDescription = item.contentDescription )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title,
                    modifier = Modifier.weight(1f))
            }
        }
    }

}