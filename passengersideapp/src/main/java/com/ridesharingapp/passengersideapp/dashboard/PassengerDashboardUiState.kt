package com.ridesharingapp.passengersideapp.dashboard

sealed interface PassengerDashboardUiState {
    object RideInactive: PassengerDashboardUiState
    data class SearchingForDriver(
        val passengerLat: Double,
        val passengerLon: Double,
        val destinationAddress: String
    ): PassengerDashboardUiState
    data class PassengerPickUp(
        val passengerLat: Double,
        val passengerLon: Double,
        val driverLat: Double,
        val driverLon: Double,
        val destinationLat: Double,
        val destinationLon: Double,
        val destinationAddress: String,
        val driverName: String,
        val driverAvatar: String
    ): PassengerDashboardUiState
    data class EnRoute(
        val passengerLat: Double,
        val passengerLon: Double,
        val destinationLat: Double,
        val destinationLon: Double,
        val driverLat: Double,
        val driverLon: Double,
        val destinationAddress: String,
        val driverName: String,
        val driverAvatar: String
    ): PassengerDashboardUiState

    data class Arrived(
        val passengerLat: Double,
        val passengerLon: Double,
        val destinationLat: Double,
        val destinationLon: Double,
        val destinationAddress: String,
        val driverName: String,
        val driverAvatar: String
    ): PassengerDashboardUiState

    data class Rating(
        val rating: Float = 5.0f
    ): PassengerDashboardUiState

    //Signals something unexpected has happened
    object Error: PassengerDashboardUiState
    object Loading: PassengerDashboardUiState

    data class NewMessages(val totalMessages: Int) : PassengerDashboardUiState
}