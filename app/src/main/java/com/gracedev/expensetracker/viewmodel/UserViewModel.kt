package com.gracedev.expensetracker.viewmodel

import android.app.Application
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


}
