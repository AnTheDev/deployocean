import 'package:dio/dio.dart';
import 'package:flutter_boilerplate/services/shared_pref/shared_pref.dart';

const BASE_URL = 'https://dev-api.timtour.vn/api/v1';

class ApiService {
  late final Dio client;

  ApiService() {
    client = Dio();
    client.options.baseUrl = BASE_URL;
    client.options.connectTimeout = const Duration(milliseconds: 5000);
    client.options.receiveTimeout = const Duration(milliseconds: 30000);
    client.options.followRedirects = false;
    client.options.validateStatus = (status) {
      return status != null && status < 500;
    };
  }

  void setToken(String authToken) {
    client.options.headers['Authorization'] = 'Bearer $authToken';
  }

  Future<void> clientSetup() async {
    final String? authToken = SharedPref.sharedPref.getString(AUTH_TOKEN);
    if (authToken != null && authToken.isNotEmpty) {
      client.options.headers['Authorization'] = 'Bearer $authToken';
    }
  }
}
