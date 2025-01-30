package ru.wizand.newpasswordmanager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ru.wizand.newpasswordmanager.adapter.PasswordAdapter
import ru.wizand.newpasswordmanager.databinding.ActivityMainBinding
import ru.wizand.newpasswordmanager.data.PasswordDatabase
import ru.wizand.newpasswordmanager.model.PasswordEntry
import ru.wizand.newpasswordmanager.utils.CsvExporter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: PasswordDatabase
    private lateinit var adapter: PasswordAdapter
    private lateinit var appPreferences: AppPreferences

    private val exportLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri: Uri? ->
        uri?.let { exportToCsv(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appPreferences = AppPreferences(this)
        database = PasswordDatabase.getDatabase(this, appPreferences.getMasterPassword()!!)

        adapter = PasswordAdapter { entry -> showDeleteDialog(entry) }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditEntryActivity::class.java)
            startActivity(intent)
        }

        binding.fabExport.setOnClickListener {
            requestExportPermissionAndExport()
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchEntries(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchEntries(it) }
                return true
            }
        })

        loadEntries()
    }

    private fun loadEntries() {
        lifecycleScope.launch {
            database.passwordDao().getAllEntries().collect { entries ->
                adapter.submitList(entries)
            }
//            val entries = database.passwordDao()
//                .getAllEntries().first()
            // получаем сразу List<PasswordEntry>

//            adapter.submitList(entries) // теперь здесь список
        }
    }

    private fun searchEntries(query: String) {
        lifecycleScope.launch {
            database.passwordDao().searchEntries("%$query%").collect { entries ->
                adapter.submitList(entries)
            }
//            val entries = database.passwordDao()
//                .searchEntries("%$query%")
//                .first() // получаем сразу List<PasswordEntry>

//            adapter.submitList(entries) // теперь здесь список


        }
    }

    private fun showDeleteDialog(entry: PasswordEntry) {
        AlertDialog.Builder(this)
            .setTitle("Удаление записи")
            .setMessage("Вы уверены, что хотите удалить эту запись?")
            .setPositiveButton("Да") { _, _ ->
                deleteEntry(entry)
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun deleteEntry(entry: PasswordEntry) {
        lifecycleScope.launch {
            database.passwordDao().deleteEntry(entry)
            Toast.makeText(this@MainActivity, "Запись удалена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestExportPermissionAndExport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
// Для Android 10 и выше используем Storage Access Framework
            startExport()
        } else {
            // Для Android 9 и ниже запрашиваем разрешение
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_EXPORT)
            } else {
                startExport()
            }
        }
    }

    private fun startExport() {
        val filename = "passwords_export_${System.currentTimeMillis()}.csv"
        exportLauncher.launch(filename)
    }

    private fun exportToCsv(uri: Uri) {
        lifecycleScope.launch {
//            val entries = database.passwordDao().getAllEntries().collect { it }
            val entries = database.passwordDao().getAllEntries().first()

            val csvData = CsvExporter.export(entries)
            val success = kotlin.runCatching {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(csvData.toByteArray())
                }
            }.isSuccess

            if (success) {
                Toast.makeText(this@MainActivity, "Экспорт выполнен успешно", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Ошибка экспорта", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_EXPORT = 100
    }

    // Обработчик результата запроса разрешений
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_EXPORT) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startExport()
            } else {
                Toast.makeText(this, "Разрешение отклонено", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}