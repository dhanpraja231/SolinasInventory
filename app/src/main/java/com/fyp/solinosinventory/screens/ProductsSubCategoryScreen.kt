package com.fyp.solinosinventory.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fyp.solinosinventory.Screens
import com.fyp.solinosinventory.data.RealmDAOInstance
import com.fyp.solinosinventory.data.SubProduct
import com.fyp.solinosinventory.screens.composables.DeleteAlertDialog
import com.fyp.solinosinventory.screens.composables.InputDialogView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductsSubCategoryScreen(navController: NavController, productName: String){
    val context  = LocalContext.current
    val scope = rememberCoroutineScope()
    val displayDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogSubProduct = remember{ mutableStateOf(SubProduct("",0,""))}
    val scaffoldState = rememberScaffoldState()
    val subProducts = remember{ mutableStateListOf<SubProduct>() }
    val refreshKey = remember{ mutableStateOf(true) }
    var totalProductCount = remember {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = refreshKey.value){
        displayDeleteDialogState.value = false
        displayDialogState.value = false
        subProducts.clear()
        subProducts.addAll(RealmDAOInstance.getAllSubProducts(productName))
        totalProductCount.value = 0
        subProducts.forEach {
            totalProductCount.value += it.quantity
        }
        Log.d("totalProductCount",totalProductCount.toString())
    }

    if(displayDialogState.value){
        InputDialogView(onDismiss = { displayDialogState.value = false
            refreshKey.value = !refreshKey.value}, onSubmit = {
                name, quantity->
            scope.launch {

                RealmDAOInstance.addSubProduct(name, quantity = quantity.toInt(), parentName = productName)
                refreshKey.value = !refreshKey.value
            }
        }, type = "SubProduct" )
    }

    if(displayDeleteDialogState.value){
        DeleteAlertDialog(showDialog = displayDeleteDialogState, onSubmit = {
            scope.launch {
                subProducts.clear()
                RealmDAOInstance.deleteSubProduct(displayDeleteDialogSubProduct.value._id)
                refreshKey.value = !refreshKey.value
            }
        }, itemName = displayDeleteDialogSubProduct.value._id, onDismiss = {displayDeleteDialogState.value=false})
    }

    Scaffold(scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = { displayDialogState.value = true }) {
            Text("+")
        }
        },
        topBar = {AppBar(onNavigationIconClick = {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        },
            context = context,
            navController = navController,
            titleText = "$productName versions",
            onRefresh = {
                subProducts.clear()
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
            Surface(modifier = Modifier.fillMaxWidth(), elevation = 4.dp){
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 3.dp),
                    horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                    Text("Total $productName count: ${totalProductCount.value}", fontSize = 24.sp)
//                    Button({totalProductCount.value=-3}){
//                        Text("Recompose")
//                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(Modifier.fillMaxWidth()){
                items(subProducts){
                        subProduct ->
                    var dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        scope.launch {
                            dismissState.reset()
                            displayDeleteDialogSubProduct.value = subProduct
                            displayDeleteDialogState.value = true

                        }
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier
                            .padding(vertical = Dp(1f)),
                        directions = setOf(
                            DismissDirection.EndToStart
                        ),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                        },
                        background = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.White
                                    //else -> Color.Red
                                    else -> Color.White
                                }
                            )
                            val alignment = Alignment.CenterEnd
                            val icon = Icons.Default.Delete

                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = Dp(20f)),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {

                            Card(
                                elevation = animateDpAsState(
                                    4.dp
                                ).value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    //.height(Dp(50f))
                                    .padding(6.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .clickable {
                                        navController.navigate(Screens.InventoryScreen.route + "/${subProduct._id}")
                                    }
                            ) {

                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp, horizontal = 3.dp), verticalAlignment = Alignment.CenterVertically){
                                        Row(
                                            Modifier
                                                .weight(1f)
                                                .padding(horizontal = 3.dp)
                                                .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = subProduct._id, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.weight(0.2f))
                                        Row(
                                            Modifier
                                                .weight(1f)
                                                .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically){
                                            Button(onClick = {
                                                scope.launch{
                                                    val task = async{RealmDAOInstance.incrementSubProduct(subProduct._id)}
                                                    task.await()
                                                    refreshKey.value = !refreshKey.value
                                                        }
                                            }, Modifier.weight(1f),
                                                shape = CircleShape) {
                                                Text("+")
                                            }
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Box(
                                                Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                                    //.padding(vertical = 10.dp)
                                                    .border(
                                                        1.dp,
                                                        Color.Black,
                                                        shape = RoundedCornerShape(8.dp)
                                                    ), contentAlignment = Alignment.Center){
                                                Text(subProduct.quantity.toString(), modifier = Modifier
                                                    .fillMaxHeight()
                                                    .padding(vertical = 6.dp))
                                            }
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Button(onClick = {
                                                scope.launch {
                                                    RealmDAOInstance.decrementSubProduct(subProduct._id)
                                                    refreshKey.value = !refreshKey.value

                                                }
                                            },
                                                Modifier
                                                    .weight(1f)
                                                    .fillMaxHeight(),
                                                shape = CircleShape) {
                                                Text("-")
                                            }
                                        }
                                    }

                                }
                                //Spacer(modifier = Modifier.height(6.dp))
                        }
                    )
                    //Spacer(Modifier.height(4.dp))
                    //Divider(Modifier.fillMaxWidth(), Color.DarkGray)
                }




            }

        }

    }
}