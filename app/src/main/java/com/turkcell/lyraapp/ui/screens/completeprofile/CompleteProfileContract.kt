package com.turkcell.lyraapp.ui.screens.completeprofile

object CompleteProfileContract {
    data class State(
        val firstName: String = "",
        val lastName: String = "",
        val birthDay: String = "",
        val birthMonth: String = "",
        val birthYear: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Intent {
        data class FirstNameChanged(val value: String) : Intent
        data class LastNameChanged(val value: String) : Intent
        data class BirthDayChanged(val value: String) : Intent
        data class BirthMonthChanged(val value: String) : Intent
        data class BirthYearChanged(val value: String) : Intent
        data object SaveClicked : Intent
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
    }
}
