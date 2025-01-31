package ru.wizand.newpasswordmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import ru.wizand.newpasswordmanager.R
import ru.wizand.newpasswordmanager.databinding.ItemPasswordEntryBinding
import ru.wizand.newpasswordmanager.model.PasswordEntry

class PasswordAdapter(
    private val onDelete: (PasswordEntry) -> Unit
) : ListAdapter<PasswordEntry, PasswordAdapter.PasswordViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding = ItemPasswordEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PasswordViewHolder(private val binding: ItemPasswordEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isPasswordVisible = false

        fun bind(entry: PasswordEntry) {
            binding.tvSiteName.text = entry.siteName
            binding.tvUsername.text = "Логин: ${entry.username}"
            binding.tvPassword.text = "••••••••"

            binding.btnShowPassword.setOnClickListener {
                if (isPasswordVisible) {
                    binding.tvPassword.text = "••••••••"
//                    binding.btnShowPassword.setIconResource(R.drawable.ic_visibility_off)
//                    binding.btnShowPassword.setImageResource(R.drawable.ic_visibility_off)
                } else {
                    binding.tvPassword.text = entry.password
//                    binding.btnShowPassword.setImageResource(R.drawable.ic_visibility_on)
                }
                isPasswordVisible = !isPasswordVisible
            }

            binding.btnDelete.setOnClickListener {
                onDelete(entry)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PasswordEntry>() {
        override fun areItemsTheSame(oldItem: PasswordEntry, newItem: PasswordEntry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PasswordEntry, newItem: PasswordEntry): Boolean =
            oldItem == newItem
    }
}

