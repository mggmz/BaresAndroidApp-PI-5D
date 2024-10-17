package com.axldev.yumeat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser
    private val db = FirebaseFirestore.getInstance()  // Instancia de Firestore

    fun registerUser(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                // Registrar al usuario en Firebase Auth
                val result = auth.createUserWithEmailAndPassword(email, password).await()

                // Obtener el UID del usuario recién creado
                val userUID = result.user?.uid ?: throw Exception("User UID not found")

                // Guardar el username en Firestore bajo una colección de usuarios
                val userData = hashMapOf(
                    "username" to username,
                    "email" to email
                )
                db.collection("users").document(userUID).set(userData).await()

                Log.d(javaClass.simpleName, "Usuario registrado con éxito y username guardado en Firestore")
            } catch (e: Exception) {
                Log.d(javaClass.simpleName, "${e.message}")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
            } catch (e: Exception) {
                Log.d(javaClass.simpleName, "${e.message}")
            }
        }
    }

    fun logOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.d(javaClass.simpleName, "${e.message}")
        }
    }

    fun listenToAuthState() {
        auth.addAuthStateListener {
            this._currentUser.value = it.currentUser
        }
    }
}
