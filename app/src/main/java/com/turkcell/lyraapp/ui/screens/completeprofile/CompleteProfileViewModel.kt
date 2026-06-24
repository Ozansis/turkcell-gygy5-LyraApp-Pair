package com.turkcell.lyraapp.ui.screens.completeprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.auth.AuthRepository
import com.turkcell.lyraapp.ui.screens.completeprofile.CompleteProfileContract.Effect
import com.turkcell.lyraapp.ui.screens.completeprofile.CompleteProfileContract.Intent
import com.turkcell.lyraapp.ui.screens.completeprofile.CompleteProfileContract.State
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
class CompleteProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.FirstNameChanged -> _state.update { it.copy(firstName = intent.value, error = null) }
            is Intent.LastNameChanged -> _state.update { it.copy(lastName = intent.value, error = null) }
            is Intent.BirthDayChanged -> _state.update { it.copy(birthDay = intent.value.take(2), error = null) }
            is Intent.BirthMonthChanged -> _state.update { it.copy(birthMonth = intent.value.take(2), error = null) }
            is Intent.BirthYearChanged -> _state.update { it.copy(birthYear = intent.value.take(4), error = null) }
            Intent.SaveClicked -> handleSave()
        }
    }

    private fun handleSave() {
        val s = _state.value
        if (s.firstName.isBlank() || s.lastName.isBlank() ||
            s.birthDay.isBlank() || s.birthMonth.isBlank() || s.birthYear.isBlank()
        ) {
            _state.update { it.copy(error = "Tüm alanları doldurun") }
            return
        }
        // API formatı: YYYY-MM-DD
        val birthDate = "${s.birthYear}-${s.birthMonth.padStart(2, '0')}-${s.birthDay.padStart(2, '0')}"
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            authRepository.updateProfile(
                firstName = s.firstName.trim(),
                lastName = s.lastName.trim(),
                birthDate = birthDate,
            )
                .onSuccess { sendEffect(Effect.NavigateToHome) }
                .onFailure { _state.update { st -> st.copy(error = it.message ?: "Bir hata oluştu") } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
