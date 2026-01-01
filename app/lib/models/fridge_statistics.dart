import 'package:json_annotation/json_annotation.dart';

part 'fridge_statistics.g.dart';

@JsonSerializable()
class FridgeStatistics {
  final int totalItems;
  final int activeItems;
  final int expiringSoonItems;
  final int expiredItems;
  final int consumedItems;
  final int discardedItems;
  final Map<String, int>? itemsByLocation;
  final Map<String, int>? itemsByCategory;

  const FridgeStatistics({
    required this.totalItems,
    required this.activeItems,
    required this.expiringSoonItems,
    required this.expiredItems,
    required this.consumedItems,
    required this.discardedItems,
    this.itemsByLocation,
    this.itemsByCategory,
  });

  factory FridgeStatistics.fromJson(Map<String, dynamic> json) => _$FridgeStatisticsFromJson(json);
  Map<String, dynamic> toJson() => _$FridgeStatisticsToJson(this);
}
