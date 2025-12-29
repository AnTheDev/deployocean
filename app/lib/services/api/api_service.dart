import 'package:dio/dio.dart';
import 'package:flutter_boilerplate/constants/api_config.dart';
import 'package:flutter_boilerplate/models/auth_model.dart';
import 'package:flutter_boilerplate/models/fridge_item.dart';
import 'package:flutter_boilerplate/services/shared_pref/shared_pref.dart'; // Import SharedPref

class ApiService {
  final Dio _dio;

  ApiService._() 
      : _dio = Dio(BaseOptions(
          baseUrl: ApiConfig.baseUrl,
          connectTimeout: const Duration(milliseconds: 15000),
          receiveTimeout: const Duration(milliseconds: 15000),
        )) {
    _dio.interceptors.add(LogInterceptor(responseBody: true, requestBody: true));

    // --- AUTHENTICATION INTERCEPTOR ---
    _dio.interceptors.add(QueuedInterceptorsWrapper(
      onRequest: (options, handler) async {
        // Get the token from storage
        final token = await SharedPref.getToken();
        if (token != null) {
          // Add the token to the request header
          options.headers['Authorization'] = 'Bearer $token';
        }
        return handler.next(options); // Continue with the request
      },
      onError: (e, handler) {
        // TODO: Handle token expiration and refresh token logic here if needed
        return handler.next(e);
      },
    ));
  }

  static final ApiService _instance = ApiService._();
  factory ApiService() => _instance;

  // ... (The rest of the methods remain the same)

  // --- Auth ---
  Future<LoginData> login({required String username, required String password, String? deviceToken}) async {
    try {
      final response = await _dio.post(ApiConfig.login, data: {'username': username, 'password': password, if (deviceToken != null) 'device_token': deviceToken});
      if (response.data['code'] == 1000 && response.data['data'] != null) {
        return LoginData.fromJson(response.data['data']);
      } else {
        throw Exception(response.data['message'] ?? 'Login failed');
      }
    } on DioException catch (e) {
      throw _handleDioError(e);
    } catch (e) {
      throw Exception('An unexpected error occurred: ${e.toString()}');
    }
  }

  // --- Fridge ---
  Future<List<FridgeItem>> getFridgeItems(int familyId) async {
    try {
      final response = await _dio.get(ApiConfig.familyFridgeItems(familyId));
      if (response.data['code'] == 1000 && response.data['data'] != null) {
        List<dynamic> itemsJson = response.data['data'];
        return itemsJson.map((json) => FridgeItem.fromJson(json)).toList();
      } else {
        throw Exception(response.data['message'] ?? 'Failed to load fridge items');
      }
    } on DioException catch (e) {
      throw _handleDioError(e);
    } catch (e) {
      throw Exception('An unexpected error occurred: ${e.toString()}');
    }
  }

  Future<FridgeItem> addFridgeItem(Map<String, dynamic> itemData) async {
    try {
      final response = await _dio.post(ApiConfig.fridgeItems, data: itemData);
      if (response.data['code'] == 1000 && response.data['data'] != null) {
        return FridgeItem.fromJson(response.data['data']);
      } else {
        throw Exception(response.data['message'] ?? 'Failed to add item');
      }
    } on DioException catch (e) {
      throw _handleDioError(e);
    } catch (e) {
      throw Exception('An unexpected error occurred: ${e.toString()}');
    }
  }

  Future<void> deleteFridgeItem(int itemId) async {
    try {
      final response = await _dio.delete(ApiConfig.fridgeItemById(itemId));
      if (response.data['code'] != 1000) {
        throw Exception(response.data['message'] ?? 'Failed to delete item');
      }
    } on DioException catch (e) {
      throw _handleDioError(e);
    } catch (e) {
      throw Exception('An unexpected error occurred: ${e.toString()}');
    }
  }

  // --- Error Handling Helper ---
  Exception _handleDioError(DioException e) {
    if (e.response != null && e.response!.data != null) {
      if (e.response!.data is Map) {
        return Exception(e.response!.data['message'] ?? 'An unknown error occurred');
      }
      return Exception(e.response!.data.toString());
    }
    return Exception('Failed to connect to the server. Please check your network.');
  }
}
