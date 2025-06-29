package com.gracedev.expensetracker.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): User?

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    fun checkUsername(username: String): User?

    @Query("SELECT * FROM user WHERE uuid = :id")
    fun getUserById(id: Int): LiveData<User>

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}
