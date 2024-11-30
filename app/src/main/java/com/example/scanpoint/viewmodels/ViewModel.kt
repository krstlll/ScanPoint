package com.example.scanpoint.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanpoint.models.EventModel
import com.example.scanpoint.models.UserModel
import com.example.scanpoint.states.AuthenticationStates
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import java.util.UUID

class ViewModel: ViewModel() {

    private val authenticationStates = MutableLiveData<AuthenticationStates>()

    private val auth = Firebase.auth
    private var database = Firebase.database.reference

    fun getState() : LiveData<AuthenticationStates> = authenticationStates

    fun isUserSignedIn() {
        if (auth.currentUser != null) {
            authenticationStates.value = AuthenticationStates.AlreadySignedIn(true)
        } else {
            authenticationStates.value = AuthenticationStates.AlreadySignedIn(false)
        }
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authenticationStates.value = AuthenticationStates.SignedUp
                } else {
                    authenticationStates.value = AuthenticationStates.Error
                }
            }
            .addOnFailureListener {
                authenticationStates.value = AuthenticationStates.Error
            }
    }

    fun createUserRecord(uid: String, name: String, email: String, studentNumber: String) {
        val database = database.child("users/$uid")

        val user = UserModel(
            uid,
            name,
            email,
            studentNumber
        )

        database.setValue(user)
    }

    fun getUserInfo() {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<UserModel>()
                authenticationStates.value = AuthenticationStates.Default(user)
            }

            override fun onCancelled(error: DatabaseError) {
                authenticationStates.value = AuthenticationStates.Error
            }
        }

        database.child("/users/" + auth.currentUser?.uid).addValueEventListener(objectListener)
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                authenticationStates.value = AuthenticationStates.SignedIn
            } else {
                authenticationStates.value = AuthenticationStates.Error

            }
        }.addOnFailureListener {
            authenticationStates.value = AuthenticationStates.Error
        }
    }

    fun signOut() {
        auth.signOut()
        authenticationStates.value = AuthenticationStates.SignedOut
    }

    fun createEvent(eventName: String, eventDate: String, eventVenue: String) {
        val eventId = UUID.randomUUID().toString()
        val database = database.child("event/$eventId")

        val event = EventModel(
            eventId,
            eventName,
            eventDate,
            eventVenue
        )

        database.setValue(event).addOnCompleteListener {
            authenticationStates.value = AuthenticationStates.EventCreateSuccess
        }
    }
}