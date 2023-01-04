package com.example.bank_cards.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.bank_cards.R

class ShowRequestHistory(private val showText:String) : DialogFragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val textTitle = TextView(it)
            val textView = TextView(it)
            val scrollView = ScrollView(it)
            textTitle.text = getString(R.string.show_request_history_title)
            textTitle.gravity = 1
            textTitle.textSize = 20F
            textView.text = showText
            scrollView.addView(textView)
            scrollView.setPadding(50, 10, 50, 10)
            builder.setView(scrollView)
                .setCustomTitle(textTitle)
                .setPositiveButton("close") {
                        dialog, _ -> dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}