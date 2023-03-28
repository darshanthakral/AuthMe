package com.example.authme.helpers

import android.widget.EditText

object CheckIfNull {
    fun isNull(et: EditText): Boolean {
        return et.text.isEmpty() || et.text.isBlank()
    }
}