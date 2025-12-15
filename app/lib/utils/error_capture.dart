import 'dart:async';

import 'package:sentry_flutter/sentry_flutter.dart';

/// Reports an error and its stack trace to Sentry.
///
/// This function simplifies error reporting by using the static Sentry API,
/// which automatically captures relevant context like device, OS, and release info.
Future<void> reportError(Object error, StackTrace stackTrace) async {
  // In debug builds, Sentry might be configured not to send events.
  // Printing here ensures visibility during development.
  print('Caught error: $error');

  // Sentry.captureException is the modern way to report errors.
  // It returns a SentryId if the event is successfully sent.
  try {
    final eventId = await Sentry.captureException(
      error,
      stackTrace: stackTrace,
    );

    if (eventId.toString().isNotEmpty) {
      print('Successfully reported to Sentry. Event ID: $eventId');
    } else {
      // This can happen if the event is dropped, e.g., by a beforeSend callback
      // or if Sentry is disabled.
      print('Sentry event was not sent.');
    }
  } catch (e) {
    print('Failed to report to Sentry: $e');
  }
}
