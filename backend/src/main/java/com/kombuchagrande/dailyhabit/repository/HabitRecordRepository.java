package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.HabitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRecordRepository extends JpaRepository<HabitRecord, Long> {
}
