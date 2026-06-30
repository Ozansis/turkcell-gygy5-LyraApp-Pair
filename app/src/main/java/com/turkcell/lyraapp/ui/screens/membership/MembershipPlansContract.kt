package com.turkcell.lyraapp.ui.screens.membership

import com.turkcell.lyraapp.data.membership.MembershipPlan

object MembershipPlansContract {

    data class State(
        val plans: List<MembershipPlan> = emptyList(),
        val selectedPlanId: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class PlanSelected(val planId: String) : Intent
        data object ContinueClicked : Intent
        data object BackClicked : Intent
    }

    sealed interface Effect {
        data class NavigateToCheckout(val planId: String) : Effect
        data object NavigateBack : Effect
    }
}
