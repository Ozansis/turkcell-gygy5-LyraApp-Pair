package com.turkcell.lyraapp.data.membership

data class MembershipPlan(
    val id: String,
    val type: String,
    val name: String,
    val description: String,
    val priceKurus: Int,
    val price: Int,
    val currency: String,
    val durationDays: Int,
    val autoRenew: Boolean,
)
