package com.turkcell.lyraapp.ui.screens.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import com.turkcell.lyraapp.ui.screens.phone.PhoneEntryContract.Effect
import com.turkcell.lyraapp.ui.screens.phone.PhoneEntryContract.Intent
import com.turkcell.lyraapp.ui.screens.phone.PhoneEntryContract.State
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
class PhoneEntryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.PhoneChanged -> _state.update { it.copy(phone = intent.value, error = null) }
            Intent.ContinueClicked -> handleContinue()
        }
    }

    private fun handleContinue() {
        val phone = _state.value.phone.trim()
        if (phone.isBlank()) {
            _state.update { it.copy(error = "Telefon numarası boş olamaz") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.requestOtp(phone)
                .onSuccess { sendEffect(Effect.NavigateToOtpVerify(phone)) }
                .onFailure { _state.update { s -> s.copy(error = it.message ?: "Bir hata oluştu") } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
