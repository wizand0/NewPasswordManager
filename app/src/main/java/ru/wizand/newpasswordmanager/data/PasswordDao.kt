package ru.wizand.newpasswordmanager.data


import androidx.room.*
import ru.wizand.newpasswordmanager.model.PasswordEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM password_entries ORDER BY siteName ASC")
    fun getAllEntries(): Flow<List<PasswordEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: PasswordEntry): Long  // Возвращает ID вставленной записи

    @Delete
    suspend fun deleteEntry(entry: PasswordEntry): Int  // Возвращает количество удалённых строк

    @Query("SELECT * FROM password_entries WHERE username LIKE :query OR siteName LIKE :query ORDER BY siteName ASC")
    fun searchEntries(query: String): Flow<List<PasswordEntry>>
}


