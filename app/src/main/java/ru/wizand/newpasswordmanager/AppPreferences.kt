package ru.wizand.newpasswordmanager


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class AppPreferences(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun isFirstRun(): Boolean {
        return prefs.getString("master_password", null) == null
    }

    fun setMasterPassword(password: String) {
        prefs.edit().putString("master_password", password).apply()
    }

    fun getMasterPassword(): String? {
        return prefs.getString("master_password", null)
    }
}