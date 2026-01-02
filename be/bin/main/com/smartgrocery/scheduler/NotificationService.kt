package com.smartgrocery.scheduler

import com.smartgrocery.dto.fridge.ExpiringItemNotification
import com.smartgrocery.entity.NotificationType
import com.smartgrocery.repository.FamilyMemberRepository
import com.smartgrocery.repository.UserRepository
import com.smartgrocery.service.UserNotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service for sending notifications to users.
 * Currently mocks Firebase Cloud Messaging (FCM) by logging to console.
 * Replace with actual FCM integration in production.
 */
@Service
class NotificationService(
    private val familyMemberRepository: FamilyMemberRepository,
    private val userRepository: UserRepository,
    private val userNotificationService: UserNotificationService
) {

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun sendExpiringItemsNotification(familyId: Long, items: List<ExpiringItemNotification>) {
        if (items.isEmpty()) return

        val familyName = items.first().familyName
        val members = familyMemberRepository.findByFamilyIdWithUsers(familyId)

        members.forEach { member ->
            val user = member.user
            val fcmToken = user.fcmToken

            // Create individual notification for each item
            items.forEach { item ->
                val (title, body) = buildItemNotification(item, familyName)
                
                // Save notification to database
                userNotificationService.sendNotification(
                    userId = user.id!!,
                    title = title,
                    message = body,
                    type = NotificationType.FRIDGE_EXPIRY,
                    referenceType = "FAMILY",
                    referenceId = familyId
                )
            }

            if (fcmToken != null) {
                sendPushNotification(
                    token = fcmToken,
                    title = "🍎 Thực phẩm sắp hết hạn!",
                    body = buildNotificationBody(items),
                    data = mapOf(
                        "type" to "FRIDGE_EXPIRY",
                        "familyId" to familyId.toString(),
                        "itemCount" to items.size.toString()
                    )
                )
            } else {
                logger.info("User ${user.username} (${user.fullName}) has no FCM token registered")
            }
        }
    }
    
    fun sendExpiredItemNotification(familyId: Long, item: ExpiringItemNotification) {
        val familyName = item.familyName
        val members = familyMemberRepository.findByFamilyIdWithUsers(familyId)

        members.forEach { member ->
            val user = member.user
            val fcmToken = user.fcmToken

            val title = "⚠️ Thực phẩm đã quá hạn!"
            val body = "${item.productName} của nhóm $familyName đã quá hạn sử dụng, hãy vứt ngay"

            // Save notification to database
            userNotificationService.sendNotification(
                userId = user.id!!,
                title = title,
                message = body,
                type = NotificationType.FRIDGE_EXPIRY,
                referenceType = "FAMILY",
                referenceId = familyId
            )

            if (fcmToken != null) {
                sendPushNotification(
                    token = fcmToken,
                    title = title,
                    body = body,
                    data = mapOf(
                        "type" to "FRIDGE_EXPIRY",
                        "familyId" to familyId.toString(),
                        "itemId" to item.itemId.toString()
                    )
                )
            } else {
                logger.info("User ${user.username} (${user.fullName}) has no FCM token registered")
            }
        }
    }
    
    private fun buildItemNotification(item: ExpiringItemNotification, familyName: String): Pair<String, String> {
        val hoursUntilExpiration = item.daysUntilExpiration * 24
        
        return when {
            item.daysUntilExpiration <= 0L -> {
                Pair(
                    "⚠️ Thực phẩm đã quá hạn!",
                    "${item.productName} của nhóm $familyName đã quá hạn sử dụng, hãy vứt ngay"
                )
            }
            item.daysUntilExpiration == 1L -> {
                Pair(
                    "🍎 Thực phẩm sắp hết hạn!",
                    "${item.productName} của nhóm $familyName chỉ còn 24 giờ nữa là hết hạn, hãy sử dụng ngay"
                )
            }
            else -> {
                Pair(
                    "🍎 Thực phẩm sắp hết hạn!",
                    "${item.productName} của nhóm $familyName chỉ còn $hoursUntilExpiration giờ nữa là hết hạn, hãy sử dụng ngay"
                )
            }
        }
    }

    fun sendSingleItemNotification(
        userId: Long,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) {
        val user = userRepository.findById(userId).orElse(null) ?: return
        val fcmToken = user.fcmToken

        if (fcmToken != null) {
            sendPushNotification(fcmToken, title, body, data)
        } else {
            logger.info("User ${user.username} has no FCM token registered")
        }
    }

    /**
     * Mock FCM push notification sender.
     * In production, replace this with actual Firebase Admin SDK integration.
     */
    private fun sendPushNotification(
        token: String,
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        logger.info("""
            |
            |╔══════════════════════════════════════════════════════════════╗
            |║                    📱 PUSH NOTIFICATION                      ║
            |╠══════════════════════════════════════════════════════════════╣
            |║ Token: ${token.take(20)}...
            |║ Title: $title
            |║ Body: $body
            |║ Data: $data
            |╚══════════════════════════════════════════════════════════════╝
            |
        """.trimMargin())

        // TODO: Replace with actual FCM implementation
        // Example with Firebase Admin SDK:
        // val message = Message.builder()
        //     .setToken(token)
        //     .setNotification(Notification.builder()
        //         .setTitle(title)
        //         .setBody(body)
        //         .build())
        //     .putAllData(data)
        //     .build()
        // FirebaseMessaging.getInstance().send(message)
    }

    private fun buildNotificationBody(items: List<ExpiringItemNotification>): String {
        if (items.size == 1) {
            val item = items.first()
            return when (item.daysUntilExpiration) {
                0L -> "${item.productName} hết hạn hôm nay!"
                1L -> "${item.productName} sẽ hết hạn vào ngày mai"
                else -> "${item.productName} sẽ hết hạn trong ${item.daysUntilExpiration} ngày"
            }
        }

        val todayCount = items.count { it.daysUntilExpiration == 0L }
        val tomorrowCount = items.count { it.daysUntilExpiration == 1L }
        val laterCount = items.count { it.daysUntilExpiration > 1L }

        val parts = mutableListOf<String>()
        if (todayCount > 0) parts.add("$todayCount sản phẩm hết hạn hôm nay")
        if (tomorrowCount > 0) parts.add("$tomorrowCount sản phẩm hết hạn ngày mai")
        if (laterCount > 0) parts.add("$laterCount sản phẩm sắp hết hạn")

        return parts.joinToString(", ")
    }
}

