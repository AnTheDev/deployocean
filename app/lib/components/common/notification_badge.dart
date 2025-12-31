import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_boilerplate/providers/notification_provider.dart';
import 'package:flutter_boilerplate/pages/notification/notification_page.dart';

class NotificationBadge extends StatefulWidget {
  final Color? iconColor;
  final double iconSize;

  const NotificationBadge({
    Key? key,
    this.iconColor,
    this.iconSize = 24,
  }) : super(key: key);

  @override
  State<NotificationBadge> createState() => _NotificationBadgeState();
}

class _NotificationBadgeState extends State<NotificationBadge> {
  @override
  void initState() {
    super.initState();
    // Fetch notification count when widget is created
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<NotificationProvider>().fetchCount();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<NotificationProvider>(
      builder: (context, provider, child) {
        final unreadCount = provider.unreadCount;

        return IconButton(
          icon: Stack(
            clipBehavior: Clip.none,
            children: [
              Icon(
                Icons.notifications_outlined,
                color: widget.iconColor ?? Colors.black,
                size: widget.iconSize,
              ),
              if (unreadCount > 0)
                Positioned(
                  right: -6,
                  top: -6,
                  child: Container(
                    padding: const EdgeInsets.all(4),
                    decoration: const BoxDecoration(
                      color: Colors.red,
                      shape: BoxShape.circle,
                    ),
                    constraints: const BoxConstraints(
                      minWidth: 18,
                      minHeight: 18,
                    ),
                    child: Text(
                      unreadCount > 99 ? '99+' : unreadCount.toString(),
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 10,
                        fontWeight: FontWeight.bold,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
            ],
          ),
          onPressed: () {
            Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const NotificationPage()),
            );
          },
        );
      },
    );
  }
}
