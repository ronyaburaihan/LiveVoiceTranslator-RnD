package com.example.livevoicetranslator_rd.presentation.app

enum class AppState {
    SubscriptionYearly,
    SubscriptionMonthly,
    Lifetime,
    Free
}

val AppState?.isPremium: Boolean
    get() = this != null && this != AppState.Free