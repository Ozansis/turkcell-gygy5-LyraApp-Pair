package com.turkcell.lyraapp.ui.screens.checkout

import com.turkcell.lyraapp.data.membership.MembershipPlan

object CheckoutContract {

    data class State(
        val plan: MembershipPlan? = null,
        val cardNumber: String = "",
        val holderName: String = "",
        val expiry: String = "",
        val cvc: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val paymentSuccessful: Boolean = false,
    )

    sealed interface Intent {
        data class CardNumberChanged(val value: String) : Intent
        data class HolderNameChanged(val value: String) : Intent
        data class ExpiryChanged(val value: String) : Intent
        data class CvcChanged(val value: String) : Intent
        data object PayClicked : Intent
        data object BackClicked : Intent
        data object StartListeningClicked : Intent
    }

    sealed interface Effect {
        data object NavigateBack : Effect
        data object NavigateToHome : Effect
    }
}
