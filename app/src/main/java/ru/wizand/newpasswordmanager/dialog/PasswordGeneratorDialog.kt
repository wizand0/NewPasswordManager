package ru.wizand.newpasswordmanager.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        // Настраиваем кнопку "Отмена"
        binding.btnSave.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


