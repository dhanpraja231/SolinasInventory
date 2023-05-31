package com.fyp.solinosinventory.screens

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fyp.solinosinventory.R
import com.fyp.solinosinventory.Screens
import com.fyp.solinosinventory.data.Product
import com.fyp.solinosinventory.data.RealmDAOInstance
import com.fyp.solinosinventory.data.SubProduct
import com.fyp.solinosinventory.data.Vendor
import com.fyp.solinosinventory.screens.composables.DeleteAlertDialog
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VendorScreen(navController: NavController){
    suspend fun refresh(){
        RealmDAOInstance.addVendor("##ref","ref","","","","","","",) //todo change
        RealmDAOInstance.deleteVendor("##ref")
    }
    val displayDeleteDialogState = remember{ mutableStateOf(false)}
    val displayDeleteDialogVendor = remember{ mutableStateOf(Vendor())}
    val options = listOf<String>("Active","Inactive","In Progress")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vendors = remember{ mutableStateListOf<Vendor>() }
    val refreshKey = remember{ mutableStateOf(true) }
    LaunchedEffect(key1 = refreshKey.value){
        //displayDeleteDialogState.value = false
        //displayDialogState.value = false
        vendors.clear()
        refresh()
        vendors.addAll(RealmDAOInstance.getAllVendors())

    }

    if(displayDeleteDialogState.value){
        DeleteAlertDialog(showDialog = displayDeleteDialogState, onSubmit = {
            scope.launch {
                vendors.clear()
                RealmDAOInstance.deleteVendor(displayDeleteDialogVendor.value._id)
                refreshKey.value = !refreshKey.value
            }
        }, itemName = displayDeleteDialogVendor.value._id, onDismiss = {displayDeleteDialogState.value=false})
    }


    Scaffold(scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {
            navController.navigate(Screens.AddVendorScreen.route)
             }) {
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
            titleText = "Vendors",
            onRefresh = {
                vendors.clear()
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
            LazyColumn(Modifier.fillMaxSize()){
                items(vendors) { vendor ->
                    var dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        scope.launch {
                            dismissState.reset()
                            displayDeleteDialogVendor.value = vendor
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
                            ) {

                                Column(Modifier.fillMaxSize().padding(12.dp)) {

                                    Row(Modifier.fillMaxWidth()) {
                                        Column(Modifier.weight(4f)) {
                                            DisplayText(title = "Vendor", content = vendor._id)
                                            DisplayText(
                                                title = "Vendor Type",
                                                content = vendor.vendorType
                                            )
                                            DisplayText(
                                                title = "Parts supplied",
                                                content = vendor.parts
                                            )
                                            DisplayText(
                                                title = "Avg lead time",
                                                content = vendor.avgLeadTime
                                            )
                                            DisplayText(
                                                title = "Email ID",
                                                content = vendor.emailID
                                            )
                                            DisplayText(
                                                title = "Contact",
                                                content = vendor.contactNumber
                                            )
                                            DisplayText(
                                                title = "For Product",
                                                content = vendor.forProduct
                                            )
                                        }
                                        //status active, in process, inactive
                                        Column(Modifier.weight(2.9f)) {
                                            Spacer(Modifier.height(12.dp))

                                            ExposedDropdownMenuBox(
                                                modifier = Modifier
                                                    .fillMaxWidth().height(80.dp),
                                                expanded = expanded,
                                                onExpandedChange = {
                                                    expanded = !expanded
                                                }
                                            ) {
                                                TextField(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    readOnly = true,
                                                    value = selectedOptionText,
                                                    onValueChange = {

                                                    },
                                                    label = {
                                                        Text(
                                                            "Status",
                                                            color = Color.Black
                                                        )
                                                    },
                                                    trailingIcon = {
                                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                                            expanded = expanded
                                                        )
                                                    },
                                                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                                                )
                                                ExposedDropdownMenu(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    expanded = expanded,
                                                    onDismissRequest = {
                                                        expanded = false
                                                    }
                                                ) {
                                                    Box(
                                                        modifier = Modifier.size(
                                                            width = 250.dp,
                                                            height = 150.dp
                                                        )
                                                    ) {
                                                        LazyColumn {
                                                            items(options) { selectionOption ->
                                                                DropdownMenuItem(
                                                                    modifier = Modifier.fillMaxWidth(),
                                                                    onClick = {
                                                                        selectedOptionText =
                                                                            selectionOption
                                                                        scope.launch {
                                                                            RealmDAOInstance.updateStatus(vendor._id,selectedOptionText)

                                                                        }
                                                                        expanded = false
                                                                    },
                                                                ) {
                                                                    Text(
                                                                        text = selectionOption,
                                                                        fontSize = 12.sp,
                                                                        modifier = Modifier.fillMaxWidth()
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))

                                }

                            }
                        })
                    }
                }
            }

        }

    }


@Composable
fun DisplayText(title: String, content:String ){
    Text(title, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(3.dp))
    Text(content)
    Spacer(modifier = Modifier.height(6.dp))
}