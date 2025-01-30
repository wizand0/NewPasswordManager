package ru.wizand.newpasswordmanager.utils

object PasswordGenerator {
    private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SPECIAL = "!@#$%^&*()-_=+<>?{}[]|/~`"

    fun generatePassword(
        length: Int,
        includeUpper: Boolean,
        includeLower: Boolean,
        includeDigits: Boolean,
        includeSpecial: Boolean
    ): String {
        var allowedChars = ""
        if (includeUpper) allowedChars += UPPER
        if (includeLower) allowedChars += LOWER
        if (includeDigits) allowedChars += DIGITS
        if (includeSpecial) allowedChars += SPECIAL

        if (allowedChars.isEmpty()) {
            allowedChars = LOWER
        }

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}