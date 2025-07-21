package com.mollin.app.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mollin.app.data.user.UserEntity
import com.mollin.app.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, error = null) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, error = null) }
    }

    fun onLoginClick(onSuccess: () -> Unit) {
        val current = _uiState.value

        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(error = "Completa el correo y la contraseña") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(current.email, current.password)
                .addOnSuccessListener { result ->
                    val firebaseUser = result.user ?: return@addOnSuccessListener

                    db.collection("users").document(firebaseUser.uid)
                        .get()
                        .addOnSuccessListener { doc ->
                            val name = doc.getString("fullName") ?: ""
                            val email = doc.getString("email") ?: ""
                            val address = doc.getString("address") ?: ""
                            val phone = doc.getString("phone") ?: ""

                            viewModelScope.launch {
                                userRepo.insert(
                                    UserEntity(
                                        uid = firebaseUser.uid,
                                        name = name,
                                        phone = phone,
                                        address = address
                                    )
                                )
                                _uiState.update { it.copy(isLoading = false) }
                                onSuccess()
                            }
                        }
                        .addOnFailureListener { ex ->
                            _uiState.update { it.copy(isLoading = false, error = ex.localizedMessage) }
                        }
                }
                .addOnFailureListener { ex ->
                    _uiState.update { it.copy(isLoading = false, error = ex.localizedMessage) }
                }
        }
    }
}
