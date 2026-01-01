import 'package:json_annotation/json_annotation.dart';

part 'product_model.g.dart';

@JsonSerializable()
class Product {
  final int id;
  final String name;
  final String? imageUrl;
  final String defaultUnit;
  final int? avgShelfLife;
  final String? description;
  final bool isActive;
  final List<Category>? categories;

  Product({
    required this.id,
    required this.name,
    this.imageUrl,
    required this.defaultUnit,
    this.avgShelfLife,
    this.description,
    required this.isActive,
    this.categories,
  });

  factory Product.fromJson(Map<String, dynamic> json) => _$ProductFromJson(json);
  Map<String, dynamic> toJson() => _$ProductToJson(this);
}

@JsonSerializable()
class Category {
  final int id;
  final String name;
  final String? description;

  Category({
    required this.id,
    required this.name,
    this.description,
  });

  factory Category.fromJson(Map<String, dynamic> json) => _$CategoryFromJson(json);
  Map<String, dynamic> toJson() => _$CategoryToJson(this);
}
