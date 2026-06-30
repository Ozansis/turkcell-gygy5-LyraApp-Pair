package com.turkcell.lyraapp.data.membership

data class CheckoutResult(
    val transactionId: String,
    val amountKurus: Int,
    val currency: String,
)
