package com.example.scanpoint.states

import com.example.scanpoint.models.EventModel
import com.example.scanpoint.models.UserModel

sealed class States {

    data class Default(val user: UserModel?): States()
    data class AlreadySignedIn(val alreadySignedIn: Boolean): States()
    data object SignedIn: States()
    data object SignedUp: States()
    data object SignedOut: States()
    data object Error: States()

    data object EventCreateSuccess: States()
    data class EventsFetchSuccess(val list: ArrayList<EventModel>): States()
    data class EventDetailsFetched(val event: EventModel?): States()

    data object RegisterSuccess: States()
}