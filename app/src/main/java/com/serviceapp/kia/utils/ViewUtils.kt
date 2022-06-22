package com.serviceapp.kia.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.text.Editable
import android.util.Patterns
import android.util.TypedValue
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.serviceapp.kia.ui.activity.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.serviceapp.kia.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/*
 *Created by Adithya T Raj on 24-06-2021
*/

/*fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG ).show()
    Toast.makeText(this, message, Toast.LENGTH_SHORT ).show()
}*/
fun Context.toast(message: String){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(this.getLocaleStringResource(R.string.alert))
    builder.setMessage(message)
    builder.setPositiveButton(this.resources.getText(android.R.string.ok)) { dialog, _ ->
        dialog.dismiss()
    }
    builder.setCancelable(true)
    builder.show()
}

fun Context.simpleAlert(title: String, message: String) : DialogInterface? {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(this.resources.getText(android.R.string.ok)) { dialog, _ ->
        dialog.dismiss()
    }
    builder.setCancelable(true)
    return builder.show()
}

fun ProgressBar.show(){
    visibility = View.VISIBLE
}

fun ProgressBar.hide(){
    visibility = View.GONE
}

fun Button.show() {
    visibility = View.VISIBLE
}

fun Button.hide(){
    visibility = View.GONE
}

fun ImageView.show() {
    visibility = View.VISIBLE
}

fun ImageView.hide() {
    visibility = View.GONE
}

fun FloatingActionButton.shows(){
    visibility = View.VISIBLE
}

fun FloatingActionButton.hides(){
    visibility = View.GONE
}

fun View.snackBar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackBar ->
        snackBar.setAction(this.context.getText(android.R.string.ok)) {
            snackBar.dismiss()
        }
    }.show()
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun dpToPixel(dp : Int, context: Context) : Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun dpToSp(dp : Int, context: Context) : Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        dp.toFloat(),
        context.resources.displayMetrics
    )
}

fun progressView(
    view : View?,
    toVisibility: Int
) {
    view?.visibility = toVisibility
}

fun checkEditTextBlankOrEmpty(editable: Editable?) : Boolean {
    return editable.isNullOrBlank() || editable.isNullOrEmpty()
}

fun NavController.safeNavigate(
    destination: NavDirections
) {
    try {
        currentDestination?.getAction(destination.actionId)?.let {
            navigate(destination)
        }
    } catch (e: Exception) {
        //
        return
    }
}

fun NavController.safePopBackStack() {
    try {
        currentDestination?.let {
            popBackStack()
        }
    } catch (e: Exception) {
        //
        return
    }
}

fun File.getMimeType(fallback: String = "image/jpeg"): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase(Locale.getDefault())) }
        ?: fallback
}

fun dip(dp: Int, view: View): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), view.resources.displayMetrics).toInt()
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Fragment.showProgress() {
    view?.let {
        (activity as MainActivity?)?.progress(View.VISIBLE)
    }
}

fun Fragment.hideProgress() {
    view?.let {
        (activity as MainActivity?)?.progress(View.GONE)
    }
}

fun hasBiometricCapability(context: Context): Int {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate(BIOMETRIC_STRONG)
}

fun Context.isBiometricReady() =
    hasBiometricCapability(this) == BiometricManager.BIOMETRIC_SUCCESS

fun Fragment.exitApp() {
    view?.let {
        (activity as MainActivity?)?.exitApp()
    }
}

fun Calendar.formatTime(): String {
    return SimpleDateFormat("kk:mm a", Locale.getDefault()).format(this.time)
}

fun Calendar.formatDate(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this.time)
}

fun Calendar.formatDateServer(): String {
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.time)
}

fun Calendar.formatDateTime(): String {
    return SimpleDateFormat("kk:mm a, MMMM dd, yyyy", Locale.getDefault()).format(this.time)
}

fun TextView.disableCopyPaste() {
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return false
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            return false
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            //
        }

    }
}

fun Fragment.hideToolBarContent() {
    view?.let {
        (activity as MainActivity?)?.toolBarContentVisibility(View.GONE)
    }
}

fun Fragment.showToolBarContent() {
    view?.let {
        (activity as MainActivity?)?.toolBarContentVisibility(View.VISIBLE)
    }
}

fun Context.getLocaleStringResource(
    resourceId: Int,
): String {
    val requestedLocale = Locale.getDefault()
    val result: String
    val config =
        Configuration(resources.configuration)
    config.setLocale(requestedLocale)
    result = createConfigurationContext(config).getText(resourceId).toString()

    return result
}

fun Fragment.selectLanguage(switchLang : String) {
    view?.let {
        (activity as MainActivity?)?.selectLanguage(switchLang)
    }
}