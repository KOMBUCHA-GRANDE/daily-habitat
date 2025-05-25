package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
