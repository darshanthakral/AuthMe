package com.example.authme.helpers

import android.content.Context
import android.widget.Toast

class ToastMe(activityContext: Context, toastMessage: String) {

    init {
        toast(activityContext, toastMessage)
    }

    private fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}