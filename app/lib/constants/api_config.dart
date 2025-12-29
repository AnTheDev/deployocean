import 'dart:io' show Platform;
import 'package:flutter/foundation.dart' show kIsWeb;

class ApiConfig {
  static String get baseUrl {
    const String apiVersion = '/v1';
    String base;

    if (kIsWeb) {
      base = 'http://127.0.0.1:8080/api';
    } else {
      if (Platform.isAndroid) {
        base = 'http://10.0.2.2:8080/api';
      } else {
        base = 'http://127.0.0.1:8080/api';
      }
    }
    return base + apiVersion;
  }

  // --- Auth Endpoints ---
  static const String login = '/auth/login';
  static const String register = '/auth/register';

  // --- Fridge Item Endpoints ---
  static const String fridgeItems = '/fridge-items';
  static String familyFridgeItems(int familyId) => '/families/$familyId/fridge-items';
  static String familyFridgeItemsActive(int familyId) => '/families/$familyId/fridge-items/active';
  static String familyFridgeItemsExpiring(int familyId) => '/families/$familyId/fridge-items/expiring';
  static String familyFridgeItemsExpired(int familyId) => '/families/$familyId/fridge-items/expired';
  static String familyFridgeItemsStatistics(int familyId) => '/families/$familyId/fridge-items/statistics';
  static String fridgeItemById(int id) => '/fridge-items/$id';
  static String consumeFridgeItem(int id) => '/fridge-items/$id/consume';
  static String discardFridgeItem(int id) => '/fridge-items/$id/discard';
}
