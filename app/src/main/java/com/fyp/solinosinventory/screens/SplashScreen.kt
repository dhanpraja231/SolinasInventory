package com.fyp.solinosinventory.screens

import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fyp.solinosinventory.BuildConfig
import com.fyp.solinosinventory.R
import com.fyp.solinosinventory.Screens
import com.fyp.solinosinventory.data.Component
//import com.fyp.solinosinventory.data.Component
import com.fyp.solinosinventory.data.Product
import com.fyp.solinosinventory.data.SubProduct
import com.google.android.gms.auth.api.signin.GoogleSignIn
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
//import io.realm.Realm
//import io.realm.kotlin.mongodb.App
//import io.realm.kotlin.mongodb.AppConfiguration
//import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import io.realm.kotlin.mongodb.sync.SyncConfiguration


//import io.realm.kotlin.Realm

//import io.realm.mongodb.App
//import io.realm.mongodb.AppConfiguration
//import io.realm.mongodb.Credentials
//import io.realm.mongodb.sync.ClientResetRequiredError
//import io.realm.mongodb.sync.DiscardUnsyncedChangesStrategy


//import io.realm.mongodb.sync.SyncConfiguration
//import io.realm.mongodb.sync.SyncSession
import kotlinx.coroutines.delay

const val PARTITION = "Project0"
@Composable
fun SplashScreen(navController: NavController) = Box(
    Modifier
        .fillMaxWidth()
        .fillMaxHeight()
) {
    val context = LocalContext.current
    val scale = remember {
        Animatable(0.0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(800, easing = {
                OvershootInterpolator(4f).getInterpolation(it)
            })
        )
        val email = GoogleSignIn.getLastSignedInAccount(context)?.email.toString()
        Log.d("email",email)
        delay(2000)

        val credentials: Credentials =
            Credentials.anonymous()

//      var app = App.create("solinasinventory-zgrvd")
//        app.login(credentials)
//
//        Log.d("serverAuth",GoogleSignIn.getLastSignedInAccount(context)?.serverAuthCode.toString())

//        //Credentials.google( GoogleSignIn.getLastSignedInAccount(context)?.serverAuthCode,io.realm.mongodb.auth.GoogleAuthType.AUTH_CODE)
//        val user = app.login(Credentials.anonymous())
//
//        val config = SyncConfiguration.Builder(user, PARTITION,
//            setOf(Product::class)
//        )
//            // specify name so realm doesn't just use the "default.realm" file for this user
//            .name(PARTITION)
//            .build()
//        val realm = Realm.open(config)
//
//        Log.d("debug","Successfully opened realm: ${realm.configuration.name}")
//        realm.close()



//        app.loginAsync(credentials) { result ->
//            if (result.isSuccess()) {
////                val user: User? = app.currentUser()
////                val partitionValue = "Project 0"
////                val config = SyncConfiguration.Builder(
////                    user,
////                    //partitionValue,
////                    //schema = setOf(Product::class,SubProduct::class,Component::class)
////                )
//////                    .allowWritesOnUiThread(true)
//////                    .allowQueriesOnUiThread(true)
////                    .schemaVersion(2)
////                    //.name(partitionValue)
////                    .build()
//                //Realm.setDefaultConfiguration(config)
//
//                val partitionBasedConfig = SyncConfiguration.defaultConfig(app.currentUser(), "Project0",
//                , setOf()
//                    )
//                Realm.setDefaultConfiguration(partitionBasedConfig)



//            } else {
//                Log.e(
//                    "QUICKSTART",
//                    "Failed to log in. Error: " + result.getError()
//                )
//            }
        }

        navController.navigate(Screens.ProductScreen.route)



    Image(
        painter = painterResource(id = R.drawable.solinasicon), //TODO change icon
        contentDescription = "",
        alignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .scale(scale.value)
    )

    Text(
        text = "Solinas Inventory",
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)
    )
}