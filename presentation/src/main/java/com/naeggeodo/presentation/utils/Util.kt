package com.naeggeodo.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.net.ConnectivityManager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.di.App
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object Util {
    fun showShortToast(context: Context, msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

    fun showLongToast(context: Context, msg: String) =
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    fun showShortSnackbar(rootView: View, msg: String) =
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show()

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null &&
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    }

    fun loadingAnimation(context: Context, layout: ViewGroup, view: View, start: Boolean) {
        if (start) {
            val anim = AnimationUtils.loadAnimation(context, R.anim.rotate)
            layout.visibility = View.VISIBLE
            view.startAnimation(anim)
        } else {
            layout.visibility = View.GONE
            view.clearAnimation()
        }
    }

    fun getScreenSize(context: Context): Point {
        val w = context.resources.displayMetrics.widthPixels
        val h = context.resources.displayMetrics.heightPixels

        return Point(w, h)
    }

    fun hideKeyboard(activity: Activity) {
        activity.currentFocus?.let { view ->
            val imm =
                App.INSTANCE.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun persistImage(context: Context, bitmap: Bitmap, name: String): File {
        val filesDir: File = context.filesDir
        val imageFile = File(filesDir, name)

        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
        } catch (e: java.lang.Exception) {
            Timber.e(javaClass.simpleName, "Error writing bitmap / $e")
        }
        return imageFile
    }
}