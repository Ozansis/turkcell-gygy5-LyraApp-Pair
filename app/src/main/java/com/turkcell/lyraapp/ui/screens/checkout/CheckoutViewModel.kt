package com.turkcell.lyraapp.ui.screens.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkcell.lyraapp.data.membership.MembershipRepository
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
class CheckoutViewModel @Inject constructor(
    private val membershipRepository: MembershipRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val planId: String = checkNotNull(savedStateHandle["planId"])

    private val _state = MutableStateFlow(CheckoutContract.State())
    val state: StateFlow<CheckoutContract.State> = _state.asStateFlow()

    private val _effect = Channel<CheckoutContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadPlan()
    }

    fun onIntent(intent: CheckoutContract.Intent) {
        when (intent) {
            is CheckoutContract.Intent.CardNumberChanged ->
                _state.update { it.copy(cardNumber = formatCardNumber(intent.value), error = null) }
            is CheckoutContract.Intent.HolderNameChanged ->
                _state.update { it.copy(holderName = intent.value, error = null) }
            is CheckoutContract.Intent.ExpiryChanged ->
                _state.update { it.copy(expiry = formatExpiry(intent.value), error = null) }
            is CheckoutContract.Intent.CvcChanged ->
                _state.update { it.copy(cvc = intent.value.filter { c -> c.isDigit() }.take(4), error = null) }
            CheckoutContract.Intent.PayClicked -> handlePay()
            CheckoutContract.Intent.BackClicked ->
                sendEffect(CheckoutContract.Effect.NavigateBack)
            CheckoutContract.Intent.StartListeningClicked ->
                sendEffect(CheckoutContract.Effect.NavigateToHome)
        }
    }

    private fun loadPlan() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            membershipRepository.getPlans()
                .onSuccess { plans ->
                    _state.update { it.copy(plan = plans.find { p -> p.id == planId }) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handlePay() {
        val s = _state.value
        val plan = s.plan ?: return
        val expiry = parseExpiry(s.expiry) ?: run {
            _state.update { it.copy(error = "Gecersiz son kullanma tarihi") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            membershipRepository.checkout(
                planType   = plan.type,
                cardNumber = s.cardNumber,
                expMonth   = expiry.first,
                expYear    = expiry.second,
                cvc        = s.cvc,
                holderName = s.holderName.ifBlank { null },
            )
                .onSuccess { _state.update { it.copy(paymentSuccessful = true) } }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message ?: "Odeme basarisiz") }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun formatCardNumber(input: String): String =
        input.filter { it.isDigit() }.take(16).chunked(4).joinToString(" ")

    private fun formatExpiry(input: String): String {
        val digits = input.filter { it.isDigit() }.take(4)
        return if (digits.length <= 2) digits else "${digits.take(2)}/${digits.drop(2)}"
    }

    private fun parseExpiry(expiry: String): Pair<Int, Int>? {
        val parts = expiry.split("/")
        if (parts.size != 2) return null
        val month = parts[0].toIntOrNull()?.takeIf { it in 1..12 } ?: return null
        val year = parts[1].toIntOrNull() ?: return null
        return Pair(month, 2000 + year)
    }

    private fun sendEffect(effect: CheckoutContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
