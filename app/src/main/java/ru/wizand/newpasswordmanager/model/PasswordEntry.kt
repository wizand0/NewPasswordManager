package ru.wizand.newpasswordmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_entries")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,  // Long для совместимости с @Insert
    val siteName: String,
    val username: String,
    val password: String
)
