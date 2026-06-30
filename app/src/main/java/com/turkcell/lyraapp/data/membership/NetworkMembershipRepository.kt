package com.turkcell.lyraapp.data.membership

import com.turkcell.lyraapp.data.remote.CheckoutCardDto
import com.turkcell.lyraapp.data.remote.CheckoutRequestDto
import com.turkcell.lyraapp.data.remote.MembershipApiService
import javax.inject.Inject

class NetworkMembershipRepository @Inject constructor(
    private val membershipApiService: MembershipApiService,
) : MembershipRepository {

    override suspend fun getPlans(): Result<List<MembershipPlan>> = runCatching {
        membershipApiService.getPlans().data.map { dto ->
            MembershipPlan(
                id           = dto.id,
                type         = dto.type,
                name         = dto.name,
                description  = dto.description,
                priceKurus   = dto.priceKurus,
                price        = dto.price,
                currency     = dto.currency,
                durationDays = dto.durationDays,
                autoRenew    = dto.autoRenew,
            )
        }
    }

    override suspend fun checkout(
        planType: String,
        cardNumber: String,
        expMonth: Int,
        expYear: Int,
        cvc: String,
        holderName: String?,
    ): Result<CheckoutResult> = runCatching {
        val response = membershipApiService.checkout(
            CheckoutRequestDto(
                plan = planType,
                card = CheckoutCardDto(
                    number     = cardNumber,
                    expMonth   = expMonth,
                    expYear    = expYear,
                    cvc        = cvc,
                    holderName = holderName,
                ),
            )
        )
        CheckoutResult(
            transactionId = response.data.payment.transactionId,
            amountKurus   = response.data.payment.amountKurus,
            currency      = response.data.payment.currency,
        )
    }
}
