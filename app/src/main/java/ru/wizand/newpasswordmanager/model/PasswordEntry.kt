package ru.wizand.newpasswordmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_entries")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val siteName: String,
    val username: String,
    val password: String
)
