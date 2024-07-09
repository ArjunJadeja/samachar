package com.arjun.samachar.utils

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog

typealias RetryHandler = () -> Unit

object UiHelper {

    fun showApiRetryAlert(
        context: Context,
        header: String,
        message: String,
        retryButtonClicked: RetryHandler
    ) {
        try {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(header)
            builder.setMessage(message)
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Retry", fun(dialog: DialogInterface, _: Int) {
                    retryButtonClicked()
                    dialog.dismiss()
                }
            )
            builder.show()
        } catch (e: Exception) {
            Log.e("Retry Alert Dialog", e.message.toString())
        }
    }

}