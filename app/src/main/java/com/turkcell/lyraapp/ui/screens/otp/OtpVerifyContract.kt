package com.turkcell.lyraapp.ui.screens.otp

object OtpVerifyContract {
    data class State(
        val phone: String = "",
        val code: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class CodeChanged(val value: String) : Intent
        data object VerifyClicked : Intent
        data object ResendClicked : Intent
    }

    sealed interface Effect {
        data object NavigateToCompleteProfile : Effect
        data object NavigateToHome : Effect
    }
}
