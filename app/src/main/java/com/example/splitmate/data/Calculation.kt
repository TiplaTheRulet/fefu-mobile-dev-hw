package com.example.splitmate.data

import java.util.UUID

data class Calculation(
    val id: String = UUID.randomUUID().toString(),
    val totalAmount: Double = 0.0,
    val peopleCount: Int = 1,
    val tipPercentage: Double = 15.0
) {
    val tipAmount: Double
        get() = totalAmount * tipPercentage / 100

    val totalWithTip: Double
        get() = totalAmount + tipAmount

    val perPerson: Double
        get() = totalWithTip / peopleCount
}