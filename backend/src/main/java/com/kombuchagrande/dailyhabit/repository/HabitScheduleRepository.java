package com.kombuchagrande.dailyhabit.repository;

import com.kombuchagrande.dailyhabit.entity.HabitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitScheduleRepository extends JpaRepository<HabitSchedule, Long> {
}
