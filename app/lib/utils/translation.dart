import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart' show rootBundle;

class Translations {
  Translations(this.locale);
  final Locale locale;

  // Initialize with an empty map
  static Map<String, dynamic> _localizedValues = {};
  static String trackingLanguage = 'en';

  static Translations? of(BuildContext context) {
    return Localizations.of<Translations>(context, Translations);
  }

  // Make 'args' nullable and provide a default empty map if needed
  static String text(String key, {Map<String, String>? args}) {
    String? result = _localizedValues[key];
    if (result == null) return '** $key not found';

    if (args != null) {
      args.forEach((argKey, value) {
        result = result!.replaceAll('{$argKey}', value);
      });
    }
    return result!;
  }

  static Future<Translations> load(Locale locale) async {
    final Translations translations = Translations(locale);
    trackingLanguage = translations.currentLanguage;
    final String jsonContent =
        await rootBundle.loadString('assets/langs/$trackingLanguage.json');
    _localizedValues = json.decode(jsonContent).cast<String, dynamic>();
    return translations;
  }

  String get currentLanguage => locale.languageCode;
}

class TranslationsDelegate extends LocalizationsDelegate<Translations> {
  const TranslationsDelegate();

  @override
  bool isSupported(Locale locale) => ['en', 'vi'].contains(locale.languageCode);

  @override
  Future<Translations> load(Locale locale) => Translations.load(locale);

  @override
  bool shouldReload(TranslationsDelegate old) => false;
}
