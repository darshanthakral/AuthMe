package com.example.authme.helpers

import android.widget.EditText

class EditTextHandler(editText: EditText, wantEditTextHint: Boolean, message: String) {

    init {
        handle(editText, wantEditTextHint, message)
    }

    private fun handle(et: EditText, getEditTextHint: Boolean, msg: String) {
        et.requestFocus()
        if (getEditTextHint) et.error = "${et.hint}" + msg
        else et.error = msg
        return
    }

}