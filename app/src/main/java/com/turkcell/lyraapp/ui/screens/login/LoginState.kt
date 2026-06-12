package com.turkcell.lyraapp.ui.screens.login

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)
