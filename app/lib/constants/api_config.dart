class ApiConfig {
  // For Android Emulator, 10.0.2.2 is the address of the host machine (your computer).
  // If you are using a real device, you need to use your computer's local IP address.
  // Example: static const String baseUrl = 'http://192.168.1.10:8000/api';
  static const String baseUrl = 'http://10.0.2.2:8000/api';

  // Auth endpoints
  static const String login = '/auth/login';
  static const String register = '/auth/register';
}
