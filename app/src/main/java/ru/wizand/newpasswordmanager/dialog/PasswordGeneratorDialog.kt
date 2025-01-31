package ru.wizand.newpasswordmanager.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import ru.wizand.newpasswordmanager.databinding.DialogPasswordGeneratorBinding
import ru.wizand.newpasswordmanager.utils.PasswordGenerator


class PasswordGeneratorDialog(
    private val onPasswordGenerated: (String) -> Unit
) : DialogFragment() {

    private var _binding: DialogPasswordGeneratorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPasswordGeneratorBinding.inflate(layoutInflater)


        // Кнопки на базе positive и negative buttons из alertDialog
//        return AlertDialog.Builder(requireContext())
//            .setTitle("Генерация пароля")
//            .setView(binding.root)
//
//            .setPositiveButton("Генерировать") { _, _ ->
//                val length = binding.etLength.text.toString().toIntOrNull() ?: 12
//                val includeUpper = binding.cbUppercase.isChecked
//                val includeLower = binding.cbLowercase.isChecked
//                val includeDigits = binding.cbDigits.isChecked
//                val includeSpecial = binding.cbSpecial.isChecked
//
//                val password = PasswordGenerator.generatePassword(
//                    length = length,
//                    includeUpper = includeUpper,
//                    includeLower = includeLower,
//                    includeDigits = includeDigits,
//                    includeSpecial = includeSpecial
//                )
//                onPasswordGenerated(password)
//            }
//            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
//            .create()

        // Создаем билдер AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
//            .setTitle("Генерация пароля")
//            .setView(view)
//            .create()
            .setTitle("Генерация пароля")
            .setView(binding.root)
            .create()
// Кнопки materialDesign
        // Настраиваем кнопку "Генерировать"
        binding.btnGeneratePassword.setOnClickListener {
            val length = binding.etLength.text.toString().toIntOrNull() ?: 12
            val includeUpper = binding.cbUppercase.isChecked
            val includeLower = binding.cbLowercase.isChecked
            val includeDigits = binding.cbDigits.isChecked
            val includeSpecial = binding.cbSpecial.isChecked

            // Генерация пароля (предполагается, что у вас есть метод для этого)
            val password = PasswordGenerator.generatePassword(
                length = length,
                includeUpper = includeUpper,
                includeLower = includeLower,
                includeDigits = includeDigits,
                includeSpecial = includeSpecial
            )
            Toast.makeText(requireContext(), "Сгенерированный пароль: $password", Toast.LENGTH_LONG).show()

            // Обработка сгенерированного пароля
            onPasswordGenerated(password)

            // Закрываем диалог
            dialog.dismiss()
        }

        // Настраиваем кнопку "Генерировать"
        binding.btnCopyPassword.setOnClickListener {
            val length = binding.etLength.text.toString().toIntOrNull() ?: 12
            val includeUpper = binding.cbUppercase.isChecked
            val includeLower = binding.cbLowercase.isChecked
            val includeDigits = binding.cbDigits.isChecked
            val includeSpecial = binding.cbSpecial.isChecked

            // Генерация пароля (предполагается, что у вас есть метод для этого)
            val password = PasswordGenerator.generatePassword(
                length = length,
                includeUpper = includeUpper,
                includeLower = includeLower,
                includeDigits = includeDigits,
                includeSpecial = includeSpecial
            )
            Toast.makeText(requireContext(), "Сгенерированный пароль: $password", Toast.LENGTH_LONG).show()
            textCopyThenPost(password)

            // Обработка сгенерированного пароля
            onPasswordGenerated(password)

            // Закрываем диалог
            dialog.dismiss()
        }

        // Настраиваем кнопку "Отмена"
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun textCopyThenPost(textCopied:String) {
//        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipboardManager: ClipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // When setting the clipboard text.
        clipboardManager.setPrimaryClip(ClipData.newPlainText   ("", textCopied))
        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)

            Toast.makeText(requireContext(), "Пароль: $textCopied скопирован", Toast.LENGTH_LONG).show()
    }

}


