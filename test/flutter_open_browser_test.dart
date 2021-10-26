import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_open_browser/flutter_open_browser.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_open_browser');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterOpenBrowser.platformVersion, '42');
  });
}
