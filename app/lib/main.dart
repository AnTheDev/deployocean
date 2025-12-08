import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:provider/provider.dart';

// Import new packages
import 'package:firebase_core/firebase_core.dart';
import 'package:sentry_flutter/sentry_flutter.dart';

import 'constants/app_theme.dart';
import 'constants/strings.dart';
import 'pages/home/home_page.dart';
import 'providers/base_provider.dart';
import 'routes.dart';
import 'services/locator.dart';
import 'services/shared_pref/shared_pref.dart';
import 'utils/translation.dart';

Future<void> main() async {
  // This needs to be the first line
  WidgetsFlutterBinding.ensureInitialized();

  // Set preferred orientations and UI overlays
  await SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitUp,
    DeviceOrientation.portraitDown,
  ]);
  // Use the new method for UI overlays. This hides the status bar.
  await SystemChrome.setEnabledSystemUIMode(SystemUiMode.immersive);

  await SharedPref.init();
  setupLocator();

  // Initialize Firebase and Sentry
  await SentryFlutter.init(
    (options) async {
      options.dsn = Strings.dnsSentry;
      // Initialize Firebase inside Sentry to capture initialization errors
      await Firebase.initializeApp();
    },
    // Run the app
    appRunner: () => runApp(
      MultiProvider(
        providers: [
          ChangeNotifierProvider(create: (_) => BaseProvider()),
        ],
        child: MyApp(),
      ),
    ),
  );
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: Strings.appName,
      theme: themeData,
      routes: Routes.routes,
      home: HomePage(),
      supportedLocales: const [
        Locale('en'),
        Locale('vi'),
      ],
      localizationsDelegates: const [
        TranslationsDelegate(),
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
      ],
      localeResolutionCallback:
          (Locale? locale, Iterable<Locale> supportedLocales) {
        if (locale != null) {
          for (Locale supportedLocale in supportedLocales) {
            if (supportedLocale.languageCode == locale.languageCode) {
              return supportedLocale;
            }
          }
        }
        return supportedLocales.first;
      },
    );
  }
}
