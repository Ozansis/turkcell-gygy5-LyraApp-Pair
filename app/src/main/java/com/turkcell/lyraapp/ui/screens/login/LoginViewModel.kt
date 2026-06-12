package com.turkcell.lyraapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.PhoneNumberChanged     -> _state.update { it.copy(phoneNumber = intent.value) }
            is LoginIntent.PasswordChanged        -> _state.update { it.copy(password = intent.value) }
            LoginIntent.PasswordVisibilityToggled -> _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            LoginIntent.LoginClicked              -> handleLogin()
            LoginIntent.ForgotPasswordClicked     -> sendEffect(LoginEffect.NavigateToForgotPassword)
            LoginIntent.RegisterClicked           -> sendEffect(LoginEffect.NavigateToRegister)
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.login(_state.value.phoneNumber, _state.value.password)
                .onSuccess { sendEffect(LoginEffect.NavigateToHome) }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
