package com.turkcell.lyraapp.ui.screens.phone

object PhoneEntryContract {
    data class State(
        val phone: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class PhoneChanged(val value: String) : Intent
        data object ContinueClicked : Intent
    }

    sealed interface Effect {
        data class NavigateToOtpVerify(val phone: String) : Effect
    }
}
