package com.fyp.solinosinventory.screens


import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.fyp.solinosinventory.LoginActivity
//import com.fyp.meaucompose.screens.signOut
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun AppBar(onNavigationIconClick: () -> Unit, context: Context,
    onRefresh :() ->Unit,
           navController:NavController,
           titleText:String = "NA"
) {
    var userName = GoogleSignIn.getLastSignedInAccount(context)?.displayName.toString()
    if (userName.length > 10){
        userName = userName.substring(0,11)
    }

//    val isSignedIn by remember {
//        mutableStateOf(GoogleSignIn.getLastSignedInAccount(context))
//    }
//    val username by remember {
//        mutableStateOf(isSignedIn?.displayName.toString())
//    }

    TopAppBar(

        title = {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                val titleString = if(titleText.equals("NA"))  "Welcome ${userName}" else titleText
                Log.d("title text", titleString)
                Text(text = titleString,
                    fontSize = 18.sp,
                    maxLines = 1,
                    modifier = Modifier.widthIn(max = 190.dp),
                    //textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        onRefresh()
                    }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }


        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick){
                Icon(imageVector = Icons.Default.Menu
                    , contentDescription = "Toggle Drawer" )
            }
        }
    )
}

fun signOut(context:Context) {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
    val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
    mGoogleSignInClient.signOut()
        .addOnCompleteListener() {
            Log.d("Signed Out: ", "Successful")
            Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT)
                .show()
        }
}