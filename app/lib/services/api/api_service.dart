import 'package:dio/dio.dart';
import 'package:flutter_boilerplate/constants/api_config.dart';

class ApiService {
  final Dio _dio;

  ApiService() 
      : _dio = Dio(BaseOptions(
          baseUrl: ApiConfig.baseUrl,
          connectTimeout: const Duration(milliseconds: 5000),
          receiveTimeout: const Duration(milliseconds: 3000),
        ));

  Future<Response> login(String username, String password) async {
    try {
      final response = await _dio.post(
        ApiConfig.login,
        data: {
          'username': username, // or 'email' depending on your backend
          'password': password,
        },
      );
      return response;
    } on DioException catch (e) {
      if (e.response != null) {
        print('Login Error: ${e.response!.data}');
        return e.response!;
      } else {
        print('Dio error: ${e.message}');
        throw Exception('Failed to connect to the server.');
      }
    }
  }

  Future<Response> register(Map<String, dynamic> userData) async {
    try {
      final response = await _dio.post(ApiConfig.register, data: userData);
      return response;
    } on DioException catch (e) {
      if (e.response != null) {
        return e.response!;
      } else {
        throw Exception('Failed to connect to the server.');
      }
    }
  }
}
