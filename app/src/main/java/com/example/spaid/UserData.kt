package com.example.spaid


//data strings declaration for user data to be passed in the database
data class UserData(
    val id: String? = null,
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val name: String? = null,
    val sex: String? = null,
    val address: String? = null,
    val numInfo: String? = null,
    val contactPerson: String? = null,
    val contactPersonInfo: String? = null

) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null)
}