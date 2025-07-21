package com.mollin.app.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mollin.app.data.user.UserEntity
import com.mollin.app.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()

    fun loadUser(uid: String) {
        viewModelScope.launch {
            val fetchedUser = repository.getUser(uid)
            _user.value = fetchedUser
        }
    }

    fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insert(user)
            _user.value = user
        }
    }
}
