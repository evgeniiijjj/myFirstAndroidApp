package com.example.bank_cards

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bank_cards.db.BankDb
import com.example.bank_cards.db.BankData
import com.example.bank_cards.dialog.ShowRequestHistory
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var db: BankDb
    private lateinit var binIin: EditText
    private lateinit var bank:TextView
    private lateinit var schemeNetwork: TextView
    private lateinit var type: TextView
    private lateinit var brand: TextView
    private lateinit var prepaid: TextView
    private lateinit var cardNumber: TextView
    private lateinit var cardNumberLength: TextView
    private lateinit var cardNumberLuhn: TextView
    private lateinit var country: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = BankDb.getDb(this)
        val showHistory: Button = findViewById(R.id.show_history)
        showHistory.setOnClickListener {
            Thread {
                var showText = ""
                db.getDao().getAllBankData().forEach {
                    showText += "${LocalDateTime.ofEpochSecond(it.dateTime, 0, ZoneOffset.UTC)} bin/iin: ${it.binIin} scheme/network: ${it.bankScheme} type: ${it.bankType} " +
                            "bank: ${it.bankName} ${it.bankCity} ${it.bankUrl} ${it.bankPhone} brand: ${it.bankBrand} prepaid: ${it.bankPrepaid} " +
                            "card_number: length: ${it.bankNumberLength} luhn: ${it.bankNumberLuhn} country: ${it.bankCountryAlpha2}" +
                            " ${it.bankCountryName} (latitude: ${it.bankCountryLatitude} longitude: ${it.bankCountryLongitude})\n"
                }
                val show = ShowRequestHistory(showText)
                val manager = supportFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                show.show(transaction, "showRequestList")
            }.start()
        }
        binIin = findViewById(R.id.bin_iin)
        bank = findViewById(R.id.bank)
        schemeNetwork = findViewById(R.id.scheme_network)
        type = findViewById(R.id.type)
        brand = findViewById(R.id.brand)
        prepaid = findViewById(R.id.prepaid)
        cardNumber = findViewById(R.id.card_number)
        cardNumberLength = findViewById(R.id.card_number_length)
        cardNumberLuhn = findViewById(R.id.card_number_luhn)
        country = findViewById(R.id.country)

        binIin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getData()
                true
            } else {
                false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData() {
        val url = getString(R.string.binlist_net) + binIin.text
        val queue = Volley.newRequestQueue(baseContext)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parseData(result)
            },
            {
                error -> Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseData(result: String) {
        var bankName = "?"
        var bankCity = "?"
        var bankUrl = "?"
        var bankPhone = "?"
        var bankScheme = "?"
        var bankType = "?"
        var bankBrand = "?"
        var bankPrepaid = false
        var bankNumberLength = 0
        var bankNumberLuhn = false
        var bankCountryAlpha2 = "?"
        var bankCountryName = "?"
        var bankCountryLatitude = "?"
        var bankCountryLongitude = "?"
        try {
            val json = JSONObject(result)
            val bankJson = json.getJSONObject("bank")
            val bankNumberJson = json.getJSONObject("number")
            val bankCountryJson = json.getJSONObject("country")
            bankName = bankJson.getString("name")
            bankCity = bankJson.getString("city")
            bankUrl = bankJson.getString("url")
            bankPhone = bankJson.getString("phone")
            bankScheme = json.getString("scheme")
            bankType = json.getString("type")
            bankBrand = json.getString("brand")
            bankPrepaid = json.getBoolean("prepaid")
            bankNumberLength = bankNumberJson.getInt("length")
            bankNumberLuhn = bankNumberJson.getBoolean("luhn")
            bankCountryAlpha2 = bankCountryJson.getString("alpha2")
            bankCountryName = bankCountryJson.getString("name")
            bankCountryLatitude = bankCountryJson.getString("latitude")
            bankCountryLongitude = bankCountryJson.getString("longitude")
        } catch (e: Exception) {
            Log.d("MyLog", "Error: ${e.message}")
        }

        schemeNetwork.text = "SCHEME/NETWORK\n$bankScheme"
        type.text = "TYPE\n$bankType"
        bank.autoLinkMask = Linkify.ALL
        bank.text = "BANK\n$bankName\n$bankCity\n$bankUrl\n$bankPhone"
        brand.text = "BRAND\n$bankBrand"
        prepaid.text = "PREPAID\n${if (bankPrepaid) "Yes" else "No"}"
        cardNumber.text = "CARD NUMBER"
        cardNumberLength.text = "LENGTH\n${bankNumberLength}"
        cardNumberLuhn.text = "LUHN\n${if (bankNumberLuhn) "Yes" else "No"}"
        country.text = "COUNTRY\n$bankCountryAlpha2 $bankCountryName\ngeo:${bankCountryLatitude},${bankCountryLongitude}"
        Linkify.addLinks(country, Pattern.compile("geo:.+", Pattern.CASE_INSENSITIVE), "")

        val bankData = BankData(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), binIin.text.toString().toInt(), bankName, bankCity, bankUrl, bankPhone
            , bankScheme, bankType, bankBrand, (if (bankPrepaid) "Yes" else "No"), bankNumberLength, (if (bankNumberLuhn) "Yes" else "No")
            , bankCountryAlpha2, bankCountryName, bankCountryLatitude, bankCountryLongitude)

        Thread {
            db.getDao().addNewBankData(bankData)
        }.start()
    }
}
