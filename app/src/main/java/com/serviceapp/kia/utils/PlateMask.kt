package com.serviceapp.kia.utils

import android.text.Editable
import android.text.TextWatcher
import java.util.*


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class PlateMask : TextWatcher {

    private var updatedText: String? = null
    private var editing: Boolean = false

    companion object {

        private const val MAX_LENGTH = 13
        private const val MIN_LENGTH = 3
    }


    override fun beforeTextChanged(
        charSequence: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {

    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        if (text.toString() == updatedText || editing) return

        var digits = text.toString()
        val length = digits.length

        if (length > MAX_LENGTH) {
            digits = digits.substring(0, MAX_LENGTH)
        }

        updatedText = if (length == MIN_LENGTH  && text[2].toString() != "/") {

            val startTxt = digits.substring(0, 2)
            val endTxt = digits.substring(2)

            String.format(Locale.getDefault(), "%s/%s", startTxt, endTxt)
        } else {
            digits = digits.substring(0)

            String.format(Locale.getDefault(), "%s", digits)
        }
    }

    override fun afterTextChanged(editable: Editable) {

        if (editing) return

        editing = true

        editable.clear()
        editable.insert(0, updatedText)

        editing = false
    }
}