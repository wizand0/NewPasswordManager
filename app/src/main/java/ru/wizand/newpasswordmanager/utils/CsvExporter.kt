package ru.wizand.newpasswordmanager.utils

import ru.wizand.newpasswordmanager.model.PasswordEntry

object CsvExporter {
    fun export(entries: List<PasswordEntry>): String {
        val sb = StringBuilder()
        sb.append("Site Name,Username,Password\n")
        for (entry in entries) {
            sb.append("${escapeCsv(entry.siteName)},${escapeCsv(entry.username)},${escapeCsv(entry.password)}\n")
        }
        return sb.toString()
    }

    private fun escapeCsv(value: String): String {
        var escaped = value
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            escaped = value.replace("\"", "\"\"")
            escaped = "\"$escaped\""
        }
        return escaped
    }
}