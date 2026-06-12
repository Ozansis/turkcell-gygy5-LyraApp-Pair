package com.turkcell.lyraapp.ui.screens.login

sealed interface LoginIntent {
    data class PhoneNumberChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object PasswordVisibilityToggled : LoginIntent
    data object LoginClicked : LoginIntent
    data object ForgotPasswordClicked : LoginIntent
    data object RegisterClicked : LoginIntent
}
