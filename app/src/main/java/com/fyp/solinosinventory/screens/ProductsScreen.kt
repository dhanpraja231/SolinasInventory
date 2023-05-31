package com.fyp.solinosinventory.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fyp.solinosinventory.R
import com.fyp.solinosinventory.Screens
import com.fyp.solinosinventory.data.Product
import com.fyp.solinosinventory.data.RealmDAOInstance
import com.fyp.solinosinventory.screens.composables.DeleteAlertDialog
import com.fyp.solinosinventory.screens.composables.InputDialogView
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(navController: NavController){
    suspend fun refresh(){
        RealmDAOInstance.addProduct("##ref","ref")
        RealmDAOInstance.deleteProduct("##ref")
    }

    val context  = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val displayDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogProduct = remember{ mutableStateOf(Product("",""))}
    val products = remember{ mutableStateListOf<Product>()}
    val refreshKey = remember{ mutableStateOf(true)}
    LaunchedEffect(key1 = refreshKey.value){
        //displayDeleteDialogState.value = false
        //displayDialogState.value = false
        products.clear()
        refresh()
        products.addAll(RealmDAOInstance.getAllProducts())

    }

    if(displayDialogState.value){
        InputDialogView(onDismiss = { displayDialogState.value = false }, onSubmit = {
            name, description->
            scope.launch {
                RealmDAOInstance.addProduct(name,description)
                refreshKey.value = !refreshKey.value
            }
        }, type = "Product" )
    }

    if(displayDeleteDialogState.value){
        DeleteAlertDialog(showDialog = displayDeleteDialogState, onSubmit = {
            scope.launch {
                products.clear()
                RealmDAOInstance.deleteProduct(displayDeleteDialogProduct.value._id)
                refreshKey.value = !refreshKey.value
            }
        }, itemName = displayDeleteDialogProduct.value._id, onDismiss = {displayDeleteDialogState.value=false})
    }


    Scaffold(scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = { displayDialogState.value = true }) {
            Text("+")
        }},

        topBar = {AppBar(onNavigationIconClick = {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        },
                context = context,
                navController = navController,
                titleText = "Products",
            onRefresh = {
                products.clear()
                refreshKey.value = !refreshKey.value
            }
            )
        },
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                context = context,
                navController = navController
            )
        }){
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(8.dp))
            LazyColumn(Modifier.fillMaxWidth()){
                items(products){
                    product ->
                    Surface(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp)
                            .clickable {
                                navController.navigate(Screens.ProductsSubCategoryScreen.route + "/${product._id}")
                            }, elevation = 8.dp,
                    shape = RoundedCornerShape(10.dp)
                        ){
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 3.dp), verticalAlignment = Alignment.CenterVertically){
                            Column(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()) {
                                Image(
                                    painter = painterResource(id = R.drawable.solinasicon), //TODO change icon
                                    contentDescription = "",
                                    alignment = Alignment.Center, modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(0.2f))
                            Column(
                                Modifier
                                    .weight(3f)
                                    .fillMaxHeight()) {
                                Text(text = product._id, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                                Text(text = product.description, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.weight(0.2f))
                            Column(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight() , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = {
                                    scope.launch {
                                        displayDeleteDialogProduct.value = product
                                        displayDeleteDialogState.value = true


                                    }}) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")

                                }
                            }
                        }

//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 20.dp, horizontal = 4.dp)
//                            ,
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically
//                        ){
//                            Text(text = product.name,
//                                modifier = Modifier.fillMaxWidth(),
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//                        }

                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

        }

    }

}