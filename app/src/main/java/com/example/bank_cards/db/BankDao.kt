package com.example.bank_cards.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BankDao {
    @Insert
    fun addNewBankData(bankData: BankData)
    @Query("select * from bank_data")
    fun getAllBankData(): List<BankData>
}