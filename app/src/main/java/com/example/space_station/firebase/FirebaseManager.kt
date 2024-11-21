package com.example.space_station.firebase

import android.util.Log
import com.example.space_station.model.UserSettingData
import com.example.space_station.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager(
) {

    companion object {
        // `instance`를 통해 싱글톤 접근
        val instance: FirebaseManager by lazy { FirebaseManager() }
    }
    private val _auth = FirebaseAuth.getInstance()
    private val _db = FirebaseFirestore.getInstance()

    fun getCurrentUser(): FirebaseUser? {
        return _auth.currentUser
    }

    fun signIn(email: String, password: String, onSuccess: (uid:String) -> Unit){
        _auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    _auth.currentUser?.let { onSuccess(it.uid) }
                }
            }
    }
//    fun getUserSettingData(
//        uid: String,
//        onSuccess: (UserSettingData) -> Unit,
//        onError: () -> Unit
//    ) {
//        // Firestore에서 'users' 컬렉션의 해당 UID 문서를 가져옴
//        _db.collection("users")
//            .document(uid)  // 'uid'에 해당하는 문서 가져오기
//            .get()  // 문서 가져오기
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {  // 문서가 존재하면
//                    val userSettingData = documentSnapshot.toObject(UserSettingData::class.java)
//                    // 문서를 UserSettingData 객체로 변환
//                    if (userSettingData != null) {
//                        // 데이터가 정상적으로 변환되었으면 onSuccess 콜백 실행
//                        onSuccess(userSettingData)
//                    } else {
//                        // 데이터가 null이면 onError 콜백 실행
//                        onError()
//                    }
//                } else {
//                    // 문서가 존재하지 않으면 onError 콜백 실행
//                    onError()
//                }
//            }
//            .addOnFailureListener { exception ->
//                // Firestore 호출 실패 시 onError 콜백 실행
//                onError()
//            }
//    }


    fun createUser(email: String, password: String,onSuccess:(uid:String)->Unit,onError:()->Unit) {
        _auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signIn(email,password){
                        _auth.currentUser?.let { onSuccess(it.uid) }
                    }
                    // 회원가입 성공
                } else {
                    onError()
                }
            }
    }

    fun updateUserSettingData(uid: String, userSettingData: UserSettingData) {
        _db.collection("users")
            .document(uid)
            .set(userSettingData)
            .addOnSuccessListener {
                //생성 알림
            }
    }


    fun createUserSettingData(uid: String) {
        Log.d("Firebase", "UID: $uid")
        var userSettingData = UserSettingData()
        _db.collection("users")
            .document(uid)
            .set(userSettingData)
            .addOnSuccessListener {
                Log.d("Firebase","firebase collection build")
            }
            .addOnFailureListener {

            }

    }

    fun getUserSettingData(uid: String, onSuccess: (UserSettingData) -> Unit, onError: (Exception) -> Unit) {
        _db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d("Data Load", documentSnapshot.getData().toString())
                if (documentSnapshot.exists()) {
                    val userSettingData = documentSnapshot.toObject(UserSettingData::class.java)
                    Log.d("Data Load", userSettingData.toString())
                    if (userSettingData != null) {
                        onSuccess(userSettingData)
                    } else {
                        onError(Exception("Data is null"))
                    }
                } else {
                    onError(Exception("Document does not exist"))
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }



    fun checkLogin(){

    }



}