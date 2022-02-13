package ru.dkotik.weatherapplication.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun getDataByWord(city: String): List<HistoryEntity>

    @Query("UPDATE HistoryEntity SET temperature = :temp WHERE id = :id")
    fun updateQuery(temp: Int, id: Int)

    @Query("DELETE FROM HistoryEntity WHERE city LIKE :city")
    fun deleteQuery(city: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}