// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'notification_model.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

NotificationItem _$NotificationItemFromJson(Map<String, dynamic> json) =>
    NotificationItem(
      id: (json['id'] as num).toInt(),
      title: json['title'] as String,
      message: json['message'] as String,
      type: $enumDecode(_$NotificationTypeEnumMap, json['type']),
      referenceType: json['referenceType'] as String?,
      referenceId: (json['referenceId'] as num?)?.toInt(),
      isRead: json['isRead'] as bool,
      createdAt: DateTime.parse(json['createdAt'] as String),
      readAt: json['readAt'] == null
          ? null
          : DateTime.parse(json['readAt'] as String),
    );

Map<String, dynamic> _$NotificationItemToJson(NotificationItem instance) =>
    <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'message': instance.message,
      'type': _$NotificationTypeEnumMap[instance.type]!,
      'referenceType': instance.referenceType,
      'referenceId': instance.referenceId,
      'isRead': instance.isRead,
      'createdAt': instance.createdAt.toIso8601String(),
      'readAt': instance.readAt?.toIso8601String(),
    };

const _$NotificationTypeEnumMap = {
  NotificationType.general: 'GENERAL',
  NotificationType.familyInvite: 'FAMILY_INVITE',
  NotificationType.friendRequest: 'FRIEND_REQUEST',
  NotificationType.fridgeExpiry: 'FRIDGE_EXPIRY',
  NotificationType.shoppingReminder: 'SHOPPING_REMINDER',
  NotificationType.mealPlan: 'MEAL_PLAN',
};

NotificationCount _$NotificationCountFromJson(Map<String, dynamic> json) =>
    NotificationCount(
      total: (json['total'] as num).toInt(),
      unread: (json['unread'] as num).toInt(),
    );

Map<String, dynamic> _$NotificationCountToJson(NotificationCount instance) =>
    <String, dynamic>{
      'total': instance.total,
      'unread': instance.unread,
    };

PaginatedNotifications _$PaginatedNotificationsFromJson(
        Map<String, dynamic> json) =>
    PaginatedNotifications(
      content: (json['content'] as List<dynamic>)
          .map((e) => NotificationItem.fromJson(e as Map<String, dynamic>))
          .toList(),
      totalElements: (json['totalElements'] as num).toInt(),
      totalPages: (json['totalPages'] as num).toInt(),
      number: (json['number'] as num).toInt(),
      size: (json['size'] as num).toInt(),
      first: json['first'] as bool,
      last: json['last'] as bool,
    );

Map<String, dynamic> _$PaginatedNotificationsToJson(
        PaginatedNotifications instance) =>
    <String, dynamic>{
      'content': instance.content,
      'totalElements': instance.totalElements,
      'totalPages': instance.totalPages,
      'number': instance.number,
      'size': instance.size,
      'first': instance.first,
      'last': instance.last,
    };
