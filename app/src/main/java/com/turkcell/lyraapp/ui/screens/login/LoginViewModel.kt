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

    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.PhoneNumberChanged     -> _state.update { it.copy(phoneNumber = intent.value) }
            is LoginContract.Intent.PasswordChanged        -> _state.update { it.copy(password = intent.value) }
            LoginContract.Intent.PasswordVisibilityToggled -> _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            LoginContract.Intent.LoginClicked              -> handleLogin()
            LoginContract.Intent.ForgotPasswordClicked     -> sendEffect(LoginContract.Effect.NavigateToForgotPassword)
            LoginContract.Intent.RegisterClicked           -> sendEffect(LoginContract.Effect.NavigateToRegister)
        }
    }

    private fun handleLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.login(_state.value.phoneNumber, _state.value.password)
                .onSuccess { sendEffect(LoginContract.Effect.NavigateToHome) }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: LoginContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
