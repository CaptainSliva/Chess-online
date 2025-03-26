package com.example.chess.Functions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.chess.R
import com.google.android.material.snackbar.Snackbar

class FunctionsApp(private val context: Context) {
    fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showSnackBar(view: View, text: String, bColor: Int, length: Int, action: Boolean, actionText: String = "") {
        val snackbar = Snackbar.make(view, text, length)
        snackbar.setBackgroundTint(bColor)
        snackbar.setTextColor(context.resources.getColor(R.color.black))
        snackbar.setActionTextColor(context.resources.getColor(R.color.sb_close))
        if (action) {
            snackbar.setAction("Закрыть") {
                Toast.makeText(context, actionText, Toast.LENGTH_SHORT).show()
            }
        }
        snackbar.show()
    }

}