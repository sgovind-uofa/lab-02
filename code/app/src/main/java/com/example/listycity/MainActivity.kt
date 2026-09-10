package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.listycity.ui.theme.ListyCityTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //new line added here
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //new line/name added here
                    CityListScreen(
                        //new line added here
                        cities = cityRepository.cities,
                        //new line added here
                        onAddCity = {cityRepository.addCity(it)},
                        onDeleteCity = {cityRepository.deleteCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}

//new class created here
class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney","Berlin","Vienna",
        "Tokyo","Beijing","Osaka",
        "New Delhi"
    )

    val cities: List<String>
        get()= _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun deleteCity(city: String){
        _cities.remove(city)
    }
}

//new function created here
@Composable
fun CityListScreen(
    cities:List<String>,
    // new line added here
    onAddCity: (String) ->Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
){
    //new line added after add city
    var newCityName by remember { mutableStateOf("") }

    var selectedCity by remember { mutableStateOf<String?>(null) }

    // Keeps track of whether the user is currently adding a city
    var addingCity by remember { mutableStateOf(false) }

    Column(modifier=modifier.fillMaxSize()) {

        // ADD CITY button
        if (!addingCity) {
            Button(
                onClick = {
                    addingCity = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("ADD CITY")
            }
        }

        // City name input and CONFIRM button
        if (addingCity) {
            Row(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City name") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                            addingCity = false
                        }
                    }
                ) {
                    Text("CONFIRM")
                }
            }
        }

        // DELETE CITY button
        Button(
            onClick = {
                if (selectedCity != null) {
                    onDeleteCity(selectedCity!!)
                    selectedCity = null
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("DELETE CITY")
        }

        // City list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        selectedCity = city
                    }
                )
            }
        }
    }
}

//new function created here
@Composable
fun CityRow(
    city: String,
    isSelected: Boolean,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal=18.dp, vertical=14.dp)
    ) {
        Text(
            text = if (isSelected) "✓ $city" else city,
            fontSize = 28.sp
        )
    }
}