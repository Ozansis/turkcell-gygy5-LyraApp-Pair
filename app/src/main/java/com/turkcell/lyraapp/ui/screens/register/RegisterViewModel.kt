package com.turkcell.lyraapp.ui.screens.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterContract.State())
    val state: StateFlow<RegisterContract.State> = _state.asStateFlow()

    private val _effect = Channel<RegisterContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: RegisterContract.Intent) {
        when (intent) {
            is RegisterContract.Intent.FirstNameChanged       -> _state.update { it.copy(firstName = intent.value) }
            is RegisterContract.Intent.LastNameChanged        -> _state.update { it.copy(lastName = intent.value) }
            is RegisterContract.Intent.PhoneNumberChanged     -> _state.update { it.copy(phoneNumber = intent.value) }
            is RegisterContract.Intent.PasswordChanged        -> _state.update { it.copy(password = intent.value) }
            RegisterContract.Intent.PasswordVisibilityToggled -> _state.update { it.copy(passwordVisible = !it.passwordVisible) }
            RegisterContract.Intent.TermsAcceptanceToggled    -> _state.update { it.copy(termsAccepted = !it.termsAccepted) }
            RegisterContract.Intent.RegisterClicked           -> handleRegister()
            RegisterContract.Intent.NavigateToLoginClicked    -> sendEffect(RegisterContract.Effect.NavigateToLogin)
            RegisterContract.Intent.BackClicked               -> sendEffect(RegisterContract.Effect.NavigateUp)
        }
    }

    private fun handleRegister() {
        val current = _state.value
        if (!current.termsAccepted) return

        val passwordError = validatePassword(current.password)
        if (passwordError != null) {
            _state.update { it.copy(error = passwordError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.register(
                firstName = current.firstName,
                lastName = current.lastName,
                phoneNumber = current.phoneNumber,
                password = current.password,
            )
                .onSuccess { sendEffect(RegisterContract.Effect.NavigateToHome) }
                .onFailure { throwable -> _state.update { it.copy(error = throwable.message) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun validatePassword(password: String): String? {
        if (password.length < 8) return "Şifre en az 8 karakter olmalıdır"
        if (!password.any { it.isDigit() }) return "Şifre en az bir rakam içermelidir"
        return null
    }

    private fun sendEffect(effect: RegisterContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
