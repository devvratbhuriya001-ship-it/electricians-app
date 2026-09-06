package com.example.data.repository

import com.example.domain.models.AppNotification
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class FakeNotificationRepository(
    private val store: AppDatabaseStore = AppDatabaseStore
) : NotificationRepository {

    override fun getNotifications(userId: String): Flow<List<AppNotification>> = store.notifications

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        delay(200)
        store.markNotificationRead(notificationId)
        return Result.success(Unit)
    }

    override suspend fun markAllAsRead(userId: String): Result<Unit> {
        delay(300)
        store.markAllNotificationsRead()
        return Result.success(Unit)
    }
}
