package com.example.convert

import android.os.Bundle
import androidx.compose.ui.Alignment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.convert.ui.theme.ConvertTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ConvertTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UnitConverter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun UnitConverter(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Centimeters") }
    var inputExpanded by remember { mutableStateOf(false) }
    var outputExpanded by remember { mutableStateOf(false) }
    var outputValue by remember { mutableStateOf("") }

    val context = LocalContext.current

    val units = listOf("Millimetres", "Centimeters", "Metres", "Feet")
    val conversionToMeters = mapOf(
        "Millimetres" to 0.001,
        "Centimeters" to 0.01,
        "Metres" to 1.0,
        "Feet" to 0.3048
    )

    fun convertUnits() {
        val input = inputValue.toDoubleOrNull()
        if (input != null) {
            val inputToMeter = conversionToMeters[inputUnit] ?: 1.0
            val outputFromMeter = 1 / (conversionToMeters[outputUnit] ?: 1.0)
            val result = input * inputToMeter * outputFromMeter
            outputValue = "%.2f".format(result)
        } else {
            Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Unit Converter", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                convertUnits()
            },
            label = { Text("Enter Value") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Box {
                Button(onClick = { inputExpanded = true }) {
                    Text(inputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = inputExpanded,
                    onDismissRequest = { inputExpanded = false }
                ) {
                    units.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                inputUnit = unit
                                inputExpanded = false
                                convertUnits()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box {
                Button(onClick = { outputExpanded = true }) {
                    Text(outputUnit)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = outputExpanded,
                    onDismissRequest = { outputExpanded = false }
                ) {
                    units.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                outputUnit = unit
                                outputExpanded = false
                                convertUnits()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Result: $outputValue $outputUnit", style = MaterialTheme.typography.titleLarge)
    }
}
