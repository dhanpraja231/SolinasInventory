package com.fyp.solinosinventory.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.fyp.solinosinventory.data.Component
import com.fyp.solinosinventory.data.RealmDAOInstance
import com.fyp.solinosinventory.screens.composables.DeleteAlertDialog
import com.fyp.solinosinventory.screens.composables.InputDialogView
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InventoryScreen(navController: NavController, subProductName: String){
    val context  = LocalContext.current
    val scope = rememberCoroutineScope()
    val displayDialogState = remember{ mutableStateOf(false) }
    val displayDeleteDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogComponent = remember{ mutableStateOf(Component("",0,""))}
    val scaffoldState = rememberScaffoldState()
    val components = remember{ mutableStateListOf<Component>() }
    val refreshKey = remember{ mutableStateOf(true) }
    var totalComponentCount by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = refreshKey.value){
        components.clear()
        displayDeleteDialogState.value = false
        displayDialogState.value = false
        components.addAll(RealmDAOInstance.getAllComponents(subProductName))
        totalComponentCount = 0
        components.forEach {
            totalComponentCount += it.quantity
        }
    }



    if(displayDialogState.value){
        InputDialogView(onDismiss = { displayDialogState.value = false
                                    refreshKey.value = !refreshKey.value}, onSubmit = {
                name, quantity->
            scope.launch {

                RealmDAOInstance.addComponent(name, quantity = quantity.toInt(), parentName = subProductName)
                refreshKey.value = !refreshKey.value
            }
        }, type = "Component" )
    }

    if(displayDeleteDialogState.value){
        DeleteAlertDialog(showDialog = displayDeleteDialogState, onSubmit = {
            scope.launch {
                components.clear()
                RealmDAOInstance.deleteComponent(displayDeleteDialogComponent.value._id)
                refreshKey.value = !refreshKey.value
            }
        }, itemName = displayDeleteDialogComponent.value._id.substring(subProductName.length,displayDeleteDialogComponent.value._id.length), onDismiss = {displayDeleteDialogState.value=false})
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
            titleText = "$subProductName's Inventory ",
            onRefresh = {}
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
                Row(Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 3.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text("Total $subProductName count: $totalComponentCount", fontSize = 24.sp)

                }
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(Modifier.fillMaxWidth()){
                items(components){
                        component ->

                    val componentName = component._id.substring(subProductName.length,component._id.length) //use this variable instead of component.name

                    val dismissState = rememberDismissState()

                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        scope.launch {
                            displayDeleteDialogComponent.value = component
                            displayDeleteDialogState.value = true
                            dismissState.reset()
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
                                        Text(text = componentName, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.weight(0.2f))
                                    Row(
                                        Modifier
                                            .weight(1f)
                                            .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically){
                                        Button(onClick = {
                                            scope.launch{
                                                RealmDAOInstance.incrementComponent(component._id,subProductName)
                                                refreshKey.value = !refreshKey.value
                                            }
                                        }, Modifier.weight(1f),
                                            shape = CircleShape
                                        ) {
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
                                            Text(component.quantity.toString(), modifier = Modifier.fillMaxHeight().padding(vertical = 6.dp))
                                        }
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Button(onClick = {
                                            scope.launch {
                                                RealmDAOInstance.decrementComponent(component._id,subProductName)
                                                refreshKey.value = !refreshKey.value

                                            }
                                        },
                                            Modifier
                                                .weight(1f)
                                                .fillMaxHeight(),
                                            shape = CircleShape
                                        ) {
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