package com.fyp.solinosinventory.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

@Composable
fun TestScreen(navController: NavController){
    val myState = remember{ mutableStateOf("")}
    Column() {
        Text(myState.value)
        Button(onClick = {myState.value=myState.value+"k"}){
            Text(text = "Clolc")
        }
    }
}