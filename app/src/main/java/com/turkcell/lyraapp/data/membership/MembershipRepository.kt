package com.turkcell.lyraapp.data.membership

interface MembershipRepository {
    suspend fun getPlans(): Result<List<MembershipPlan>>

    suspend fun checkout(
        planType: String,
        cardNumber: String,
        expMonth: Int,
        expYear: Int,
        cvc: String,
        holderName: String?,
    ): Result<CheckoutResult>
}
