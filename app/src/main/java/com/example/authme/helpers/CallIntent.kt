package com.example.authme.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent

class CallIntent(currentActivityContext: Context, activityToIntent: Activity) {

    private var context: Context? = currentActivityContext
    private var intentActivity = activityToIntent::class.java
    private var intent: Intent? = null

    init {
        thisIntent()
    }

    private fun thisIntent() {
        intent = Intent(context, intentActivity)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context?.startActivity(intent)
    }

}