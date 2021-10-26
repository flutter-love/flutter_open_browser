import 'dart:async';
import 'package:flutter/services.dart';
import 'package:flutter_open_browser/setting_model.dart';

class FlutterOpenBrowser {
  static const _METHOD_OPEN_BROWSER_AUTO = "open_browser_auto";
  static const _METHOD_OPEN_BROWSER_SELECT = "open_browser_select";
  static const _MESSAGE_KEY_URL = "open_browser_message_key_url";
  static const _MESSAGE_KEY_SHOWCASE = "open_browser_message_key_showcase";
  static const _MESSAGE_KEY_ERROR = "open_browser_message_key_error";
  static const MethodChannel _channel =
      const MethodChannel('flutter_open_browser');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Null> openBrowserAuto({SettingModel settingModel}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent(_MESSAGE_KEY_URL, () => settingModel.url);
    args.putIfAbsent(
        _MESSAGE_KEY_SHOWCASE,
        () => settingModel.intCaseShowError == null
            ? 1
            : settingModel.intCaseShowError);
    args.putIfAbsent(_MESSAGE_KEY_ERROR, () => settingModel.messageErrorShow);
    await _channel.invokeMethod(_METHOD_OPEN_BROWSER_AUTO, args);
    return null;
  }

  static Future<Null> openBrowserSelect({SettingModel settingModel}) async {
    Map<String, dynamic> args = <String, dynamic>{};
    args.putIfAbsent(_MESSAGE_KEY_URL, () => settingModel.url);
    args.putIfAbsent(
        _MESSAGE_KEY_SHOWCASE,
        () => settingModel.intCaseShowError == null
            ? 1
            : settingModel.intCaseShowError);
    args.putIfAbsent(_MESSAGE_KEY_ERROR, () => settingModel.messageErrorShow);
    await _channel.invokeMethod(_METHOD_OPEN_BROWSER_SELECT, args);
    return null;
  }
}
