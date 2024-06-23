package com.example.spaid

//data strings to be stored in firebase database
data class VitalsData(
    val time: String? = null,
    val alert: String? = null,
    val finger: String? = null,
    val heart: String? = null,
    val oxygen: String? = null,
    val stress: String? = null,
    val vibrate: String? = null,
    val date: String? = null,

) {
    constructor() : this(null, null, null, null, null, null)
}