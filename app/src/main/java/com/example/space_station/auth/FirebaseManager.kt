package com.example.space_station.auth

import com.example.space_station.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager(
    private val userViewModel: UserViewModel
) {
    private val _auth = FirebaseAuth.getInstance()
    private val _db = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser?{
        return null //교체하기
        //return auth.currentUser
    }

}