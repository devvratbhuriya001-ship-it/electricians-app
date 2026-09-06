package com.example.data.repository

import com.example.domain.repository.*

object RepositoryProvider {
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }
    val electricianRepository: ElectricianRepository by lazy { FakeElectricianRepository() }
    val staffRepository: StaffRepository by lazy { FakeStaffRepository() }
    val ownerRepository: OwnerRepository by lazy { FakeOwnerRepository() }
    val notificationRepository: NotificationRepository by lazy { FakeNotificationRepository() }
}
