package com.binhit.flutter_open_browser.model

class SettingModel {
    companion object {
        const val SHOW_TOAST = 1
        const val SHOW_DIALOG = -1
    }

    var url: String? = ""
    var intCaseShowError: Int = SHOW_TOAST
    var messageErrorShow: String? = ""
}