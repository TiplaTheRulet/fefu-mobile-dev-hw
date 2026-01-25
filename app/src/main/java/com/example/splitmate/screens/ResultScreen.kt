package com.example.splitmate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splitmate.viewmodel.CalculationViewModel

@ExperimentalMaterial3Api
@Composable
fun ResultScreen(
    viewModel: CalculationViewModel,
    calculationId: String,
    onEditClick: () -> Unit,
    onNewCalculationClick: () -> Unit
) {
    val calculation = viewModel.getCalculationById(calculationId)

    LaunchedEffect(calculationId) {
        if (calculation == null) {
            onEditClick()
        }
    }

    if (calculation == null) {
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результат расчета") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Итоги расчета",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ResultRow(
                        label = "Сумма счёта",
                        value = String.format("%.2f ₽", calculation.totalAmount)
                    )

                    ResultRow(
                        label = "Чаевые (${calculation.tipPercentage.toInt()}%)",
                        value = String.format("%.2f ₽", calculation.tipAmount)
                    )

                    ResultRow(
                        label = "Сумма с чаевыми",
                        value = String.format("%.2f ₽", calculation.totalWithTip)
                    )

                    ResultRow(
                        label = "С каждого (${calculation.peopleCount} чел.)",
                        value = String.format("%.2f ₽", calculation.perPerson),
                        isHighlighted = true
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Изменить данные")
                }

                Button(
                    onClick = onNewCalculationClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Вернуться на главную")
                }
            }
        }
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            fontSize = if (isHighlighted) 24.sp else 18.sp,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}