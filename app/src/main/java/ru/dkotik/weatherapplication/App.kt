package ru.dkotik.weatherapplication

import android.app.Application
import androidx.room.Room
import ru.dkotik.weatherapplication.room.HistoryDao
import ru.dkotik.weatherapplication.room.HistoryDataBase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (instance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            instance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        ).allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }

            return db!!.historyDao()
        }
    }
}