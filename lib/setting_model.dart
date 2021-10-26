class SettingModel {
  static const int SHOW_TOAST = 1;
  static const int SHOW_DIALOG = -1;
  String url = "";
  int intCaseShowError = SHOW_TOAST;
  String messageErrorShow = "";

  SettingModel({this.url, this.intCaseShowError, this.messageErrorShow});
}
