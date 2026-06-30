package com.turkcell.lyraapp.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class MembershipPlanDto(
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

data class MembershipPlansResponse(
    val data: List<MembershipPlanDto>,
)

data class CheckoutCardDto(
    val number: String,
    val expMonth: Int,
    val expYear: Int,
    val cvc: String,
    val holderName: String? = null,
)

data class CheckoutRequestDto(
    val plan: String,
    val card: CheckoutCardDto,
)

data class CheckoutPaymentDto(
    val transactionId: String,
    val amountKurus: Int,
    val currency: String,
)

data class CheckoutMembershipDto(
    val planId: String,
    val type: String,
    val status: String,
    val autoRenew: Boolean,
    val startedAt: String,
    val expiresAt: String,
)

data class CheckoutDataDto(
    val payment: CheckoutPaymentDto,
    val membership: CheckoutMembershipDto,
)

data class CheckoutResponse(
    val data: CheckoutDataDto,
)

interface MembershipApiService {
    @GET("api/v1/memberships/plans")
    suspend fun getPlans(): MembershipPlansResponse

    @POST("api/v1/memberships/checkout")
    suspend fun checkout(@Body body: CheckoutRequestDto): CheckoutResponse
}
