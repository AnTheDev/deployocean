import 'package:flutter/material.dart';
import 'package:flutter_boilerplate/models/product_model.dart';
import 'package:flutter_boilerplate/providers/base_provider.dart';
import 'package:flutter_boilerplate/services/api/api_service.dart';
import 'package:flutter_boilerplate/services/locator.dart';

class ProductProvider extends BaseProvider {
  final ApiService _apiService = locator<ApiService>();

  List<Product> _products = [];
  List<Product> get products => _products;

  Product? _currentProduct;
  Product? get currentProduct => _currentProduct;

  String? _errorMessage;
  String? get errorMessage => _errorMessage;

  String _searchQuery = '';
  String get searchQuery => _searchQuery;

  // Fetch products with pagination
  Future<void> fetchProducts({int page = 0, int size = 20}) async {
    setStatus(ViewStatus.Loading);
    _errorMessage = null;
    try {
      _products = await _apiService.getProducts(page: page, size: size);
    } catch (e) {
      _errorMessage = e.toString().replaceFirst('Exception: ', '');
    } finally {
      setStatus(ViewStatus.Ready);
    }
  }

  // Search products by name
  Future<void> searchProducts(String query, {int page = 0, int size = 20}) async {
    setStatus(ViewStatus.Loading);
    _errorMessage = null;
    _searchQuery = query;
    try {
      if (query.trim().isEmpty) {
        _products = await _apiService.getProducts(page: page, size: size);
      } else {
        _products = await _apiService.searchProducts(query, page: page, size: size);
      }
    } catch (e) {
      _errorMessage = e.toString().replaceFirst('Exception: ', '');
    } finally {
      setStatus(ViewStatus.Ready);
    }
  }

  // Get product by ID
  Future<void> fetchProductDetails(int productId) async {
    setStatus(ViewStatus.Loading);
    _errorMessage = null;
    try {
      _currentProduct = await _apiService.getProductById(productId);
    } catch (e) {
      _errorMessage = e.toString().replaceFirst('Exception: ', '');
    } finally {
      setStatus(ViewStatus.Ready);
    }
  }

  // Get products by category
  Future<void> fetchProductsByCategory(int categoryId, {int page = 0, int size = 20}) async {
    setStatus(ViewStatus.Loading);
    _errorMessage = null;
    try {
      _products = await _apiService.getProductsByCategory(categoryId, page: page, size: size);
    } catch (e) {
      _errorMessage = e.toString().replaceFirst('Exception: ', '');
    } finally {
      setStatus(ViewStatus.Ready);
    }
  }

  // Clear search
  void clearSearch() {
    _searchQuery = '';
    _products = [];
    notifyListeners();
  }
}
