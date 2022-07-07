package com.naeggeodo.presentation.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.PictureDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.naeggeodo.presentation.R
import com.naeggeodo.presentation.di.App
import com.naeggeodo.presentation.di.GlideApp
import com.naeggeodo.presentation.utils.svg.SvgSoftwareLayerSetter
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Util {
    var toast: Toast? = null

    fun getSvgRequestBuilder(context: Context): RequestBuilder<PictureDrawable> =
        GlideApp.with(context)
            .`as`(PictureDrawable::class.java)
//            .placeholder(R.drawable.ic_error)
            .error(R.drawable.ic_error)
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(SvgSoftwareLayerSetter())

    fun loadImageAndSetView(context: Context, imagePath: String, view: ImageView) {
        val uri = Uri.parse(imagePath)
        if (uri.toString().split((".")).last() == "svg") {
            getSvgRequestBuilder(context).load(uri)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(view)
        } else {
            Glide.with(context)
                .load(uri)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(view)
        }
    }

    fun showShortToast(context: Context, msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    fun showLongToast(context: Context, msg: String) {
        toast?.cancel()
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast!!.show()
    }

    fun showShortSnackbar(rootView: View, msg: String) =
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show()

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetwork != null &&
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    }

    fun loadingAnimation(context: Context, layout: ViewGroup, view: View, show: Boolean) {
        if (show) {
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


    fun encodeImage(path: String): String {
        val bitmap = BitmapFactory.decodeFile(path)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
//        Timber.e("before ${stream.toByteArray().size} ${stream.size()} bitmap byte count: ${bitmap.byteCount} row bytes: ${bitmap.rowBytes} ${bitmap.rowBytes * bitmap.height}")
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }

    fun decodeString(data: String): ByteArray = Base64.decode(data, Base64.DEFAULT)


    fun getTimeDiff(prevDate: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
        val prev = sdf.parse(prevDate)
        val curDate = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString()
        val curr = sdf.parse(curDate)

        return if (prev != null && curr != null) {
            curr.time - prev.time
        } else {
            0L
        }
    }


    fun getTimeStr(timeDiff: Long): String {
        val seconds = timeDiff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30

        return if (seconds < 0) {
            "???"
        } else if (seconds < 60) {
            "방금 전"
        } else if (minutes < 60) {
            "${minutes}분 전"
        } else if (hours < 24) {
            "${hours}시간 전"
        } else if (days < 30) {
            "${days}일 전"
        } else if (months < 30) {
            "${months}달 전"
        } else {
            "오래 전"
        }
    }

    fun getMessageTimeString(dateTime: LocalDateTime): String {
        val hour = dateTime.hour
        val minute = dateTime.minute
        val day = dateTime.dayOfMonth
        val month = dateTime.month.value

        val f = DecimalFormat("00")

        var result = ""
        result += "$month/$day "
        result += if (hour >= 12) "오후 " else "오전 "
        result += if (hour > 12) "${hour - 12}:" else if (hour == 0) "12:" else "${hour}:"
        result += f.format(minute)

        Timber.e("time $result $day $month")
        return result
    }
}