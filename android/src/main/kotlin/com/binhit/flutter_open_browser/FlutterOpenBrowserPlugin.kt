package com.binhit.flutter_open_browser

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import android.widget.Toast
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.binhit.flutter_open_browser.model.SettingModel
import java.lang.Exception
import java.lang.RuntimeException

class FlutterOpenBrowserPlugin private constructor(context: Activity?, methodChannel: MethodChannel) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "flutter_open_browser")
            channel.setMethodCallHandler(FlutterOpenBrowserPlugin(registrar.activity(), channel))
        }

        private const val _METHOD_OPEN_BROWSER_AUTO = "open_browser_auto"
        private const val _METHOD_OPEN_BROWSER_SELECT = "open_browser_select";
        private const val _MESSAGE_KEY_URL = "open_browser_message_key_url"
        private const val _MESSAGE_KEY_SHOWCASE = "open_browser_message_key_showcase"
        private const val _MESSAGE_KEY_ERROR = "open_browser_message_key_error"
        private const val _URL_IS_NULL_OR_EMPTY = "Url is null or empty"
        private const val _ERROR_SETTING_MODEL = "SettingModel is null"
        private const val _ERROR_MESSAGE_SHOW_SETTING_MODEL = "Error Message show on SettingModel is null"
        private const val _NOT_HANDLER_URL = "No application can handle this URL. Please install a Web browser"
        private const val _NOT_VALID_URL = "URL is not valid."
    }

    private var _context: Activity? = context
    private var channel: MethodChannel? = methodChannel

    init {
        this.channel!!.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            call.method == _METHOD_OPEN_BROWSER_AUTO -> {
                val settingModel = SettingModel()
                settingModel.url = call.argument<String>(_MESSAGE_KEY_URL)
                settingModel.intCaseShowError = call.argument<Int>(_MESSAGE_KEY_SHOWCASE)
                        ?: SettingModel.SHOW_TOAST
                settingModel.messageErrorShow = call.argument<String>(_MESSAGE_KEY_ERROR)
                if (checkSetting(settingModel)) {
                    openBrowserAuto(settingModel.intCaseShowError, settingModel.url!!)
                }
            }
            call.method == _METHOD_OPEN_BROWSER_SELECT -> {
                val settingModel = SettingModel()
                settingModel.url = call.argument<String>(_MESSAGE_KEY_URL)
                settingModel.intCaseShowError = call.argument<Int>(_MESSAGE_KEY_SHOWCASE)
                        ?: SettingModel.SHOW_TOAST
                settingModel.messageErrorShow = call.argument<String>(_MESSAGE_KEY_ERROR)
                if (checkSetting(settingModel)) {
                    openBrowserSelect(settingModel.intCaseShowError, settingModel.url!!)
                }
            }
            else -> result.notImplemented()
        }
    }

    private fun openBrowserAuto(intShowCase: Int, url: String) {
        try {
            var convertUrl = url
            if (!url.startsWith("http://") && !url.startsWith("https://")) convertUrl = "http://$url"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(convertUrl))
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            _context!!.startActivity(intent)
        } catch (ex: Exception) {
            onHandlerError(intShowCase, _NOT_HANDLER_URL)
        }
    }

    private fun openBrowserSelect(intShowCase: Int, url: String) {
        try {
            var convertUrl = url
            if (!url.startsWith("http://") && !url.startsWith("https://")) convertUrl = "http://$url"
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(convertUrl)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            _context!!.startActivity(Intent.createChooser(intent, "Choose browser:"))
        } catch (ex: Exception) {
            onHandlerError(intShowCase, _NOT_HANDLER_URL)
        }
    }

    private fun checkSetting(settingModel: SettingModel?): Boolean {
        if (settingModel == null) {
            throw RuntimeException(_ERROR_SETTING_MODEL)
        }
        val url = settingModel.url
        val intCaseShowError = settingModel.intCaseShowError
        val messageErrorShow = settingModel.messageErrorShow
        if (url.isNullOrEmpty() || url.isNullOrBlank()) {
            onHandlerError(intCaseShowError, _URL_IS_NULL_OR_EMPTY)
            return false
        }
        if (!URLUtil.isValidUrl(settingModel.url)) {
            onHandlerError(intCaseShowError, _NOT_VALID_URL)
            return false
        }
        if (messageErrorShow.isNullOrEmpty() || messageErrorShow.isNullOrBlank()) {
            onHandlerError(intCaseShowError, _ERROR_MESSAGE_SHOW_SETTING_MODEL)
            return false
        }
        return true
    }

    private fun onHandlerError(intShowCase: Int, error: String) {
        if (intShowCase == SettingModel.SHOW_TOAST) Toast.makeText(_context, error, Toast.LENGTH_SHORT).show()
        else showDialogError(error)
    }

    private fun showDialogError(message: String) {
        val mAlertDialog = AlertDialog.Builder(_context)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
        val mDialog: Dialog = mAlertDialog.create()
        mDialog.setCanceledOnTouchOutside(false)
    }
}
