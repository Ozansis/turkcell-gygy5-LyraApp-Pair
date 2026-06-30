package com.turkcell.lyraapp.ui.screens.membership

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
class MembershipPlansViewModel @Inject constructor(
    private val membershipRepository: MembershipRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MembershipPlansContract.State())
    val state: StateFlow<MembershipPlansContract.State> = _state.asStateFlow()

    private val _effect = Channel<MembershipPlansContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        loadPlans()
    }

    fun onIntent(intent: MembershipPlansContract.Intent) {
        when (intent) {
            is MembershipPlansContract.Intent.PlanSelected ->
                _state.update { it.copy(selectedPlanId = intent.planId) }
            MembershipPlansContract.Intent.ContinueClicked -> handleContinue()
            MembershipPlansContract.Intent.BackClicked ->
                sendEffect(MembershipPlansContract.Effect.NavigateBack)
        }
    }

    private fun loadPlans() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            membershipRepository.getPlans()
                .onSuccess { plans ->
                    val defaultId = plans.find { it.type == "recurring" }?.id
                        ?: plans.firstOrNull()?.id
                        ?: ""
                    _state.update { it.copy(plans = plans, selectedPlanId = defaultId) }
                }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleContinue() {
        val planId = _state.value.selectedPlanId
        if (planId.isNotEmpty()) {
            sendEffect(MembershipPlansContract.Effect.NavigateToCheckout(planId))
        }
    }

    private fun sendEffect(effect: MembershipPlansContract.Effect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
