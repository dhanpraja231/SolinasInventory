package com.fyp.solinosinventory.screens.composables

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly

@Composable
fun InputDialogView(
    onDismiss:() -> Unit,
    onSubmit: (name: String, descOrQuantity: String) -> Unit, type: String) {
    val context = LocalContext.current
    var inputFieldText by remember {
        mutableStateOf("")
    }
    var quantityText by remember {
        mutableStateOf("")
    }
    var descriptionText by remember {
        mutableStateOf("")
    }
    val nameErrorState = remember{ mutableStateOf(false)}
    val descriptionErrorState = remember{ mutableStateOf(false)}
    val quantityErrorState = remember{ mutableStateOf(false)}
    val isValidatedState = remember{ mutableStateOf(true)}


    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(10.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 40.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .background(Color.White)
                    .verticalScroll(rememberScrollState()),

            ) {

                Text(
                    text = "Add $type",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )


                OutlinedTextField(
                    value = inputFieldText,
                    singleLine = true,
                    onValueChange = { inputFieldText = it }, modifier = Modifier.padding(8.dp),
                    label = { Text("$type name") }
                )
                ErrorText(text = "  Enter valid name", isShow = nameErrorState)

                when(type) {
                    "Product" -> {

                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = descriptionText,
                            onValueChange = { descriptionText = it },
                            modifier = Modifier.padding(8.dp),
                            label = { Text("Description") }
                        ) //TODO validation
                        ErrorText(text = "  Enter valid Description", isShow = descriptionErrorState)
                    }
                    else -> {
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = quantityText,
                            onValueChange = { quantityText = it },
                            modifier = Modifier.padding(8.dp),
                            label = { Text("Quantity") }
                        ) //TODO validation}
                        ErrorText(text = "  Enter valid Quantity", isShow = quantityErrorState)

                    }
                }

                Row {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel")
                    }


                    Button(
                        onClick = {
                            when(type){
                                "Product"->{
                                    isValidatedState.value = true
                                    nameErrorState.value = false
                                    descriptionErrorState.value = false

                                    if(inputFieldText.isBlank()){
                                        isValidatedState.value = false
                                        nameErrorState.value = true
                                    }
                                    if(descriptionText.isBlank()){
                                        isValidatedState.value = false
                                        descriptionErrorState.value = true
                                    }
                                    if(isValidatedState.value){
                                        onSubmit(inputFieldText,descriptionText)
                                        Toast.makeText(context, "Added product", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }

                                }
                                else ->{
                                    isValidatedState.value = true
                                    nameErrorState.value = false
                                    quantityErrorState.value = false

                                    if(inputFieldText.isBlank()){
                                        isValidatedState.value = false
                                        nameErrorState.value = true
                                    }
                                    if(quantityText.isEmpty() || !quantityText.isDigitsOnly()){
                                        isValidatedState.value = false
                                        quantityErrorState.value = true
                                    }
                                    if(isValidatedState.value){
                                        onSubmit(inputFieldText,quantityText)
                                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                                        onDismiss()
                                    }


                                }
                            }


                             },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Add")
                    }
                }


            }
        }
    }
}

@Composable
fun DeleteAlertDialog(showDialog: MutableState<Boolean>, onSubmit: () -> Unit, itemName:String, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                    onSubmit()
                })
                { Text(text = "Confirm") }
            },
            dismissButton = {
                TextButton(onClick = {onDismiss()})
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Confirm delete") },
            text = { Text(text = "You are deleting all data related to $itemName?") }
        )
}

@Composable
fun ErrorText(text: String, isShow: MutableState<Boolean>){
    if(isShow.value){
        Spacer(Modifier.height(4.dp))
        Text(" ${text}", fontSize = 14.sp, color = Color.Red)


    }
}