package com.example.space_station.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class firebaseModel :ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    
}