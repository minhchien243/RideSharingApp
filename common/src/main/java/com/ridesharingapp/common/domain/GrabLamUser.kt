package com.ridesharingapp.common.domain

data class GrabLamUser(
    val userId: String = "",
    val username: String = "",
    val type: String = "PASSENGER",
    val status: String = "",
    val avatarPhotoUrl: String = "",
    val vehiclePhotoUrl: String? = "",
    val vehicleDescription: String? = "",
    val registeredAsDriver: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: String = "",
    val updatedAt: String = ""
) {
    //For convenience in Swift
}