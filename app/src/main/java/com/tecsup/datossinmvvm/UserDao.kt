package com.tecsup.datossinmvvm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User> // Obtiene todos los usuarios de la tabla

    @Insert
    suspend fun insert(user: User) // Inserta un nuevo usuario en la tabla

    @Query("DELETE FROM User WHERE uid = (SELECT MAX(uid) FROM User)")
    suspend fun deleteLastUser() // Elimina el último usuario
}
