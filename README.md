# flutter_open_browser

A flutter plugin to open a url with default browser or select browser

This plugin is under development, APIs might change.

#### Installation
Install the library from pub:
```dart
dependencies:
  flutter_open_browser: ^1.0.3
```

#### Import the library
```dart
import 'package:flutter_open_browser/flutter_open_browser.dart';
```

##### Open the web page
```dart
SettingModel settingModel = SettingModel(
          url: "http://google.com",//Your url
          intCaseShowError: SettingModel.SHOW_TOAST,//Show dialog or toast if error
          messageErrorShow: "Error");//Your error message.
await FlutterOpenBrowser.openBrowserSelect(settingModel: settingModel);
```