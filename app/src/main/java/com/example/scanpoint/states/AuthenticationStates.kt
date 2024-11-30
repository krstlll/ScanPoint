package com.example.scanpoint.states

import com.example.scanpoint.models.UserModel

sealed class AuthenticationStates {

    data class Default (val user : UserModel?): AuthenticationStates()
    data class AlreadySignedIn (val alreadySignedIn : Boolean): AuthenticationStates()
    data object SignedIn: AuthenticationStates()
    data object SignedUp: AuthenticationStates()
    data object SignedOut: AuthenticationStates()
    data object Error: AuthenticationStates()

    data object EventCreateSuccess: AuthenticationStates()

}