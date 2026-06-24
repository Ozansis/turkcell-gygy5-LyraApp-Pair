package com.turkcell.lyraapp.ui.screens.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import com.turkcell.lyraapp.ui.screens.otp.OtpVerifyContract.Effect
import com.turkcell.lyraapp.ui.screens.otp.OtpVerifyContract.Intent
import com.turkcell.lyraapp.ui.screens.otp.OtpVerifyContract.State
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
class OtpVerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val phone: String = checkNotNull(savedStateHandle["phone"])

    private val _state = MutableStateFlow(State(phone = phone))
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.CodeChanged -> _state.update {
                it.copy(code = intent.value.take(6), error = null)
            }
            Intent.VerifyClicked -> handleVerify()
            Intent.ResendClicked -> handleResend()
        }
    }

    private fun handleVerify() {
        val code = _state.value.code.trim()
        if (code.length != 6) {
            _state.update { it.copy(error = "Kod 6 haneli olmalıdır") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.verifyOtp(phone, code)
                .onSuccess { result ->
                    val effect = if (result.firstTime) {
                        Effect.NavigateToCompleteProfile
                    } else {
                        Effect.NavigateToHome
                    }
                    sendEffect(effect)
                }
                .onFailure { _state.update { s -> s.copy(error = it.message ?: "Kod hatalı") } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleResend() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.requestOtp(phone)
                .onFailure { _state.update { s -> s.copy(error = it.message ?: "Kod gönderilemedi") } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
