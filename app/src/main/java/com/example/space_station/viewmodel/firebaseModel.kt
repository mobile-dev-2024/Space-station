package com.example.space_station.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class FirebaseModel :ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()


    fun getCurrentUser():FirebaseUser?{
        return null //교체하기
        //return auth.currentUser 
    }

    fun registerUser(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    //회원가입 성공
                }else{
                    //계정 생성 실패
                }

            }
            .addOnFailureListener{exception->
                    //회원가입 요청 실패 
            }

    }

    fun logout(){
        auth.signOut()
    }





    private fun createUser(uid:String){

    }

    
}