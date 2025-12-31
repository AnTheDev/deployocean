import 'package:json_annotation/json_annotation.dart';

part 'notification_model.g.dart';

enum NotificationType {
  @JsonValue('GENERAL')
  general,
  @JsonValue('FAMILY_INVITE')
  familyInvite,
  @JsonValue('FRIEND_REQUEST')
  friendRequest,
  @JsonValue('FRIDGE_EXPIRY')
  fridgeExpiry,
  @JsonValue('SHOPPING_REMINDER')
  shoppingReminder,
  @JsonValue('MEAL_PLAN')
  mealPlan,
}

@JsonSerializable()
class NotificationItem {
  final int id;
  final String title;
  final String message;
  final NotificationType type;
  final String? referenceType;
  final int? referenceId;
  final bool isRead;
  final DateTime createdAt;
  final DateTime? readAt;

  NotificationItem({
    required this.id,
    required this.title,
    required this.message,
    required this.type,
    this.referenceType,
    this.referenceId,
    required this.isRead,
    required this.createdAt,
    this.readAt,
  });

  factory NotificationItem.fromJson(Map<String, dynamic> json) => _$NotificationItemFromJson(json);
  Map<String, dynamic> toJson() => _$NotificationItemToJson(this);
}

@JsonSerializable()
class NotificationCount {
  final int total;
  final int unread;

  NotificationCount({
    required this.total,
    required this.unread,
  });

  factory NotificationCount.fromJson(Map<String, dynamic> json) => _$NotificationCountFromJson(json);
  Map<String, dynamic> toJson() => _$NotificationCountToJson(this);
}

@JsonSerializable()
class PaginatedNotifications {
  final List<NotificationItem> content;
  final int totalElements;
  final int totalPages;
  final int number;
  final int size;
  final bool first;
  final bool last;

  PaginatedNotifications({
    required this.content,
    required this.totalElements,
    required this.totalPages,
    required this.number,
    required this.size,
    required this.first,
    required this.last,
  });

  factory PaginatedNotifications.fromJson(Map<String, dynamic> json) => _$PaginatedNotificationsFromJson(json);
  Map<String, dynamic> toJson() => _$PaginatedNotificationsToJson(this);
}
