package com.example.inventory.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item): Int

    @Delete
    suspend fun delete(item: Item): Int


    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    // Métodos específicos para el Content Provider que devuelven Cursor
    @Query("SELECT * FROM items")
    fun selectAllCursor(): Cursor

    @Query("SELECT * FROM items WHERE id = :id")
    fun selectByIdCursor(id: Long): Cursor
}