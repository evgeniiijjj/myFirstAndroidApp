package com.example.bank_cards.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_data")
data class BankData (
    @PrimaryKey
    @ColumnInfo(name = "date_time")
    var dateTime: Long,
    @ColumnInfo(name = "bin_iin")
    var binIin: Int,
    @ColumnInfo(name = "bank_name")
    var bankName: String,
    @ColumnInfo(name = "bank_city")
    var bankCity: String,
    @ColumnInfo(name = "bank_url")
    var bankUrl: String,
    @ColumnInfo(name = "bank_phone")
    var bankPhone: String,
    @ColumnInfo(name = "bank_scheme")
    var bankScheme: String,
    @ColumnInfo(name = "bank_type")
    var bankType: String,
    @ColumnInfo(name = "bank_brand")
    var bankBrand: String,
    @ColumnInfo(name = "bank_prepaid")
    var bankPrepaid: String,
    @ColumnInfo(name = "bank_number_length")
    var bankNumberLength: Int,
    @ColumnInfo(name = "bank_number_luhn")
    var bankNumberLuhn: String,
    @ColumnInfo(name = "bank_country_alpha2")
    var bankCountryAlpha2: String,
    @ColumnInfo(name = "bank_country_name")
    var bankCountryName: String,
    @ColumnInfo(name = "bank_country_latitude")
    var bankCountryLatitude: String,
    @ColumnInfo(name = "bank_country_longitude")
    var bankCountryLongitude: String)
