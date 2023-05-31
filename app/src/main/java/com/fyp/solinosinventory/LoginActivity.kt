package com.fyp.solinosinventory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fyp.solinosinventory.ui.theme.SolinosInventoryTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
//import io.realm.Realm
//import io.realm.RealmConfiguration


class LoginActivity : ComponentActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>



    override fun onStart() {
        super.onStart()

//        //realm
//        Realm.init(this);
//        val config: RealmConfiguration = RealmConfiguration.Builder()
//                .name("default-realm")
//                .allowQueriesOnUiThread(true)
//                .allowWritesOnUiThread(true)
//                .build();
//        Realm.setDefaultConfiguration(config);

        // Check if user is signed in (non-null) and update UI accordingly.
        val account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
//            System.out.println("launching intent to next page");
            val intent = Intent(this, UserActivity::class.java);
            startActivity(intent);
            finish()
            //account.getEmail()?.let { Log.d("Signed In: ", it) };
        } else {
            Log.d("Signed In: ", "Not signed in");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
            requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(
            this,
            gso
        )// since googleApiClient is deprecated, have to use workaround

        signInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                handleSignInResult(task)
            })

        super.onCreate(savedInstanceState)
//        var mGoogleSignInClient: GoogleSignInClient
//        var signInLauncher: ActivityResultLauncher<Intent?>
        setContent {
            SolinosInventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){

                        Button(
                            onClick = {
                                //startForResult.launch(googleSignInClient?.signInIntent)
                                signIn()


//                            val account = GoogleSignIn.getLastSignedInAccount(context);
//                            if (account != null) username = GoogleSignIn.getLastSignedInAccount(context)?.displayName.toString() else username = "not found"


                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            //.height(100.dp)
                            //.padding(start = 16.dp, end = 16.dp),
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Black,
                                contentColor = Color.White
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icons8_google),
                                contentDescription = ""
                            )
                            Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
                        }
                    }
                }
            }
        }
    }
    private fun signIn() {
        println("void sign in")
        val signInIntent = mGoogleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
        println("got sign in intent")
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>
//                                   ,navController: NavController
    ) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken
            Toast.makeText(this, "Signed in successfully", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this@LoginActivity, UserActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: ApiException) {
            e.message?.let { Log.d("error message", it) }
            Log.w("Sign In Error", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
        }
    }
}

