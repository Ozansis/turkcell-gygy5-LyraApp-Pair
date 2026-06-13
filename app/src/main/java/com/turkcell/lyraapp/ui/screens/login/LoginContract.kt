package com.turkcell.lyraapp.ui.screens.login

object LoginContract {
    data class State(
        val phoneNumber: String = "",
        val password: String = "",
        val passwordVisible: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class PhoneNumberChanged(val value: String) : Intent
        data class PasswordChanged(val value: String) : Intent
        data object PasswordVisibilityToggled : Intent
        data object LoginClicked : Intent
        data object ForgotPasswordClicked : Intent
        data object RegisterClicked : Intent
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
        data object NavigateToRegister : Effect
        data object NavigateToForgotPassword : Effect
    }
}
