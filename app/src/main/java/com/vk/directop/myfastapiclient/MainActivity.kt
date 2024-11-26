package com.vk.directop.myfastapiclient

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var sepalLength by remember { mutableStateOf("") }
    var sepalWidth by remember { mutableStateOf("") }
    var petalLength by remember { mutableStateOf("") }
    var petalWidth by remember { mutableStateOf("") }
    var prediction by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Iris Prediction", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields
        TextField(value = sepalLength, onValueChange = { sepalLength = it }, label = { Text("Sepal Length") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = sepalWidth, onValueChange = { sepalWidth = it }, label = { Text("Sepal Width") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = petalLength, onValueChange = { petalLength = it }, label = { Text("Petal Length") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = petalWidth, onValueChange = { petalWidth = it }, label = { Text("Petal Width") })

        Spacer(modifier = Modifier.height(16.dp))

        // Predict button
        Button(
            onClick = {
                // Validation for input fields
                val sepalLengthValue = sepalLength.toFloatOrNull()
                if (sepalLengthValue == null) {
                    Toast.makeText(context, "Invalid Sepal Length", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val sepalWidthValue = sepalWidth.toFloatOrNull()
                if (sepalWidthValue == null) {
                    Toast.makeText(context, "Invalid Sepal Width", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val petalLengthValue = petalLength.toFloatOrNull()
                if (petalLengthValue == null) {
                    Toast.makeText(context, "Invalid Petal Length", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val petalWidthValue = petalWidth.toFloatOrNull()
                if (petalWidthValue == null) {
                    Toast.makeText(context, "Invalid Petal Width", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                val irisSpecies = IrisSpecies(
                    sepal_length = sepalLengthValue,
                    sepal_width = sepalWidthValue,
                    petal_length = petalLengthValue,
                    petal_width = petalWidthValue
                )

                RetrofitClient.apiService.getPrediction(irisSpecies).enqueue(object : Callback<PredictionResponse> {
                    override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                        isLoading = false
                        if (response.isSuccessful) {
                            prediction = response.body()?.prediction ?: "Prediction unavailable"
                        } else {
                            Toast.makeText(context, "Failed to fetch prediction", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                        isLoading = false
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            },
            enabled = !isLoading
        ) {
            if (isLoading) {
                Text("Loading...")
            } else {
                Text("Predict")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display prediction result
        Text("Prediction: $prediction")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}
