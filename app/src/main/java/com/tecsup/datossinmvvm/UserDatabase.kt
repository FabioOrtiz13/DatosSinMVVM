package com.tecsup.datossinmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao // Metodo abstracto que devuelve una instancia de UserDao
}
