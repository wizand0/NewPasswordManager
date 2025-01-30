package ru.wizand.newpasswordmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.wizand.newpasswordmanager.databinding.ActivityAddEditEntryBinding
import ru.wizand.newpasswordmanager.dialog.PasswordGeneratorDialog
import ru.wizand.newpasswordmanager.data.PasswordDatabase
import ru.wizand.newpasswordmanager.model.PasswordEntry
import kotlinx.coroutines.launch

class AddEditEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditEntryBinding
    private lateinit var database: PasswordDatabase
    private var isEditMode: Boolean = false
    private var entryId: Long? = null
    private lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appPreferences = AppPreferences(this)
        database = PasswordDatabase.getDatabase(this, appPreferences.getMasterPassword()!!)

        // Проверка, редактируем ли мы существующую запись
        val intent = intent
        if (intent.hasExtra("entry_id")) {
            isEditMode = true
            entryId = intent.getLongExtra("entry_id", -1L)
            loadEntry(entryId!!)
        }

        binding.btnGeneratePassword.setOnClickListener {
            showPasswordGeneratorDialog()
        }

        binding.btnSave.setOnClickListener {
            saveEntry()
        }
    }

    private fun loadEntry(id: Long) {
        lifecycleScope.launch {
            database.passwordDao().getAllEntries().collect { entries ->
                val entry = entries.find { it.id == id }
                entry?.let {
                    binding.etSiteName.setText(it.siteName)
                    binding.etUsername.setText(it.username)
                    binding.etPassword.setText(it.password)
                }
            }
        }
    }

    private fun saveEntry() {
        val siteName = binding.etSiteName.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (siteName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val entry = PasswordEntry(
            id = entryId ?: 0L,
            siteName = siteName,
            username = username,
            password = password
        )

        lifecycleScope.launch {
            database.passwordDao().insertEntry(entry)
            Toast.makeText(this@AddEditEntryActivity, "Запись сохранена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPasswordGeneratorDialog() {
        val dialog = PasswordGeneratorDialog { generatedPassword ->
            binding.etPassword.setText(generatedPassword)
        }
        dialog.show(supportFragmentManager, "PasswordGeneratorDialog")
    }
}