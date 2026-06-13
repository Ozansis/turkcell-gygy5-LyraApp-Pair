package com.turkcell.lyraapp.ui.screens.register

object RegisterContract {
    data class State(
        val firstName: String = "",
        val lastName: String = "",
        val phoneNumber: String = "",
        val password: String = "",
        val passwordVisible: Boolean = false,
        val termsAccepted: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class FirstNameChanged(val value: String) : Intent
        data class LastNameChanged(val value: String) : Intent
        data class PhoneNumberChanged(val value: String) : Intent
        data class PasswordChanged(val value: String) : Intent
        data object PasswordVisibilityToggled : Intent
        data object TermsAcceptanceToggled : Intent
        data object RegisterClicked : Intent
        data object NavigateToLoginClicked : Intent
        data object BackClicked : Intent
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
        data object NavigateToLogin : Effect
        data object NavigateUp : Effect
    }
}
