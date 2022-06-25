package com.naeggeodo.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.naeggeodo.presentation.R

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
}