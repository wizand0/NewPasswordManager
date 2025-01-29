package ru.wizand.newpasswordmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.wizand.newpasswordmanager.model.PasswordEntry
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [PasswordEntry::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao

    companion object {
        @Volatile
        private var INSTANCE: PasswordDatabase? = null

        fun getDatabase(context: Context, passphrase: String): PasswordDatabase {
            return INSTANCE ?: synchronized(this) {
                // Инициализируем SQLCipher
                SQLiteDatabase.loadLibs(context)

                val passphraseBytes = SQLiteDatabase.getBytes(passphrase.toCharArray())
                val factory = SupportFactory(passphraseBytes)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDatabase::class.java,
                    "password_manager.db"
                )
                    .openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}