package com.mollin.app.presentation.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val address: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onNameChange(name: String) = _uiState.update { it.copy(fullName = name) }
    fun onEmailChange(email: String) = _uiState.update { it.copy(email = email) }
    fun onPasswordChange(pw: String) = _uiState.update { it.copy(password = pw) }
    fun onAddressChange(addr: String) = _uiState.update { it.copy(address = addr) }

    fun onRegisterClick(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.fullName.isBlank()
            || state.email.isBlank()
            || state.password.length < 6
            || state.address.isBlank()
        ) {
            _uiState.update { it.copy(error = "Por favor, completa todos los campos correctamente") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(state.email, state.password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener
                    val userMap = mapOf(
                        "fullName" to state.fullName,
                        "email" to state.email,
                        "address" to state.address
                    )
                    db.collection("users")
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            _uiState.update { it.copy(isLoading = false) }
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("RegisterVM", "createUser error", e)
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
        }
    }
}
