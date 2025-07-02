package com.gracedev.expensetracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gracedev.expensetracker.model.User
import com.gracedev.expensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val userLD = MutableLiveData<User?>()
    val usernameExistsLD = MutableLiveData<Boolean>()
    val registrationSuccessLD = MutableLiveData<Boolean>()

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    val passwordChangeResult = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, password)
            userLD.postValue(user)
        }
    }

    fun checkUsernameExists(username: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().checkUsername(username)
            usernameExistsLD.postValue(user != null)
        }
    }

    fun register(user: User) {
        launch {
            val db = buildDb(getApplication())
            try {
                db.userDao().insertUser(user)
                registrationSuccessLD.postValue(true)
            } catch (e: Exception) {
                registrationSuccessLD.postValue(false)
            }
        }
    }
    fun changePassword() {
        val oldPass = oldPassword.value ?: ""
        val newPass = newPassword.value ?: ""
        val repeatPass = repeatPassword.value ?: ""

        if (newPass != repeatPass) {
            passwordChangeResult.postValue(false)
            return
        }

        val sharedPref = getApplication<Application>().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", null)

        if (username == null) {
            passwordChangeResult.postValue(false)
            return
        }

        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, oldPass)
            if (user != null) {
                db.userDao().updatePassword(username, newPass)
                passwordChangeResult.postValue(true)
            } else {
                passwordChangeResult.postValue(false)
            }
        }
    }

}
