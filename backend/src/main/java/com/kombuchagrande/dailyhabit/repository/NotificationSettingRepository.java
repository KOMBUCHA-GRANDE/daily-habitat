package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.NotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {
}
