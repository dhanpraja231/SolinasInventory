package com.fyp.solinosinventory.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fyp.solinosinventory.Screens
import com.fyp.solinosinventory.data.RealmDAOInstance
import com.fyp.solinosinventory.data.Vendor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddVendorScreen(navController: NavController){
    suspend fun refresh(){
        RealmDAOInstance.addVendor("##ref","ref","","","","","","",) //todo change
        RealmDAOInstance.deleteVendor("##ref")
    }
    val options = listOf<String>("Active","Inactive","In Progress")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vendors = remember{ mutableStateListOf<Vendor>() }
    val refreshKey = remember{ mutableStateOf(true) }

    val displayVendorNameState = remember {
        mutableStateOf("")
    }
    val displayVendorTypeState = remember {
        mutableStateOf("")
    }
    val displayVendorPartsState = remember {
        mutableStateOf("")
    }
    val displayVendorContactState = remember {
        mutableStateOf("")
    }
    val displayVendorEmailState = remember {
        mutableStateOf("")
    }
    val displayVendorLeadTimeState = remember {
        mutableStateOf("")
    }
    val displayVendorForProductState = remember {
        mutableStateOf("")
    }
    val displayVendorStatusState = remember {
        mutableStateOf("Active")
    }





    LaunchedEffect(key1 = refreshKey.value){
        //displayDeleteDialogState.value = false
        //displayDialogState.value = false
        vendors.clear()
        refresh()
        vendors.addAll(RealmDAOInstance.getAllVendors())

    }


    Scaffold(scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { FloatingActionButton(onClick = {
            if(displayVendorContactState.value.isNotBlank()&&
                displayVendorNameState.value.isNotBlank()&&
                displayVendorTypeState.value.isNotBlank()&&
                displayVendorLeadTimeState.value.isNotBlank()&&
                displayVendorEmailState.value.isNotBlank()&&
                displayVendorForProductState.value.isNotBlank()&&
                displayVendorPartsState.value.isNotBlank()&&
                displayVendorStatusState.value.isNotBlank()
                    ){
                scope.launch {
                    RealmDAOInstance.addVendor(displayVendorNameState.value,
                    displayVendorTypeState.value,displayVendorPartsState.value,
                        displayVendorContactState.value,displayVendorEmailState.value,
                        displayVendorLeadTimeState.value,displayVendorForProductState.value,
                        displayVendorStatusState.value
                    )
                    navController.navigate(Screens.VendorScreen.route)
                }
            }
            else{

                Log.d("values",displayVendorContactState.value)
                Log.d("values",displayVendorNameState.value)
                Log.d("values",displayVendorTypeState.value)
                Log.d("values",displayVendorLeadTimeState.value)
                Log.d("values",displayVendorEmailState.value)
                Log.d("values",displayVendorForProductState.value)
                Log.d("values",displayVendorPartsState.value)
                Log.d("values",displayVendorStatusState.value)
                Toast.makeText(context,"Enter valid values",Toast.LENGTH_SHORT).show()
            }
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
            titleText = "Products",
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
        Surface(
            Modifier
                .padding(it)
                .fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorNameState.value, onValueChange = {displayVendorNameState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("Vendor name")})
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorTypeState.value, onValueChange = {displayVendorTypeState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("Vendor type")})
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorPartsState.value, onValueChange = {displayVendorPartsState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("parts")})
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxSize()) {
                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .fillMaxWidth(),
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
                                displayVendorStatusState.value = selectedOptionText
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
                            Box(modifier = Modifier.size(width = 250.dp, height = 200.dp)) {
                                LazyColumn {
                                    items(options) { selectionOption ->
                                        DropdownMenuItem(
                                            modifier = Modifier.fillMaxWidth(),
                                            onClick = {
                                                selectedOptionText = selectionOption
                                                displayVendorStatusState.value = selectedOptionText
                                                expanded = false
                                            },
                                        ){
                                            Text(text = selectionOption, fontSize = 12.sp,modifier = Modifier.fillMaxWidth())
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorContactState.value, onValueChange = {displayVendorContactState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("Mobile")})
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorLeadTimeState.value, onValueChange = {displayVendorLeadTimeState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("Avg Lead time")})
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorEmailState.value, onValueChange = {displayVendorEmailState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("Vendor email")})
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = displayVendorForProductState.value, onValueChange = {displayVendorForProductState.value = it}, modifier = Modifier.fillMaxWidth(), label = {Text("For product")})
                Spacer(Modifier.height(40.dp))
            }

        }

    }
}