package com.example.splitmate.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.splitmate.viewmodel.CalculationViewModel

@ExperimentalMaterial3Api
@Composable
fun InputScreen(
    viewModel: CalculationViewModel,
    onCalculateClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val calculation by viewModel.currentCalculation.collectAsStateWithLifecycle()
    var totalAmount by remember { mutableStateOf("") }
    var peopleCount by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(15.0f) }

    LaunchedEffect(Unit) {
        if (totalAmount.isEmpty() && calculation.totalAmount > 0) {
            totalAmount = calculation.totalAmount.toString()
        }
        if (peopleCount.isEmpty() && calculation.peopleCount > 1) {
            peopleCount = calculation.peopleCount.toString()
        }
        tipPercentage = calculation.tipPercentage.toFloat()
    }

    LaunchedEffect(totalAmount) {
        val amount = totalAmount.toDoubleOrNull() ?: 0.0
        viewModel.updateTotalAmount(amount.toString())
    }

    LaunchedEffect(peopleCount) {
        val count = peopleCount.toIntOrNull() ?: 1
        viewModel.updatePeopleCount(count.toString())
    }

    LaunchedEffect(tipPercentage) {
        viewModel.updateTipPercentage(tipPercentage.toDouble())
    }

    val isFormValid = totalAmount.isNotEmpty() &&
            totalAmount.toDoubleOrNull() != null &&
            totalAmount.toDoubleOrNull() ?: 0.0 > 0 &&
            peopleCount.isNotEmpty() &&
            peopleCount.toIntOrNull() != null &&
            peopleCount.toIntOrNull() ?: 0 > 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Введите данные") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Сумма счёта")
            OutlinedTextField(
                value = totalAmount,
                onValueChange = { totalAmount = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Например: 1500") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Количество человек")
            OutlinedTextField(
                value = peopleCount,
                onValueChange = { peopleCount = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Например: 4") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Чаевые: ${tipPercentage.toInt()}%")
            Slider(
                value = tipPercentage,
                onValueChange = { tipPercentage = it },
                valueRange = 0.0f..50.0f,
                steps = 49,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveCalculation()
                    onCalculateClick(calculation.id)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text(text = "Рассчитать", fontSize = 18.sp)
            }
        }
    }
}
