package com.example.bank_cards.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BankData::class], version = 2)
abstract class BankDb : RoomDatabase() {
    abstract fun getDao(): BankDao
    companion object {
        fun getDb(context: Context): BankDb {
            return Room.databaseBuilder(
                context.applicationContext,
                BankDb::class.java,
                "requestHistory.db"
            ).build()
        }
    }
}