package com.example.scanpoint.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scanpoint.models.EventModel
import com.example.scanpoint.models.UserModel
import com.example.scanpoint.states.States
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import java.util.UUID

class ViewModel: ViewModel() {

    private val states = MutableLiveData<States>()

    private val auth = Firebase.auth
    private var database = Firebase.database.reference

    private var eventsList = ArrayList<EventModel>()

    fun getState() : LiveData<States> = states

    fun isUserSignedIn() {
        if (auth.currentUser != null) {
            states.value = States.AlreadySignedIn(true)
        } else {
            states.value = States.AlreadySignedIn(false)
        }
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    states.value = States.SignedUp
                } else {
                    states.value = States.Error
                }
            }
            .addOnFailureListener {
                states.value = States.Error
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
                states.value = States.Default(user)
            }

            override fun onCancelled(error: DatabaseError) {
                states.value = States.Error
            }
        }

        database.child("/users/" + auth.currentUser?.uid).addValueEventListener(objectListener)
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                states.value = States.SignedIn
            } else {
                states.value = States.Error

            }
        }.addOnFailureListener {
            states.value = States.Error
        }
    }

    fun signOut() {
        auth.signOut()
        states.value = States.SignedOut
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
            states.value = States.EventCreateSuccess
        }
    }

    fun fetchEvents() {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                eventsList.clear()

                for(data in snapshot.children) {
                    data.getValue<EventModel>()?.let { eventsList.add(it) }
                }

                states.value = States.EventsFetchSuccess(eventsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child("/event").addValueEventListener(listener)
    }

    fun fetchEvenDetails(eventId: String) {
        val objectListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val event = snapshot.getValue<EventModel>()
                states.value = States.EventDetailsFetched(event)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("/event/$eventId").addValueEventListener(objectListener)
    }

    fun registerUser(uid: String, eventUid: String) {
        val database = database.child("event/$eventUid/attendees")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    println("User $uid is already registered for event $eventUid.")
                } else {
                    val user = mapOf(
                        "uid" to uid,
                    )
                    database.setValue(user).addOnSuccessListener {
                        println("User $uid successfully registered for event $eventUid.")
                        states.value = States.RegisterSuccess
                    }.addOnFailureListener { exception ->
                        println("Failed to register user: ${exception.message}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error checking for user: ${error.message}")
            }
        })
    }
}