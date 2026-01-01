// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'fridge_statistics.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FridgeStatistics _$FridgeStatisticsFromJson(Map<String, dynamic> json) =>
    FridgeStatistics(
      totalItems: (json['totalItems'] as num?)?.toInt() ?? 0,
      activeItems: (json['activeItems'] as num?)?.toInt() ?? 0,
      expiringSoonItems: (json['expiringSoonItems'] as num?)?.toInt() ?? 0,
      expiredItems: (json['expiredItems'] as num?)?.toInt() ?? 0,
      consumedItems: (json['consumedItems'] as num?)?.toInt() ?? 0,
      discardedItems: (json['discardedItems'] as num?)?.toInt() ?? 0,
      itemsByLocation: (json['itemsByLocation'] as Map<String, dynamic>?)?.map(
        (k, e) => MapEntry(k, (e as num).toInt()),
      ),
      itemsByCategory: (json['itemsByCategory'] as Map<String, dynamic>?)?.map(
        (k, e) => MapEntry(k, (e as num).toInt()),
      ),
    );

Map<String, dynamic> _$FridgeStatisticsToJson(FridgeStatistics instance) =>
    <String, dynamic>{
      'totalItems': instance.totalItems,
      'activeItems': instance.activeItems,
      'expiringSoonItems': instance.expiringSoonItems,
      'expiredItems': instance.expiredItems,
      'consumedItems': instance.consumedItems,
      'discardedItems': instance.discardedItems,
      'itemsByLocation': instance.itemsByLocation,
      'itemsByCategory': instance.itemsByCategory,
    };
