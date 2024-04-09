package com.example.submission_intermediate.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.submission_intermediate.uitls.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel (private val pref: UserPreferences) : ViewModel() {

    fun getLoginSession(): LiveData<Boolean> {
        return pref.getSession().asLiveData()
    }

    fun saveLoginSession(loginSession: Boolean) {
        viewModelScope.launch {
            pref.saveSession(loginSession)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun getName(): LiveData<String> {
        return pref.getName().asLiveData()
    }

    fun saveName(token: String) {
        viewModelScope.launch {
            pref.saveName(token)
        }
    }

    fun getUid(): LiveData<String> {
        return pref.getUid().asLiveData()
    }

    fun saveUid(token: String) {
        viewModelScope.launch {
            pref.saveUid(token)
        }
    }

    fun clearDataLogin() {
        viewModelScope.launch {
            pref.clearDataLogin()
        }
    }

}